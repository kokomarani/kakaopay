package com.kakaopay.project.repository;

import com.kakaopay.project.entity.UserEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories(basePackages = {"eventdb"})
@EntityScan(basePackages = {"eventdb"})
@Configuration
public interface UserRepository extends CrudRepository<UserEntity, Long> {
//    @Query(value = "select u from UserEntity u where u.userRoomEntity=?1 ORDER BY RAND() LIMIT 3", nativeQuery = true)
//    List<UserEntity> findByRoomId(long roomId, int limitCount);

}