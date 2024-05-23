package com.example.demo.service;

import com.example.demo.api.response.InvestorResponse;
import com.example.demo.api.response.MarketValue;
import com.example.demo.constant.TestConstant;
import com.example.demo.entity.FundEntity;
import com.example.demo.entity.InvestorEntity;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.mapper.InvestmentMapper;
import com.example.demo.repository.FundRepository;
import com.example.demo.repository.HoldingRepository;
import com.example.demo.repository.InvestorRepository;
import com.example.demo.util.PSInvestmentServiceObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class PSInvestmentServiceTest extends Assertions {

    @InjectMocks
    private PSInvestmentService investmentService;

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private FundRepository fundRepository;

    @Mock
    private HoldingRepository holdingRepository;

    private InvestmentMapper investmentMapper;

    @BeforeEach
    void setUp() {
        investmentMapper = Mappers.getMapper(InvestmentMapper.class);
        ReflectionTestUtils.setField(investmentService, "investmentMapper", investmentMapper);
    }

    @Test
    void testAddInvestorIfSavedSuccessfully() {
        Mockito.when(investorRepository.save(Mockito.any(InvestorEntity.class)))
                .thenReturn(PSInvestmentServiceObjectUtil.getInvestorEntity());
        Mockito.when(fundRepository.save(Mockito.any(FundEntity.class)))
                .thenReturn(PSInvestmentServiceObjectUtil.getFundEntity());
        Mockito.when(holdingRepository.saveAll(Mockito.anySet()))
                .thenReturn(List.of(PSInvestmentServiceObjectUtil.getHoldingEntity()));
        InvestorResponse investorResponse = investmentService.addInvestor(PSInvestmentServiceObjectUtil.getInvestorRequest());

        assertEquals("Investor1", investorResponse.getName());
        assertEquals("9ec585d8-77c9-4e96-a40c-236f96008464", investorResponse.getExternalInvestorId());
        assertEquals("Fund1", investorResponse.getFunds().get(0).getName());
        Mockito.verify(investorRepository).save(Mockito.any(InvestorEntity.class));
        Mockito.verify(fundRepository).save(Mockito.any(FundEntity.class));
        Mockito.verify(holdingRepository).saveAll(Mockito.anySet());
    }

    @Test
    void testAddInvestorIfTransactionRollbackOnError() {
        Mockito.when(investorRepository.save(Mockito.any(InvestorEntity.class)))
                .thenReturn(PSInvestmentServiceObjectUtil.getInvestorEntity());
        Mockito.when(fundRepository.save(Mockito.any(FundEntity.class)))
                .thenThrow(new IllegalArgumentException("Input is null"));
        assertThrows(IllegalArgumentException.class, () -> investmentService.addInvestor(PSInvestmentServiceObjectUtil.getInvestorRequest()));

        Mockito.verify(investorRepository)
                .save(Mockito.any(InvestorEntity.class));
        Mockito.verify(fundRepository)
                .save(Mockito.any(FundEntity.class));
        Mockito.verifyNoInteractions(holdingRepository);
    }

    @Test
    void testEvaluateTotalMarketValueByFundIfFundNotFound() {
        Mockito.when(fundRepository.findByExternalFundIdWithAssociatedHoldings(Mockito.anyString()))
                .thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> investmentService.evaluateTotalMarketValueByFund(TestConstant.EXTERNAL_FUND_ID, Collections.emptySet()));
        Mockito.verify(fundRepository)
                .findByExternalFundIdWithAssociatedHoldings(Mockito.anyString());
    }

    @Test
    void testEvaluateMarketValueByFundWithNoHoldingsExcluded() {
        Mockito.when(fundRepository.findByExternalFundIdWithAssociatedHoldings(Mockito.anyString()))
                .thenReturn(Optional.of(PSInvestmentServiceObjectUtil.getFundEntity()));
        MarketValue marketValue = investmentService.evaluateTotalMarketValueByFund(TestConstant.EXTERNAL_FUND_ID, Collections.emptySet());
        assertEquals(3500, marketValue.getTotalMarketValue());
        Mockito.verify(fundRepository)
                .findByExternalFundIdWithAssociatedHoldings(Mockito.anyString());
    }

    @Test
    void testEvaluateTotalMarketValueByFundExcludingSpecificHoldings() {
        Mockito.when(fundRepository.findByExternalFundIdWithAssociatedHoldings(Mockito.anyString()))
                .thenReturn(Optional.of(PSInvestmentServiceObjectUtil.getFundEntity()));
        MarketValue marketValue = investmentService.evaluateTotalMarketValueByFund(TestConstant.EXTERNAL_FUND_ID, Set.of("Google Stock"));
        assertEquals(2000, marketValue.getTotalMarketValue());
        Mockito.verify(fundRepository)
                .findByExternalFundIdWithAssociatedHoldings(Mockito.anyString());
    }

    @Test
    void testEvaluateTotalMarketValueByFundWithoutHoldings() {
        Mockito.when(fundRepository.findByExternalFundIdWithAssociatedHoldings(Mockito.anyString()))
                .thenReturn(Optional.of(PSInvestmentServiceObjectUtil.getFundEntityWithoutHoldings()));
        MarketValue marketValue = investmentService.evaluateTotalMarketValueByFund(TestConstant.EXTERNAL_FUND_ID, Set.of("Google Stock"));
        assertEquals(0, marketValue.getTotalMarketValue());
        Mockito.verify(fundRepository)
                .findByExternalFundIdWithAssociatedHoldings(Mockito.anyString());
    }

    @Test
    void testEvaluateTotalMarketValueByInvestorIfInvestorNotFound() {
        Mockito.when(investorRepository.findByExternalInvestorIdWithAssociatedFundsAndHoldings(Mockito.anyString()))
                .thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> investmentService.evaluateTotalMarketValueByInvestor(TestConstant.EXTERNAL_INVESTOR_ID, Collections.emptySet()));
        Mockito.verify(investorRepository)
                .findByExternalInvestorIdWithAssociatedFundsAndHoldings(Mockito.anyString());
    }

    @Test
    void testEvaluateMarketValueByInvestorWithNoHoldingsExcluded() {
        Mockito.when(investorRepository.findByExternalInvestorIdWithAssociatedFundsAndHoldings(Mockito.anyString()))
                .thenReturn(Optional.of(PSInvestmentServiceObjectUtil.getInvestorEntity()));
        MarketValue marketValue = investmentService.evaluateTotalMarketValueByInvestor(TestConstant.EXTERNAL_INVESTOR_ID, Collections.emptySet());
        assertEquals(3500, marketValue.getTotalMarketValue());
        Mockito.verify(investorRepository)
                .findByExternalInvestorIdWithAssociatedFundsAndHoldings(Mockito.anyString());
    }

    @Test
    void testEvaluateTotalMarketValueByInvestorExcludingSpecificHoldings() {
        Mockito.when(investorRepository.findByExternalInvestorIdWithAssociatedFundsAndHoldings(Mockito.anyString()))
                .thenReturn(Optional.of(PSInvestmentServiceObjectUtil.getInvestorEntity()));
        MarketValue marketValue = investmentService.evaluateTotalMarketValueByInvestor(TestConstant.EXTERNAL_INVESTOR_ID, Set.of("Apple Stock"));
        assertEquals(1500, marketValue.getTotalMarketValue());
        Mockito.verify(investorRepository)
                .findByExternalInvestorIdWithAssociatedFundsAndHoldings(Mockito.anyString());
    }

    @Test
    void testEvaluateTotalMarketValueByInvestorWithoutHoldings() {
        Mockito.when(investorRepository.findByExternalInvestorIdWithAssociatedFundsAndHoldings(Mockito.anyString()))
                .thenReturn(Optional.of(PSInvestmentServiceObjectUtil.getInvestorEntityWithoutFundsAndHoldings()));
        MarketValue marketValue = investmentService.evaluateTotalMarketValueByInvestor(TestConstant.EXTERNAL_INVESTOR_ID, Set.of("Google Stock"));
        assertEquals(0, marketValue.getTotalMarketValue());
        Mockito.verify(investorRepository)
                .findByExternalInvestorIdWithAssociatedFundsAndHoldings(Mockito.anyString());
    }
}
