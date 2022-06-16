package com.example.trains.entity;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    public Role() {
    }

    public Role(long id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
