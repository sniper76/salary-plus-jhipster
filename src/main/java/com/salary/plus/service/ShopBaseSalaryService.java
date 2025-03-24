package com.salary.plus.service;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopUserBaseSalaryMapping;
import com.salary.plus.domain.User;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.repository.ShopUserBaseSalaryMappingRepository;
import com.salary.plus.service.dto.AdminUserSalaryMappingDTO;
import com.salary.plus.service.dto.ShopBaseSalaryDTO;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ShopBaseSalaryService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopBaseSalaryService.class);

    private final ShopBaseSalaryRepository shopBaseSalaryRepository;
    private final ShopUserBaseSalaryMappingRepository shopUserBaseSalaryMappingRepository;
    private final ShopUserMappingService shopUserMappingService;

    public ShopBaseSalary create(Long shopId, ShopBaseSalaryDTO shopPenaltyDTO) {
        ShopBaseSalary shopPenalty = new ShopBaseSalary();
        shopPenalty.setShopId(shopId);
        shopPenalty.setNameKo(shopPenaltyDTO.getNameKo());
        shopPenalty.setNameEn(shopPenaltyDTO.getNameEn());
        shopPenalty.setPrice(shopPenaltyDTO.getPrice());
        shopPenalty.created(shopPenaltyDTO.getLogin());
        return shopBaseSalaryRepository.save(shopPenalty);
    }

    public Optional<ShopBaseSalary> update(Long shopId, ShopBaseSalaryDTO shopPenaltyDTO) {
        return Optional.of(get(shopId, shopPenaltyDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(it -> {
                it.setShopId(shopId);
                it.setNameKo(shopPenaltyDTO.getNameKo());
                it.setNameEn(shopPenaltyDTO.getNameEn());
                it.setPrice(shopPenaltyDTO.getPrice());
                it.setActivated(shopPenaltyDTO.isActivated());
                it.updateLastModified(shopPenaltyDTO.getLogin());
                return shopBaseSalaryRepository.save(it);
            });
    }

    public void delete(Long shopId, long salesItemId) {
        get(shopId, salesItemId).ifPresent(shopBaseSalaryRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<ShopBaseSalary> getAllBaseSalaries(Long shopId) {
        return shopBaseSalaryRepository.findAllByShopIdAndActivated(shopId, true);
    }

    @Transactional(readOnly = true)
    public Page<ShopBaseSalary> getAllBaseSalaries(Long shopId, Pageable pageable) {
        return shopBaseSalaryRepository.findAllByShopId(shopId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<ShopBaseSalary> get(Long shopId, long shopBaseSalaryId) {
        return shopBaseSalaryRepository.findByIdAndShopIdAndActivated(shopBaseSalaryId, shopId, true);
    }

    public void createUserBaseSalaryMappings(String login, AdminUserSalaryMappingDTO mappingDTO) {
        if (mappingDTO.getUserId() == null) {
            //all
            final List<User> users = shopUserMappingService.getMappingUsersByShopIdAndCommissionTargetUser(mappingDTO.getShopId());
            users.forEach(it -> createAndUpdate(login, mappingDTO, it.getId()));
        } else {
            createAndUpdate(login, mappingDTO, mappingDTO.getUserId());
        }
    }

    private void createAndUpdate(String login, AdminUserSalaryMappingDTO mappingDTO, Long userId) {
        shopUserBaseSalaryMappingRepository
            .findByShopIdAndUserId(mappingDTO.getShopId(), userId)
            .ifPresentOrElse(
                elem -> {
                    if (Objects.equals(elem.getShopBaseSalaryId(), mappingDTO.getShopBaseSalaryId())) {
                        elem.setActivated(true);
                        elem.updateLastModified(login);
                        shopUserBaseSalaryMappingRepository.save(elem);
                    } else {
                        shopUserBaseSalaryMappingRepository.delete(elem);
                        createUserBaseSalaryMapping(login, mappingDTO.getShopBaseSalaryId(), userId);
                    }
                },
                () -> createUserBaseSalaryMapping(login, mappingDTO.getShopBaseSalaryId(), userId)
            );
    }

    private void createUserBaseSalaryMapping(String login, Long shopBaseSalaryId, Long userId) {
        ShopUserBaseSalaryMapping mapping = new ShopUserBaseSalaryMapping();
        mapping.setShopBaseSalaryId(shopBaseSalaryId);
        mapping.setUserId(userId);
        mapping.created(login);
        shopUserBaseSalaryMappingRepository.save(mapping);
    }
}
