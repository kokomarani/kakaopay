package com.kakaopay.project.repository;

import com.kakaopay.project.entity.RoomEntity;
import com.kakaopay.project.entity.UserEntity;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Test
    void saveUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPayMoney(0);

        RoomEntity randomRoom = roomRepository.findById(RandomUtils.nextInt(1,3));
        userEntity.setRoomEntity(randomRoom);
        userRepository.save(userEntity);


    }
}