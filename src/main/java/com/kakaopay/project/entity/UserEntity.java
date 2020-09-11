package com.kakaopay.project.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="USER")
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private long id;
    private long payMoney;

    @ManyToOne(targetEntity = RoomEntity.class)
    @JoinColumn(name = "ROOM_ID")
    private RoomEntity roomEntity;
}
