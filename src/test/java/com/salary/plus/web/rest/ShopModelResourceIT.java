package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.User;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.UserRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.service.dto.AdminModelDTO;
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
class ShopModelResourceIT {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    @Nested
    class WhenSuccess {

        @Nested
        class WhenCreateShopModels {

            private Long shopId;

            @BeforeEach
            void setUp() {
                Shop shop = new Shop();
                shop.setType(ShopType.BAR);
                shop.setNameKo("shop1");
                shop.setNameEn("shop1");
                shop.setWorkStartTime("09:00:00");
                shopRepository.saveAndFlush(shop);

                shopId = shop.getId();
            }

            @Test
            @Transactional
            void create() throws Exception {
                final String csvData =
                    """
                    jake1,kim,ROLE_USER,NO001
                    jake2,kim,ROLE_USER,NO002
                    jake3,kim,ROLE_USER,NO003
                    """;

                // Create the User
                AdminModelDTO userDTO = new AdminModelDTO();
                userDTO.setShopId(shopId);
                userDTO.setCsvData(csvData);

                restUserMockMvc
                    .perform(
                        post("/api/shops/{shopId}/models", shopId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsBytes(userDTO))
                    )
                    .andExpect(status().isCreated());

                final List<User> users = userRepository.findAll();
                assertThat(users.size()).isEqualTo(5); //admin, user 더해서 검증
            }
        }
    }
}
