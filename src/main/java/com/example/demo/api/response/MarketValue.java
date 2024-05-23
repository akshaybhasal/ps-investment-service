package com.example.demo.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * Represents a response that returns the total market value for an investor or fund
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketValue {

    private int totalMarketValue;
}
