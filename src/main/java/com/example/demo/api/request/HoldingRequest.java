package com.example.demo.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * Represents a request to create a holding including its market value and quantity
 */
@Data
@NoArgsConstructor
public class HoldingRequest {

    private String name;
    private int marketValue;
    private int quantity;
}
