package com.yevgen.companybackend.service.company;

import com.yevgen.companybackend.jpa.CompanyRepository;
import com.yevgen.companybackend.model.Company;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    private final String COMPANY_NAME = "company";
    private final String NEW_COMPANY_NAME = "my company";

    @Test
    public void updateCompanyGivesCompanyNewName() {
        Company company = companyRepository.findByCompanyName(COMPANY_NAME).iterator().next();
        company.setCompanyName(NEW_COMPANY_NAME);
        companyService.updateCompany(company);
        Company updatedCompany = companyRepository.findByCompanyName(NEW_COMPANY_NAME).iterator().next();
        Assert.assertEquals(NEW_COMPANY_NAME, updatedCompany.getCompanyName());
    }
}