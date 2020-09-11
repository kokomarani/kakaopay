package com.kakaopay.project.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="SPRINKLE_MONEY")
public class SprinkleMoneyEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SPRINKLE_ID")
    private long id;
    @Column(name = "token", unique=true, nullable=false)
    private String token;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private long payMoneyAmount;
    private long userId; //뿌린 친구

    @JsonProperty("room")
    @ManyToOne(targetEntity = RoomEntity.class)
    @JoinColumn(name = "ROOM_ID")
    private RoomEntity roomEntity;

    @JsonProperty("pickUpMoney")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "SPRINKLE_ID")
    private List<PickUpMoneyEntity> pickUpMoneyEntities;
}
