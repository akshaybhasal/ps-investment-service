package com.example.demo.mapper;

import com.example.demo.api.request.FundRequest;
import com.example.demo.api.request.HoldingRequest;
import com.example.demo.api.request.InvestorRequest;
import com.example.demo.api.response.FundResponse;
import com.example.demo.api.response.InvestorResponse;
import com.example.demo.entity.FundEntity;
import com.example.demo.entity.HoldingEntity;
import com.example.demo.entity.InvestorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvestmentMapper {

    InvestorEntity toInvestorEntity(InvestorRequest investorRequest);

    FundEntity toFundEntity(FundRequest fundRequest);

    HoldingEntity toHoldingEntity(HoldingRequest holdingRequest);

    InvestorResponse toInvestorResponse(InvestorEntity investorEntity);

    FundResponse toFundResponse(FundEntity fundEntity);
}
