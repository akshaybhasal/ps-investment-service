package com.example.demo.util;

import com.example.demo.api.request.FundRequest;
import com.example.demo.api.request.HoldingRequest;
import com.example.demo.api.request.InvestorRequest;
import com.example.demo.constant.TestConstant;
import com.example.demo.entity.FundEntity;
import com.example.demo.entity.HoldingEntity;
import com.example.demo.entity.InvestorEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PSInvestmentServiceObjectUtil {

    public static FundEntity getFundEntity() {
        FundEntity fundEntity = new FundEntity();

        Set<HoldingEntity> holdings = new HashSet<>();

        fundEntity.setName(TestConstant.FUND_NAME);
        fundEntity.setExternalFundId(TestConstant.EXTERNAL_FUND_ID);

        HoldingEntity googleStock = new HoldingEntity();
        googleStock.setQuantity(100);
        googleStock.setExternalHoldingId("852f9b98-20dd-4f1f-99bd-95777df1545c");
        googleStock.setName("Google Stock");
        googleStock.setMarketValue(15);
        holdings.add(googleStock);

        HoldingEntity appleStock = new HoldingEntity();
        appleStock.setQuantity(100);
        appleStock.setExternalHoldingId("67f2e486-3f5a-4289-a591-9a358eb2088e");
        appleStock.setName("Apple Stock");
        appleStock.setMarketValue(20);
        holdings.add(appleStock);

        fundEntity.setHoldingEntities(holdings);
        return fundEntity;
    }

    public static HoldingEntity getHoldingEntity() {
        HoldingEntity googleStock = new HoldingEntity();
        googleStock.setQuantity(100);
        googleStock.setExternalHoldingId("852f9b98-20dd-4f1f-99bd-95777df1545c");
        googleStock.setName("Google Stock");
        googleStock.setMarketValue(15);
        return googleStock;
    }

    public static FundEntity getFundEntityWithoutHoldings() {
        FundEntity fundEntity = new FundEntity();
        fundEntity.setExternalFundId(TestConstant.EXTERNAL_FUND_ID);
        fundEntity.setName(TestConstant.FUND_NAME);
        fundEntity.setHoldingEntities(Collections.emptySet());
        return fundEntity;
    }

    public static InvestorEntity getInvestorEntityWithoutFundsAndHoldings() {
        InvestorEntity investorEntity = new InvestorEntity();
        investorEntity.setName(TestConstant.INVESTOR_NAME);
        investorEntity.setExternalInvestorId(TestConstant.EXTERNAL_INVESTOR_ID);
        investorEntity.setFundEntities(Collections.emptySet());
        return investorEntity;
    }

    public static InvestorEntity getInvestorEntity() {
        InvestorEntity investorEntity = new InvestorEntity();
        investorEntity.setName(TestConstant.INVESTOR_NAME);
        investorEntity.setExternalInvestorId(TestConstant.EXTERNAL_INVESTOR_ID);
        investorEntity.setFundEntities(Set.of(getFundEntity()));
        return investorEntity;
    }

    public static InvestorRequest getInvestorRequest() {
        InvestorRequest investorRequest = new InvestorRequest();
        investorRequest.setName(TestConstant.INVESTOR_NAME);

        FundRequest fundRequest = new FundRequest();
        fundRequest.setName(TestConstant.FUND_NAME);

        Set<HoldingRequest> holdings = new HashSet<>();

        HoldingRequest googleStock = new HoldingRequest();
        googleStock.setQuantity(100);
        googleStock.setName("Google Stock");
        googleStock.setMarketValue(15);
        holdings.add(googleStock);

        HoldingRequest appleStock = new HoldingRequest();
        appleStock.setQuantity(100);
        appleStock.setName("Apple Stock");
        appleStock.setMarketValue(20);
        holdings.add(appleStock);

        fundRequest.setHoldings(holdings);
        investorRequest.setFunds(Set.of(fundRequest));
        return investorRequest;
    }
}
