package com.yevgen.companybackend.jpa;

import com.yevgen.companybackend.model.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    Collection<Company> findByCompanyName(String companyName);
}