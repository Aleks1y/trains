package com.example.trains.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Train {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "myid")
    @GenericGenerator(name = "myid", strategy = "com.example.trains.entity.IdGenerator")
    private Long number;

    private String category;
    @ManyToOne
    private Station station;
    @OneToMany(mappedBy = "train")
    private List<TrainDeparture> trainDepartures;
    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private List<TrainTickets> trainTickets;
    private Long routeNumber;

    public int getQuantity() {
        int quantity = 0;
        for (TrainTickets trainTicket: this.trainTickets){
            quantity += trainTicket.getCount();
        }
        return quantity;
    }
}
