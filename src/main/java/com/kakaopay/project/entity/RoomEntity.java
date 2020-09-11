package com.kakaopay.project.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="ROOM")
public class RoomEntity {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ROOM_ID")
    @Id
    private long id; // pk
}
