package com.devsuperior.dscatalog.factory;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        var product = new Product(1L, "Phone", "Indestructible phone", 800.0, "https://www.techtudo.com.br/listas/2017/05/do-tijolao-3310-ao-lumia-relembre-celulares-mais-marcantes-da-nokia.ghtml", Instant.parse("2020-07-14T10:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        var product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
