package com.example.demo.controller;

import com.example.demo.api.response.MarketValue;
import com.example.demo.service.PSInvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;

@Tag(name = "PS Fund APIs", description = "PS Fund APIs")
@Log4j2
@RestController
@RequestMapping("/api/v1/investments")
public class FundController {

    private final PSInvestmentService investmentService;

    public FundController(PSInvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @GetMapping("/funds/{externalFundId}/market-value")
    @Operation(summary = "Retrieve Fund Market Value", description = "This endpoint retrieves the total market value of a specified fund")
    public ResponseEntity<MarketValue> evaluateTotalMarketValueByFund(@PathVariable String externalFundId, @RequestParam(required = false) Set<String> excludeHoldings) {
        log.info("Attempting to retrieve total market value for fund for externalFundId={}", externalFundId);
        MarketValue marketValue = investmentService.evaluateTotalMarketValueByFund(externalFundId, excludeHoldings == null ? Collections.emptySet() : excludeHoldings);
        log.info("Successfully retrieved total market value for fund for externalFundId={}", externalFundId);
        return new ResponseEntity<>(marketValue, HttpStatus.OK);
    }
}
