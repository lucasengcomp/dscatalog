package com.devsuperior.dscatalog.resources;


import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.services.product.ProductServiceImpl;
import com.devsuperior.dscatalog.services.Utils.exceptions.DataBaeException;
import com.devsuperior.dscatalog.services.Utils.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductResource.class)
public class ProductResourcesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    private Long existingId;

    private Long nonExistingId;

    private Long dependentId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DataBaeException.class).when(service).delete(dependentId);
    }

    @Test
    public void deleteShouldReturnVoidWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNoContent());
        result.andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        objectsProductDTOJsonPath(result);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        objectsProductDTOJsonPath(result);
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    private static void objectsProductDTOJsonPath(ResultActions result) throws Exception {
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.imgUrl").exists());
        result.andExpect(jsonPath("$.date").exists());
    }
}
