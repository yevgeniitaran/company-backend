package com.yevgen.companybackend.service.company;

import com.yevgen.companybackend.utils.exceptions.ResourceNotFoundException;
import com.yevgen.companybackend.jpa.CompanyRepository;
import com.yevgen.companybackend.model.Company;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company updateCompany(Company company) {
        Company oldCompany = companyRepository.findById(company.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Company with id " + company.getId() + " doesn't exist and can't be updated"));
        company.setUsers(oldCompany.getUsers());
        return companyRepository.save(company);
    }
}
