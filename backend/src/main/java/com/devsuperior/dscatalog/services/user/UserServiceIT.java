package com.devsuperior.dscatalog.services.user;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserServiceIT {

    Page<UserDTO> findAllPaged(Pageable pageable);

    UserDTO findById(Long id);

    UserDTO insert(UserInsertDTO dto);

    UserDTO update(Long id, UserDTO dto);

    void delete(Long id);
}
