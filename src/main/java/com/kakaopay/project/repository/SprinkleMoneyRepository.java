package com.kakaopay.project.repository;


import com.kakaopay.project.entity.SprinkleMoneyEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories(basePackages = {"eventdb"})
@EntityScan(basePackages = {"eventdb"})
@Configuration
public interface SprinkleMoneyRepository extends CrudRepository<SprinkleMoneyEntity, Long> {

    @Query(value = "select * from SPRINKLE_MONEY s where s.TOKEN= :token and s.ROOM_ID= :roomId", nativeQuery = true)
    SprinkleMoneyEntity findByTokenAndRoomId(@Param("token") String token,@Param("roomId") long roomId);



}