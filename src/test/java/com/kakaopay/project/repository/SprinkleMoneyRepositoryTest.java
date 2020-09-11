package com.kakaopay.project.repository;

import com.kakaopay.project.entity.SprinkleMoneyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SprinkleMoneyRepositoryTest {
    @Autowired
    private SprinkleMoneyRepository sprinkleMoneyRepository;
    @Test
    void findByTokenAndRoomId() {
        SprinkleMoneyEntity sprinkleMoneyEntity = sprinkleMoneyRepository.findByTokenAndRoomId("xrl", 1);
        if (sprinkleMoneyEntity == null){
            System.out.println("없어용");
        }else{
            System.out.println(sprinkleMoneyEntity.getUserId());
        }
    }
}