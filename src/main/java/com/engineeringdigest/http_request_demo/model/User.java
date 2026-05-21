package com.engineeringdigest.http_request_demo.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    private int age;

}
