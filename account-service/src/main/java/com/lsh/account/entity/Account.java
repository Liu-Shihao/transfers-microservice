package com.lsh.account.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class Account {
    private Long accountId;
    private Long customerId;
    private String accountNumber;
    private String accountType;
    private String currency;
    private BigDecimal totalBalance;
    private BigDecimal availableBalance;
    private BigDecimal frozenAmount;
    private String status;
    private Timestamp createdAt;
} 