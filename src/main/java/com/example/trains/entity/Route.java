package com.example.trains.entity;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
public class Route {
    @EmbeddedId
    private RouteId id;

    private Integer _order;
}
