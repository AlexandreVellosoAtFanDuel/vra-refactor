package com.betfair.video.api.infrastructure.in.controller;

import com.betfair.video.api.infrastructure.in.config.VideoAPIExceptionHandler;
import com.betfair.video.api.infrastructure.in.interceptor.AccessControlInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideoApiController.class)
@Import({VideoAPIExceptionHandler.class, AccessControlInterceptor.class})
class VideoApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSucceedWhenAllRequiredHeadersArePresent() throws Exception {
        mockMvc.perform(get("/VideoAPI/v1.0/retrieveUserGeolocation")
                        .header("X-Application", "test-key")
                        .header("X-UUID", "test-uuid-123")
                        .header("X-IP", "192.168.1.1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.countryCode").value("GB"))
                .andExpect(jsonPath("$.subDivisionCode").value("ENG"))
                .andExpect(jsonPath("$.dmaId").value(12345));
    }

    @Test
    void shouldThrowExceptionWhenXIPHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/VideoAPI/v1.0/retrieveUserGeolocation")
                        .header("X-Application", "test-key")
                        .header("X-UUID", "test-uuid-123"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.faultcode").value("Client"))
                .andExpect(jsonPath("$.faultstring").value("DSC-????"))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void shouldThrowExceptionWhenXApplicationHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/VideoAPI/v1.0/retrieveUserGeolocation")
                        .header("X-UUID", "test-uuid-123")
                        .header("X-IP", "192.168.1.1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.faultcode").value("Client"))
                .andExpect(jsonPath("$.faultstring").value("DSC-????"))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void shouldThrowExceptionWhenXApplicationHeaderIsInvalid() throws Exception {
        mockMvc.perform(get("/VideoAPI/v1.0/retrieveUserGeolocation")
                        .header("X-Application", "invalid-key")
                        .header("X-UUID", "test-uuid-123")
                        .header("X-IP", "192.168.1.1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.faultcode").value("Client"))
                .andExpect(jsonPath("$.faultstring").value("DSC-????"))
                .andExpect(jsonPath("$.detail").exists());
    }
}