package com.yevgen.companybackend.service.user;

import com.yevgen.companybackend.model.User;

public interface UserService {

    User registerNewUser(User user);

    User updateUser(User user);

    User updateCompanyById(Long userId, Long companyId);
}
