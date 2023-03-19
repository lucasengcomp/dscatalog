package com.devsuperior.dscatalog.services.product;

import com.devsuperior.dscatalog.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductServiceIT {

    Page<ProductDTO> findAllPaged(Pageable pageable);

    ProductDTO findById(Long id);

    ProductDTO insert(ProductDTO dto);

    ProductDTO update(Long id, ProductDTO dto);

    void delete(Long id);
}
