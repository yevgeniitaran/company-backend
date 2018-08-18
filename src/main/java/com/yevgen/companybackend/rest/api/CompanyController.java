package com.yevgen.companybackend.rest.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.yevgen.companybackend.utils.jackson.Views;
import com.yevgen.companybackend.jpa.CompanyRepository;
import com.yevgen.companybackend.model.Company;
import com.yevgen.companybackend.service.company.CompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/v1/companies")
public class CompanyController {

    private CompanyRepository companyRepository;
    private CompanyService companyService;

    public CompanyController(CompanyRepository companyRepository, CompanyService companyService) {
        this.companyRepository = companyRepository;
        this.companyService = companyService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.CompanyView.class)
    public Collection<Company> getCompanies(@RequestParam(value = "name", required = false) String name) {
         return StringUtils.isNotBlank(name) ? companyRepository.findByCompanyName(name)
                : StreamSupport.stream(companyRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.CompanyView.class)
    public Company getCompanyById(@PathVariable("id") Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.CompanyView.class)
    ResponseEntity<Company> addCompany(@RequestBody Company company) {
        if (company.getId() != null && companyRepository.existsById(company.getId())) {
            return ResponseEntity.ok(companyService.updateCompany(company));
        }
        Company result = companyRepository.save(company);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }
}