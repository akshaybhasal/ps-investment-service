package com.example.demo.repository;

import com.example.demo.entity.FundEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundRepository extends JpaRepository<FundEntity, Long> {

    @EntityGraph(attributePaths = {"holdingEntities"})
    @Query("SELECT f FROM FundEntity f WHERE f.externalFundId = :externalFundId")
    Optional<FundEntity> findByExternalFundIdWithAssociatedHoldings(@Param("externalFundId") String externalFundId);
}
