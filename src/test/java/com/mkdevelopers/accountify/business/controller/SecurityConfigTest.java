package com.mkdevelopers.accountify.business.controller;

import com.mkdevelopers.accountify.business.service.BusinessService;
import com.mkdevelopers.accountify.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusinessController.class)
@Import({SecurityConfig.class, SecurityConfigTest.TestJwtDecoderConfig.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BusinessService businessService;

    @Test
    void unauthenticatedRequestDoesNotReachBusinessService() throws Exception {
        mockMvc.perform(get("/businesses"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(businessService);
    }

    @TestConfiguration
    static class TestJwtDecoderConfig {

        @Bean
        JwtDecoder jwtDecoder() {
            return token -> Jwt.withTokenValue(token)
                    .header("alg", "none")
                    .claim("sub", "test-user")
                    .build();
        }
    }
}
