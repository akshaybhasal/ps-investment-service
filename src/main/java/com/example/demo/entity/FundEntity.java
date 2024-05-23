package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/***
 * Represents an entity to save fund along with associated holdings
 * The externalFundId identifier is created to allow access to the specific fund without exposing
 * internal primary key
 */
@Entity
@Table(name = "funds")
@Getter
@Setter
public class FundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String externalFundId = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "investor_id")
    private InvestorEntity investorEntity;

    @OneToMany(mappedBy = "fundEntity")
    private Set<HoldingEntity> holdingEntities;
}
