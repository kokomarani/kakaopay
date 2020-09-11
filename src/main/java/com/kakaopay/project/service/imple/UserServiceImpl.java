package com.kakaopay.project.service.imple;

import com.kakaopay.project.entity.UserEntity;
import com.kakaopay.project.repository.UserRepository;
import com.kakaopay.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(UserEntity userEntity){
        userRepository.save(userEntity);
        return;
    }
}
