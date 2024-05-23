package com.example.demo.controller;

import com.example.demo.api.request.InvestorRequest;
import com.example.demo.api.response.InvestorResponse;
import com.example.demo.api.response.MarketValue;
import com.example.demo.service.PSInvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;

@Tag(name = "PS Investor APIs", description = "PS Investor APIs")
@Log4j2
@RestController
@RequestMapping("/api/v1/investments")
public class InvestorController {

    private final PSInvestmentService investmentService;

    public InvestorController(PSInvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping("/investors")
    @Operation(summary = "Add Investor", description = "This endpoint allows you to add a new investor to the system. You can also associate funds and holdings to the investor")
    public ResponseEntity<InvestorResponse> addInvestor(@RequestBody InvestorRequest investorRequest) {
        InvestorResponse investorResponse = investmentService.addInvestor(investorRequest);
        log.info("Successfully added investor along with associated funds and respective holdings in DB for externalInvestorId={}", investorResponse.getExternalInvestorId());
        return new ResponseEntity<>(investorResponse, HttpStatus.CREATED);
    }

    @GetMapping("/investors/{externalInvestorId}/market-value")
    @Operation(summary = "Retrieve Investor Market Value", description = "This endpoint retrieves the total market value of the funds invested by an investor")
    public ResponseEntity<MarketValue> evaluateTotalMarketValueByInvestor(@PathVariable String externalInvestorId, @RequestParam(required = false) Set<String> excludeHoldings) {
        log.info("Attempting to retrieve total market value for investor including all fund holdings for externalInvestorId={}", externalInvestorId);
        MarketValue marketValue = investmentService.evaluateTotalMarketValueByInvestor(externalInvestorId, excludeHoldings == null ? Collections.emptySet() : excludeHoldings);
        log.info("Successfully retrieved total market value for investor including all fund holdings for externalInvestorId={}", externalInvestorId);
        return new ResponseEntity<>(marketValue, HttpStatus.OK);
    }
}
