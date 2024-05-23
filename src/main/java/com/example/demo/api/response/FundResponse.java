package com.example.demo.api.response;

import lombok.Data;

/***
 * Represents a response that returns fund associated to an investor
 * The externalFundId identifier is returned to the client to allow access to the specific fund without exposing
 * internal primary key
 */
@Data
public class FundResponse {

    private String name;
    private String externalFundId;
}
