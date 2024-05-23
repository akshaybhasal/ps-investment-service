package com.example.demo.service;


import com.example.demo.api.request.FundRequest;
import com.example.demo.api.request.InvestorRequest;
import com.example.demo.api.response.FundResponse;
import com.example.demo.api.response.InvestorResponse;
import com.example.demo.api.response.MarketValue;
import com.example.demo.entity.FundEntity;
import com.example.demo.entity.HoldingEntity;
import com.example.demo.entity.InvestorEntity;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.mapper.InvestmentMapper;
import com.example.demo.repository.FundRepository;
import com.example.demo.repository.HoldingRepository;
import com.example.demo.repository.InvestorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PSInvestmentService {

    private final InvestmentMapper investmentMapper;
    private final InvestorRepository investorRepository;
    private final FundRepository fundRepository;
    private final HoldingRepository holdingRepository;

    public PSInvestmentService(InvestmentMapper investmentMapper, InvestorRepository investorRepository, FundRepository fundRepository, HoldingRepository holdingRepository) {
        this.investmentMapper = investmentMapper;
        this.investorRepository = investorRepository;
        this.fundRepository = fundRepository;
        this.holdingRepository = holdingRepository;
    }

    /***
     * Adds an investor along with associated funds and holdings
     * Rollbacks the transaction in case of an error
     */
    @Transactional(rollbackFor = Exception.class)
    public InvestorResponse addInvestor(InvestorRequest investorRequest) {
        InvestorEntity investorToSave = investmentMapper.toInvestorEntity(investorRequest);
        InvestorEntity savedInvestor = investorRepository.save(investorToSave);
        InvestorResponse investorResponse = investmentMapper.toInvestorResponse(savedInvestor);

        List<FundResponse> fundsToReturn = new ArrayList<>();

        for (FundRequest fundRequest : investorRequest.getFunds()) {
            FundEntity fundToSave = investmentMapper.toFundEntity(fundRequest);
            fundToSave.setInvestorEntity(investorToSave);
            FundEntity savedFund = fundRepository.save(fundToSave);
            FundResponse fundResponse = investmentMapper.toFundResponse(savedFund);
            fundsToReturn.add(fundResponse);

            Set<HoldingEntity> holdingEntities = fundRequest.getHoldings()
                    .stream()
                    .map(holdingRequest -> {
                        HoldingEntity holdingToSave = investmentMapper.toHoldingEntity(holdingRequest);
                        holdingToSave.setFundEntity(fundToSave);
                        return holdingToSave;
                    })
                    .collect(Collectors.toSet());

            holdingRepository.saveAll(holdingEntities);
        }
        investorResponse.setFunds(fundsToReturn);
        return investorResponse;
    }


    /***
     * Evaluates the total market value for a specific fund
     * @throws RecordNotFoundException if investor cannot be found
     */
    public MarketValue evaluateTotalMarketValueByFund(String externalFundId, Set<String> excludeHoldings) {
        return fundRepository.findByExternalFundIdWithAssociatedHoldings(externalFundId)
                .map(FundEntity::getHoldingEntities)
                .map(holdings -> holdings.stream()
                        .filter(holding -> excludeHoldings.isEmpty() || !excludeHoldings.contains(holding.getName()))
                        .mapToInt(holding -> holding.getMarketValue() * holding.getQuantity())
                        .sum())
                .map(MarketValue::new)
                .orElseThrow(() -> {
                    log.error("No record found for externalFundId={}", externalFundId);
                    return new RecordNotFoundException("Fund cannot be found");
                });
    }

    /***
     * Evaluates the total market value for all the funds associated to an investor
     * @throws RecordNotFoundException if investor cannot be found
     */
    public MarketValue evaluateTotalMarketValueByInvestor(String externalInvestorId, Set<String> excludeHoldings) {
        return investorRepository.findByExternalInvestorIdWithAssociatedFundsAndHoldings(externalInvestorId)
                .map(InvestorEntity::getFundEntities)
                .map(funds -> funds.stream()
                        .flatMap(fund -> fund.getHoldingEntities()
                                .stream())
                        .filter(holding -> excludeHoldings.isEmpty() || !excludeHoldings.contains(holding.getName()))
                        .mapToInt(holding -> holding.getMarketValue() * holding.getQuantity())
                        .sum())
                .map(MarketValue::new)
                .orElseThrow(() -> {
                    log.error("No record found for externalInvestorId={}", externalInvestorId);
                    return new RecordNotFoundException("Investor cannot be found");
                });
    }
}
