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
    void saveAndFindRoom() {
        RoomEntity roomEntity = new RoomEntity();
        roomRepository.save(roomEntity);

        Iterable<RoomEntity> roomEntities = roomRepository.findAll();
        roomEntities.forEach(roomEntity1 -> System.out.println(roomEntity1.toString()));
    }
}