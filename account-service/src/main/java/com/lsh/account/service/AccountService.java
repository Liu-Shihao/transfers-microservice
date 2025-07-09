package com.lsh.account.service;

import com.lsh.account.entity.Account;
import com.lsh.account.entity.AccountTransactionLog;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account getAccountById(Long accountId);
    List<AccountTransactionLog> getAccountTransactionLogs(Long accountId);
    boolean freezeAmount(Long accountId, BigDecimal amount);
    boolean unfreezeAmount(Long accountId, BigDecimal amount);
    boolean debit(Long accountId, BigDecimal amount);
    boolean credit(Long accountId, BigDecimal amount);
    List<Account> getAllAccounts();
    List<Account> getAccountsByCustomerId(Long customerId);
} 