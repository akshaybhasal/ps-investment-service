package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/***
 * Represents an entity to save specific holing
 * The externalHoldingId identifier is created to allow access to the specific holding without exposing
 * internal primary key
 */
@Entity
@Table(name = "holdings")
@Getter
@Setter
public class HoldingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int marketValue;

    private int quantity;

    private String externalHoldingId = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "fund_id")
    private FundEntity fundEntity;
}

