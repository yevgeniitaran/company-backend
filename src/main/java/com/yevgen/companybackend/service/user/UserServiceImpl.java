package com.yevgen.companybackend.service.user;

import com.yevgen.companybackend.utils.exceptions.ResourceNotFoundException;
import com.yevgen.companybackend.utils.exceptions.UserAlreadyExistsException;
import com.yevgen.companybackend.jpa.CompanyRepository;
import com.yevgen.companybackend.jpa.UserRepository;
import com.yevgen.companybackend.model.Company;
import com.yevgen.companybackend.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User " + user.getUsername() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new ResourceNotFoundException("User with id " + user.getId() + " doesn't exist and can't be updated");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateCompanyById(Long userId, Long companyId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " doesn't exist and can't be updated"));
        Optional<Company> companyOptional = companyRepository.findById(companyId);
        Company company = companyOptional.orElseThrow(() -> new ResourceNotFoundException("Company with id " + companyId + " doesn't exist and can't be updated"));
        user.setCompany(company);
        return updateUser(user);
    }
}
