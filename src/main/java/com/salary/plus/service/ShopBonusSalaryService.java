package com.salary.plus.service;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopBonusSalary;
import com.salary.plus.domain.ShopUserBaseSalaryMapping;
import com.salary.plus.domain.User;
import com.salary.plus.enums.BonusType;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.repository.ShopBonusSalaryRepository;
import com.salary.plus.repository.ShopUserBaseSalaryMappingRepository;
import com.salary.plus.service.dto.AdminUserSalaryMappingDTO;
import com.salary.plus.service.dto.ShopBonusSalaryDTO;
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
public class ShopBonusSalaryService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopBonusSalaryService.class);

    private final ShopBonusSalaryRepository shopBonusSalaryRepository;

    public ShopBonusSalary create(Long shopId, ShopBonusSalaryDTO shopBonusSalaryDTO) {
        ShopBonusSalary shopPenalty = new ShopBonusSalary();
        shopPenalty.setShopId(shopId);
        shopPenalty.setNameKo(shopBonusSalaryDTO.getNameKo());
        shopPenalty.setNameEn(shopBonusSalaryDTO.getNameEn());
        shopPenalty.setType(BonusType.fromValue(shopBonusSalaryDTO.getType()));
        shopPenalty.setPrice(shopBonusSalaryDTO.getPrice());
        shopPenalty.created(shopBonusSalaryDTO.getLogin());
        return shopBonusSalaryRepository.save(shopPenalty);
    }

    public Optional<ShopBonusSalary> update(Long shopId, ShopBonusSalaryDTO shopBonusSalaryDTO) {
        return Optional.of(get(shopId, shopBonusSalaryDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(it -> {
                it.setShopId(shopId);
                it.setNameKo(shopBonusSalaryDTO.getNameKo());
                it.setNameEn(shopBonusSalaryDTO.getNameEn());
                it.setType(BonusType.fromValue(shopBonusSalaryDTO.getType()));
                it.setPrice(shopBonusSalaryDTO.getPrice());
                it.setActivated(shopBonusSalaryDTO.isActivated());
                it.updateLastModified(shopBonusSalaryDTO.getLogin());
                return shopBonusSalaryRepository.save(it);
            });
    }

    public void delete(Long shopId, long salesItemId) {
        get(shopId, salesItemId).ifPresent(shopBonusSalaryRepository::delete);
    }

    public void updateActivated(Long shopId, long salesItemId, String login) {
        get(shopId, salesItemId).ifPresent(it -> {
            it.setActivated(false);
            it.updateLastModified(login);
            shopBonusSalaryRepository.save(it);
        });
    }

    @Transactional(readOnly = true)
    public List<ShopBonusSalary> getAllBonusSalaries(Long shopId) {
        return shopBonusSalaryRepository.findAllByShopIdAndActivated(shopId, true);
    }

    @Transactional(readOnly = true)
    public Page<ShopBonusSalary> getAllBonusSalaries(Long shopId, Pageable pageable) {
        return shopBonusSalaryRepository.findAllByShopId(shopId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<ShopBonusSalary> get(Long shopId, long shopBonusSalaryId) {
        return shopBonusSalaryRepository.findByIdAndShopIdAndActivated(shopBonusSalaryId, shopId, true);
    }
}
