package com.salary.plus.service;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.service.dto.ShopBaseSalaryDTO;
import java.util.List;
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
}
