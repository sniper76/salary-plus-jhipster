package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopUserMappingRepository;
import com.salary.plus.repository.UserRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.service.dto.ShopDailySalaryDTO;
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
class ShopDailySalaryResourceIT {

    private static final String TARGET_URL = "/api/shops/{shopId}/daily-salaries";

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

        private Long shopId;

        @BeforeEach
        void setUp() {
            Shop shop = new Shop();
            shop.setType(ShopType.BAR);
            shop.setNameKo("shop1");
            shop.setNameEn("shop1");
            shopRepository.saveAndFlush(shop);

            shopId = shop.getId();

            final User user = userRepository.findOneByLogin("user").orElseThrow();

            ShopUserMapping shopUserMapping = new ShopUserMapping();
            shopUserMapping.setShopId(shopId);
            shopUserMapping.setUserId(user.getId());
            shopUserMappingRepository.saveAndFlush(shopUserMapping);
        }

        @Nested
        class WhenHasItemsShopDailySalary {

            @Test
            @Transactional
            void createShopDailySalary() throws Exception {
                // Create the User
                ShopDailySalaryDTO userDTO = new ShopDailySalaryDTO();
                userDTO.setShopId(shopId);
                userDTO.setDate("2025-02-25");
                userDTO.setUserIds(List.of(1L, 2L));

                var returnedUserDTO = om.readValue(
                    restUserMockMvc
                        .perform(post(TARGET_URL, shopId).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                    UserDTO.class
                );

                // Validate the returned User
                assertThat(returnedUserDTO.getSalaries().size()).isEqualTo(userDTO.getUserIds().size());
            }
        }

        @Nested
        class WhenAllShopDailySalary {}
    }
}
