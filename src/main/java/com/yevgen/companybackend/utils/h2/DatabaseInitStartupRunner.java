package com.yevgen.companybackend.utils.h2;

import com.yevgen.companybackend.jpa.CompanyRepository;
import com.yevgen.companybackend.jpa.RoleRepository;
import com.yevgen.companybackend.jpa.UserRepository;
import com.yevgen.companybackend.model.Company;
import com.yevgen.companybackend.model.Role;
import com.yevgen.companybackend.model.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DatabaseInitStartupRunner implements ApplicationRunner {

    private CompanyRepository companyRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public DatabaseInitStartupRunner(CompanyRepository companyRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Company company = new Company("company");
        companyRepository.save(company);

        Role admin_role = new Role("ROLE_ADMIN");
        roleRepository.save(admin_role);
        Role user_role = new Role("ROLE_USER");
        roleRepository.save(user_role);

        User admin = new User("admin", "$2a$10$R2MB1s1bZ8AVLQ9ROe7GauyvEdZjx83EVtPnFTU7hUTVoikS5UeMS", company);
        admin.setRoles(Collections.singleton(admin_role));
        userRepository.save(admin);
        User user = new User("user", "$2a$10$mJfgeospQJhzHnq7ci7QluJwJFJHfZmN.XH0I1oIkis.fkDnT6Tv.", company);
        user.setRoles(Collections.singleton(user_role));
        userRepository.save(user);
    }
}