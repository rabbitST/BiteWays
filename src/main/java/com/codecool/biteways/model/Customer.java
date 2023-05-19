package com.codecool.biteways.model;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String country;
    private String postalCode;
    private String city;
    private String address;
    private String cellPhone;
    @OneToMany(mappedBy = "customer")
    private List<Order> orderList;
}
