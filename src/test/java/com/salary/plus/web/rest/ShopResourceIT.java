package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.service.dto.AdminShopDTO;
import java.util.List;
import java.util.function.Consumer;
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
class ShopResourceIT {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private ShopRepository shopRepository;

    @Nested
    class WhenSuccess {

        @Nested
        class WhenCreateOnlyShop {

            @Test
            @Transactional
            void createShop() throws Exception {
                // Create the User
                AdminShopDTO userDTO = new AdminShopDTO();
                userDTO.setType("BAR");
                userDTO.setNameKo("shop1");
                userDTO.setNameEn("shop1");

                var returnedUserDTO = om.readValue(
                    restUserMockMvc
                        .perform(post("/api/admin/shops").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                    AdminShopDTO.class
                );

                // Validate the returned User
                assertThat(returnedUserDTO.getNameKo()).isEqualTo(userDTO.getNameKo());
            }
        }

        @Nested
        class WhenCreateShopWithUsers {

            @Test
            @Transactional
            void createShopWithUsers() throws Exception {
                // Create the User
                AdminShopDTO userDTO = new AdminShopDTO();
                userDTO.setType("BAR");
                userDTO.setNameKo("shop1");
                userDTO.setNameEn("shop1");

                var returnedUserDTO = om.readValue(
                    restUserMockMvc
                        .perform(post("/api/admin/shops").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                    Shop.class
                );

                // Validate the returned User
                assertThat(returnedUserDTO.getNameKo()).isEqualTo(userDTO.getNameKo());
            }
        }

        @Nested
        @Transactional
        class WhenUpdateOnlyShop {

            private Shop shop;

            @BeforeEach
            void setUp() {
                shop = new Shop();
                shop.setNameKo("shop1Ko");
                shop.setNameEn("shop1En");
                shop.setType(ShopType.BAR);

                shopRepository.save(shop);
            }

            @Test
            void updateShop() throws Exception {
                // Create the User
                AdminShopDTO userDTO = new AdminShopDTO();
                userDTO.setId(shop.getId());
                userDTO.setType("BAR");
                userDTO.setNameKo("shop1");
                userDTO.setNameEn("shop2");

                int databaseSizeBeforeUpdate = shopRepository.findAll().size();

                restUserMockMvc
                    .perform(put("/api/admin/shops").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
                    .andExpect(status().isOk());

                assertPersistedUsers(shops -> {
                    assertThat(shops).hasSize(databaseSizeBeforeUpdate);
                    Shop testShop = shops.stream().filter(s -> s.getId().equals(userDTO.getId())).findFirst().orElseThrow();
                    assertThat(testShop.getNameKo()).isEqualTo(userDTO.getNameKo());
                    assertThat(testShop.getNameEn()).isEqualTo(userDTO.getNameEn());
                    assertThat(testShop.getType().name()).isEqualTo(userDTO.getType());
                });
            }
        }

        @Nested
        class WhenGetAllShops {

            @BeforeEach
            void setUp() {
                Shop shop1 = new Shop();
                shop1.setNameKo("shop1Ko");
                shop1.setNameEn("shop1En");
                shop1.setType(ShopType.BAR);

                shopRepository.save(shop1);
            }

            @Test
            @Transactional
            void getAllShops() throws Exception {
                // Get all the users
                restUserMockMvc
                    .perform(get("/api/admin/shops?sort=id,desc").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.[*].nameKo").value(hasItem("shop1Ko")))
                    .andExpect(jsonPath("$.[*].nameEn").value(hasItem("shop1En")))
                    .andExpect(jsonPath("$.[*].type").value(hasItem("BAR")));
            }
        }
    }

    private void assertPersistedUsers(Consumer<List<Shop>> shopAssertion) {
        shopAssertion.accept(shopRepository.findAll());
    }
}
