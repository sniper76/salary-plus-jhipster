package com.salary.plus.service;

import com.salary.plus.config.Constants;
import com.salary.plus.domain.Authority;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
import com.salary.plus.repository.AuthorityRepository;
import com.salary.plus.repository.UserRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.dto.AdminModelDTO;
import com.salary.plus.service.dto.AdminUserDTO;
import com.salary.plus.service.dto.ShopModelResponse;
import com.salary.plus.service.dto.ShopUserResponse;
import com.salary.plus.service.dto.UserDTO;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import io.undertow.util.BadRequestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final CacheManager cacheManager;
    private final ShopUserMappingService shopUserMappingService;
    private final ShopService shopService;

    public Optional<User> activateRegistration(String key) {
        LOG.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                LOG.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        LOG.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        LOG.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO, String generatePassword, String generateResetKey) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(generatePassword);
        user.setPassword(encryptedPassword);
        user.setResetKey(generateResetKey);
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setModelNo(userDTO.getModelNo());
        user.setCommissionTargetYn(userDTO.isCommissionTargetYn());
        user.setDiscountAcceptYn(userDTO.isDiscountAcceptYn());
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        createMapping(userDTO, userRepository.save(user));
        this.clearUserCaches(user);
        LOG.debug("Created Information for User: {}", user);
        return user;
    }

    private void createMapping(AdminUserDTO userDTO, User user) {
        if (userDTO.getShopStrings() != null) {
            final Set<Long> databaseShopIds = shopUserMappingService
                .getAllByUserId(user.getId())
                .stream()
                .map(ShopUserMapping::getShopId)
                .collect(Collectors.toSet());

            final Set<Long> requestShopIds = userDTO.getShopStrings().stream().map(Long::parseLong).collect(Collectors.toSet());

            Map<String, Set<Long>> result = compareSets(databaseShopIds, requestShopIds);

            result.get("deleteTarget").forEach(shopId -> shopUserMappingService.deleteByUserIdAndShopId(user.getId(), shopId));

            result
                .get("createTarget")
                .forEach(shopId -> {
                    ShopUserMapping userMapping = new ShopUserMapping();
                    userMapping.setUserId(user.getId());
                    userMapping.setShopId(shopId);
                    shopUserMappingService.save(userMapping);
                });
        }
    }

    public Map<String, Set<Long>> compareSets(Set<Long> left, Set<Long> right) {
        Set<Long> deleteTarget = new HashSet<>(left);
        Set<Long> noneTarget = new HashSet<>(left);
        Set<Long> createTarget = new HashSet<>(right);

        deleteTarget.removeAll(right); // 왼쪽에만 있는 값
        noneTarget.retainAll(right); // 양쪽에 모두 있는 값
        createTarget.removeAll(left); // 오른쪽에만 있는 값

        Map<String, Set<Long>> result = new HashMap<>();
        result.put("deleteTarget", deleteTarget);
        result.put("noneTarget", noneTarget);
        result.put("createTarget", createTarget);

        return result;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setModelNo(userDTO.getModelNo());
                user.setCommissionTargetYn(userDTO.isCommissionTargetYn());
                user.setDiscountAcceptYn(userDTO.isDiscountAcceptYn());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);

                createMapping(userDTO, userRepository.save(user));
                this.clearUserCaches(user);
                LOG.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                this.clearUserCaches(user);
                LOG.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                userRepository.save(user);
                this.clearUserCaches(user);
                LOG.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                LOG.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    @Transactional(readOnly = true)
    public List<String> getUserAuthorities() throws BadRequestException {
        final String userEmail = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new BadRequestException("User could not be found"));
        return userRepository.findAuthoritiesByLogin(userEmail);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired every day, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                LOG.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).toList();
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evictIfPresent(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evictIfPresent(user.getEmail());
        }
    }

    @Transactional(readOnly = true)
    public List<ShopModelResponse> getAllModels(Long shopId) {
        return userRepository.findAllByShopId(shopId, true, true);
    }

    @Transactional(readOnly = true)
    public List<ShopUserResponse> getAllUsersByDate(Long shopId, String date) {
        return userRepository.findAllByShopIdAndDate(shopId, date, true, true);
    }

    public void createModels(String login, AdminModelDTO modelDTO) {
        final String csvData = modelDTO.getCsvData();
        final Shop shop = shopService
            .get(modelDTO.getShopId())
            .orElseThrow(() -> new BadRequestAlertException("Not found shop", "shopManagement", "idexists"));
        final int userSize = shopUserMappingService.getMappingUsersByShopIdAndActivated(modelDTO.getShopId()).size();
        AtomicInteger count = new AtomicInteger(userSize);

        try (BufferedReader reader = new BufferedReader(new StringReader(csvData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] strings = line.split(",");
                createModel(shop, count.incrementAndGet(), login, strings);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createModel(Shop shop, int userSize, String login, String[] strings) {
        final String shopNameEn = shop.getNameEn().replaceAll("\\s", "");
        final String email = "%s_%s".formatted(shopNameEn, userSize);
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setLogin(email);
        adminUserDTO.setFirstName(strings[0]);
        adminUserDTO.setLastName(strings[1]);
        adminUserDTO.setAuthorities(build(strings[2]));
        adminUserDTO.setModelNo(strings[3]);
        adminUserDTO.setLangKey("en");
        adminUserDTO.setCreatedBy(login);
        adminUserDTO.setEmail(email + "@salary-plus.ph");
        adminUserDTO.setShopStrings(build(String.valueOf(shop.getId())));
        createUser(adminUserDTO, RandomUtil.generatePassword(), RandomUtil.generateResetKey());
    }

    private HashSet<String> build(String value) {
        final HashSet<String> objects = new HashSet<>();
        objects.add(value);
        return objects;
    }
}
