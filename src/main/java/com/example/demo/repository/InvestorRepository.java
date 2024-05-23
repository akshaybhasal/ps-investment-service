package com.example.demo.repository;

import com.example.demo.entity.InvestorEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestorRepository extends JpaRepository<InvestorEntity, Long> {

    @EntityGraph(attributePaths = {"fundEntities", "fundEntities.holdingEntities"})
    @Query("SELECT i FROM InvestorEntity i WHERE i.externalInvestorId = :externalInvestorId")
    Optional<InvestorEntity> findByExternalInvestorIdWithAssociatedFundsAndHoldings(@Param("externalInvestorId") String externalInvestorId);
}
