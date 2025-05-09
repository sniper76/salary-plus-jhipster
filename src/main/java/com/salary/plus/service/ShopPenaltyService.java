package com.salary.plus.service;

import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.enums.PenaltyType;
import com.salary.plus.repository.ShopPenaltyRepository;
import com.salary.plus.repository.ShopUserPenaltyMappingRepository;
import com.salary.plus.repository.ShopUserSalesSalaryRepository;
import com.salary.plus.service.dto.ShopOrderDetailDTO;
import com.salary.plus.service.dto.ShopPenaltyDTO;
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
public class ShopPenaltyService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopPenaltyService.class);

    private final ShopPenaltyRepository shopPenaltyRepository;
    private final ShopUserPenaltyMappingRepository shopUserPenaltyMappingRepository;
    private final ShopUserSalesSalaryRepository shopUserSalesSalaryRepository;

    public ShopPenalty create(Long shopId, ShopPenaltyDTO shopPenaltyDTO) {
        ShopPenalty shopPenalty = new ShopPenalty();
        shopPenalty.setShopId(shopId);
        shopPenalty.setNameKo(shopPenaltyDTO.getNameKo());
        shopPenalty.setNameEn(shopPenaltyDTO.getNameEn());
        shopPenalty.setType(PenaltyType.fromValue(shopPenaltyDTO.getType()));
        shopPenalty.setTypeValue(shopPenaltyDTO.getTypeValue());
        shopPenalty.setPrice(shopPenaltyDTO.getPrice());
        shopPenalty.created(shopPenaltyDTO.getLogin());
        return shopPenaltyRepository.save(shopPenalty);
    }

    public Optional<ShopPenalty> update(Long shopId, ShopPenaltyDTO shopPenaltyDTO) {
        return Optional.of(get(shopId, shopPenaltyDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(it -> {
                it.setShopId(shopId);
                it.setNameKo(shopPenaltyDTO.getNameKo());
                it.setNameEn(shopPenaltyDTO.getNameEn());
                it.setPrice(shopPenaltyDTO.getPrice());
                it.setType(PenaltyType.fromValue(shopPenaltyDTO.getType()));
                it.setTypeValue(shopPenaltyDTO.getTypeValue());
                it.setActivated(shopPenaltyDTO.isActivated());
                it.updateLastModified(shopPenaltyDTO.getLogin());
                return shopPenaltyRepository.save(it);
            });
    }

    public void delete(Long shopId, long salesItemId) {
        get(shopId, salesItemId).ifPresent(shopPenaltyRepository::delete);
    }

    public void updateActivated(Long shopId, long salesItemId, String login) {
        get(shopId, salesItemId).ifPresent(it -> {
            it.setActivated(false);
            it.updateLastModified(login);
            shopPenaltyRepository.save(it);
        });
    }

    @Transactional(readOnly = true)
    public List<ShopPenalty> getAllActivatedPenalties(Long shopId) {
        return shopPenaltyRepository.findAllByShopIdAndActivated(shopId, true);
    }

    @Transactional(readOnly = true)
    public Optional<ShopPenalty> getByType(Long shopId, PenaltyType type) {
        return shopPenaltyRepository.findByShopIdAndType(shopId, type);
    }

    @Transactional(readOnly = true)
    public Page<ShopPenalty> getAllPenalties(Long shopId, Pageable pageable) {
        return shopPenaltyRepository.findAllByShopId(shopId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<ShopPenalty> get(Long shopId, long shopPenaltyId) {
        return shopPenaltyRepository.findByIdAndShopIdAndActivated(shopPenaltyId, shopId, true);
    }

    public List<ShopOrderDetailDTO> getAllPenaltyListByDate(Long shopId, String date, Long orderId) {
        final List<Long> userIds = shopUserSalesSalaryRepository.findByShopIdAndDateAndOrderId(shopId, date, orderId);
        return shopUserPenaltyMappingRepository.findAllByShopIdAndDate(shopId, date, userIds);
    }
}
