package com.salary.plus.service;

import com.salary.plus.domain.ShopUserToUserMapping;
import com.salary.plus.repository.ShopUserToUserMappingRepository;
import com.salary.plus.service.dto.ModelMappingDTO;
import com.salary.plus.utils.ListComparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ShopModelService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopModelService.class);

    private final ShopUserToUserMappingRepository shopUserToUserMappingRepository;
    private final ListComparator listComparator;

    public void updateMamaMappings(String login, ModelMappingDTO modelDTO) {
        final List<Long> originList = shopUserToUserMappingRepository.findAllTargetUserIdByShopIdAndUserId(
            modelDTO.getShopId(),
            modelDTO.getUserId()
        );
        final Map<String, List<Long>> compareLists = listComparator.compareLists(originList, modelDTO.getModelUserIds());

        final List<Long> insert = compareLists.get("insert");
        insert.forEach(it -> {
            ShopUserToUserMapping mapping = new ShopUserToUserMapping();
            mapping.setShopId(modelDTO.getShopId());
            mapping.setUserId(modelDTO.getUserId());
            mapping.setTargetUserId(it);
            mapping.created(login);
            shopUserToUserMappingRepository.save(mapping);
        });
        final List<Long> delete = compareLists.get("delete");
        delete.forEach(it -> {
            shopUserToUserMappingRepository.deleteByShopIdAndTargetUserId(modelDTO.getShopId(), it);
        });
    }
}
