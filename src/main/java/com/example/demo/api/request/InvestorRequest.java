package com.example.demo.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/***
 * Represents a request to create an investor along with associated fundings
 */
@Data
@NoArgsConstructor
public class InvestorRequest {

    private String name;
    private Set<FundRequest> funds;
}
