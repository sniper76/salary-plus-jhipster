package com.salary.plus.web.rest;

import static com.salary.plus.web.rest.TestUtil.someEmail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomThings.someThing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.service.dto.AdminShopDTO;
import com.salary.plus.service.dto.AdminUserDTO;
import java.util.Collections;
import java.util.List;
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

    @Test
    @Transactional
    void createShop() throws Exception {
        // Create the User
        AdminShopDTO userDTO = new AdminShopDTO();
        userDTO.setName("shop1");

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
        assertThat(returnedUserDTO.getName()).isEqualTo(userDTO.getName());
    }

    @Test
    @Transactional
    void createShopWithUsers() throws Exception {
        // Create the User
        AdminShopDTO userDTO = new AdminShopDTO();
        userDTO.setName("shop1");
        userDTO.setUsers(getUsers());

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
        assertThat(returnedUserDTO.getName()).isEqualTo(userDTO.getName());
    }

    private List<AdminUserDTO> getUsers() {
        // Create the User
        final AdminUserDTO userDTO1 = getAdminUserDTO();
        final AdminUserDTO userDTO2 = getAdminUserDTO();

        return List.of(userDTO1, userDTO2);
    }

    private AdminUserDTO getAdminUserDTO() {
        AdminUserDTO userDTO1 = new AdminUserDTO();
        userDTO1.setLogin(someAlphanumericString(10));
        userDTO1.setFirstName(someAlphanumericString(10));
        userDTO1.setLastName(someAlphanumericString(10));
        userDTO1.setEmail(someEmail());
        userDTO1.setActivated(true);
        userDTO1.setLangKey(someThing("en", "ko"));
        userDTO1.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));
        return userDTO1;
    }
}
