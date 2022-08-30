package com.devsuperior.dscatalog.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

@DataJpaTest
public class ProductRepositoriesTests {

    private long existingId;
    private long nonExistingId;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 1000L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);
        var result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }
}
