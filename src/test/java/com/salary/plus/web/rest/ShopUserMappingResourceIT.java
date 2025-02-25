package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.repository.ShopUserMappingRepository;
import com.salary.plus.repository.UserRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.service.dto.ShopSalesItemDTO;
import com.salary.plus.service.dto.UserDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class ShopUserMappingResourceIT {

    private static final String TARGET_URL = "/api/shops";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopUserMappingRepository shopUserMappingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    @Nested
    class WhenSuccess {

        @BeforeEach
        void setUp() {
            Shop shop = new Shop();
            shop.setType(ShopType.BAR);
            shop.setNameKo("shop1 ko");
            shop.setNameEn("shop1 en");
            shopRepository.saveAndFlush(shop);

            final User user = userRepository.findOneByLogin("user").orElseThrow();

            ShopUserMapping shopUserMapping = new ShopUserMapping();
            shopUserMapping.setShopId(shop.getId());
            shopUserMapping.setUserId(user.getId());
            shopUserMappingRepository.saveAndFlush(shopUserMapping);
        }

        @Nested
        class WhenAllShops {

            @Test
            @Transactional
            void getAllShop() throws Exception {
                var returnedUserDTO = om.readValue(
                    restUserMockMvc
                        .perform(get(TARGET_URL).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                    List.class
                );

                assertThat(returnedUserDTO.size()).isEqualTo(1);
            }
        }
    }
}
