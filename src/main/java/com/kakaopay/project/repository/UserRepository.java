package com.kakaopay.project.repository;

import com.kakaopay.project.entity.UserEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories(basePackages = {"eventdb"})
@EntityScan(basePackages = {"eventdb"})
@Configuration
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    List<UserEntity> findTop20By();
}