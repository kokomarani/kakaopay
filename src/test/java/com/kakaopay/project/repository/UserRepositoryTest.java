package com.kakaopay.project.repository;

import com.kakaopay.project.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPayMoney(5000);
        userRepository.save(userEntity);

        Iterable<UserEntity> userEntities = userRepository.findAll();
        userEntities.forEach(userEntity1 -> System.out.println(userEntity1.toString()));
    }
}