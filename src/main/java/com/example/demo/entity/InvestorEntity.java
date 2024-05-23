package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/***
 * Represents an entity to save investor along with associated funds and holdings
 * The externalInvestorId identifier is created to allow access to the specific investor without exposing
 * internal primary key
 */
@Entity
@Table(name = "investors")
@Getter
@Setter
public class InvestorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String externalInvestorId = UUID.randomUUID().toString();

    @OneToMany(mappedBy = "investorEntity")
    private Set<FundEntity> fundEntities;
}
