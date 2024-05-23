package com.example.demo.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/***
 * Represents a response that returns the investor created along with its associated fundings
 * The externalInvestorId identifier is returned to the client to allow access to the specific investor without exposing
 * internal primary key
 */
@Data
@NoArgsConstructor
public class InvestorResponse {

    private String name;
    private String externalInvestorId;
    private List<FundResponse> funds;
}
