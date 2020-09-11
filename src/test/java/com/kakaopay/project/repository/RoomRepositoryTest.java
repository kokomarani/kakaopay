package com.kakaopay.project.repository;

import com.kakaopay.project.entity.RoomEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RoomRepositoryTest {
    @Autowired
    private RoomRepository roomRepository;

    @Test
    void saveRoom() {
        RoomEntity roomEntity = new RoomEntity();
        roomRepository.save(roomEntity);
    }
}