package com.example.demo.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/***
 * Represents a request to create fund along with associated holdings
 */
@Data
@NoArgsConstructor
public class FundRequest {

    private String name;
    private Set<HoldingRequest> holdings;
}
