package com.sparta.spartatigers.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "stadiums")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Stadium extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stadium_id")
    private Long id;

    @Column
    private String name;

    @Column
    private double latitude;

    @Column
    private double longitude;
}