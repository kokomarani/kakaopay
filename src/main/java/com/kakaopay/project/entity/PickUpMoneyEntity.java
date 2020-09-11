package com.kakaopay.project.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="PICKUP_MONEY")
public class PickUpMoneyEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="PICKUP_ID")
    private long id;
    private long payMoneyAmount;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;
}
