package com.kakaopay.project.repository;


import com.kakaopay.project.entity.PickUpMoneyEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories(basePackages = {"eventdb"})
@EntityScan(basePackages = {"eventdb"})
@Configuration
public interface PickUpMoneyRepository extends CrudRepository<PickUpMoneyEntity, Long> {

}


