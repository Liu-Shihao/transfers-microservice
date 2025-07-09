package com.lsh.account.controller;

import com.lsh.account.entity.Account;
import com.lsh.account.entity.AccountTransactionLog;
import com.lsh.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId);
    }

    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/byUser/{userId}")
    public List<Account> getAccountsByUserId(@PathVariable("userId") Long userId) {
        return accountService.getAccountsByCustomerId(userId);
    }

    @GetMapping("/{accountId}/logs")
    public List<AccountTransactionLog> getAccountLogs(@PathVariable Long accountId) {
        return accountService.getAccountTransactionLogs(accountId);
    }

    @PostMapping("/{accountId}/freeze")
    public boolean freeze(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        return accountService.freezeAmount(accountId, amount);
    }

    @PostMapping("/{accountId}/unfreeze")
    public boolean unfreeze(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        return accountService.unfreezeAmount(accountId, amount);
    }

    @PostMapping("/{accountId}/debit")
    public boolean debit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        return accountService.debit(accountId, amount);
    }

    @PostMapping("/{accountId}/credit")
    public boolean credit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        return accountService.credit(accountId, amount);
    }
} 