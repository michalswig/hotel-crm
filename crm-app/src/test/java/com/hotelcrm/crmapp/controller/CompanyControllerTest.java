package com.hotelcrm.crmapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelcrm.crmapp.dto.CompanyRequest;
import com.hotelcrm.crmapp.enums.Industry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin1", roles = {"ADMINISTRATOR"})
    @Test
    void shouldCreateCompany() throws Exception {
        CompanyRequest request = CompanyRequest.builder()
                .name("Test Company")
                .taxId("1234567890")
                .industry(Industry.CORPORATE)
                .email("test@example.com")
                .phoneNumber("+48123456789")
                .website("https://test.com")
                .address("Main Street 1")
                .postalCode("00-001")
                .city("Warsaw")
                .country("Poland")
//                .createdByUserId(1L)
                .build();

        mockMvc.perform(post("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Company"));
    }

    @WithMockUser(username = "admin1", roles = {"ADMINISTRATOR"})
    @Test
    void shouldReturnPaginatedCompanies() throws Exception {
        mockMvc.perform(get("/api/v1/companies?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @WithMockUser(username = "admin1", roles = {"ADMINISTRATOR"})
    @Test
    void shouldReturnCompanyByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/companies/999"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin1", roles = {"ADMINISTRATOR"})
    @Test
    void shouldFilterCompanies() throws Exception {
        mockMvc.perform(get("/api/v1/companies/filter")
                        .param("name", "Test")
                        .param("city", "Warsaw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}