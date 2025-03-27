package com.salary.plus.service;

import com.salary.plus.domain.Authority;
import com.salary.plus.enums.RoleType;
import com.salary.plus.repository.AuthorityRepository;
import com.salary.plus.repository.ShopTableRepository;
import com.salary.plus.service.dto.ShopTableResponse;
import java.util.List;
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
public class ShopAuthorityService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopAuthorityService.class);

    private final AuthorityRepository authorityRepository;

    public List<Authority> findAllExceptAdmin() {
        return authorityRepository.findAll().stream().filter(it -> RoleType.ROLE_ADMIN != RoleType.fromValue(it.getName())).toList();
    }
}
