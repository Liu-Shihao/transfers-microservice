package com.lsh.account.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class AccountTransactionLog {
    private Long id;
    private Long accountId;
    private String operationType;
    private BigDecimal amount;
    private Long relatedTransferId;
    private String remark;
    private Timestamp createdAt;
} 