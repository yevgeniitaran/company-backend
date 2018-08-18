package com.yevgen.companybackend.jpa;

import com.yevgen.companybackend.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}