package com.devsuperior.dscatalog.services.category;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryServiceIT {
    Page<CategoryDTO> findAllPaged(Pageable pageable);

    CategoryDTO findById(Long id);

    CategoryDTO insert(CategoryDTO dto);

    CategoryDTO update(Long id, CategoryDTO dto);

    void delete(Long id);
}
