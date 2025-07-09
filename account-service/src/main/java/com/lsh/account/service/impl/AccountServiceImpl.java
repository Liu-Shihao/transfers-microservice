package com.lsh.account.service.impl;

import com.lsh.account.entity.Account;
import com.lsh.account.entity.AccountTransactionLog;
import com.lsh.account.mapper.AccountMapper;
import com.lsh.account.mapper.AccountTransactionLogMapper;
import com.lsh.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountTransactionLogMapper logMapper;

    @Override
    public Account getAccountById(Long accountId) {
        return accountMapper.selectById(accountId);
    }

    @Override
    public List<AccountTransactionLog> getAccountTransactionLogs(Long accountId) {
        return logMapper.selectByAccountId(accountId);
    }

    @Override
    @Transactional
    public boolean freezeAmount(Long accountId, BigDecimal amount) {
        //当可用金额大于当前冻结金额时，可以冻结金额，并更新可用金额（-amount）和冻结金额（+amount）
        int updated = accountMapper.freezeAmount(accountId, amount);
        if (updated > 0) {
            AccountTransactionLog log = new AccountTransactionLog();
            log.setAccountId(accountId);
            log.setOperationType("FREEZE");
            log.setAmount(amount);
            logMapper.insert(log);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean unfreezeAmount(Long accountId, BigDecimal amount) {
        //当冻结金额大于当前可用金额时，可以解冻金额，并更新可用金额（+amount）和冻结金额（-amount）
        int updated = accountMapper.unfreezeAmount(accountId, amount);
        if (updated > 0) {
            AccountTransactionLog log = new AccountTransactionLog();
            log.setAccountId(accountId);
            log.setOperationType("UNFREEZE");
            log.setAmount(amount);
            logMapper.insert(log);
            return true;
        }
        return false;
    }

    /**
     * 扣款从frozen_balance中扣除，并更新total_balance和frozen_balance
     */
    @Override
    @Transactional
    public boolean debit(Long accountId, BigDecimal amount) {
        int updated = accountMapper.debit(accountId, amount);
        if (updated > 0) {
            AccountTransactionLog log = new AccountTransactionLog();
            log.setAccountId(accountId);
            log.setOperationType("DEBIT");
            log.setAmount(amount);
            logMapper.insert(log);
            return true;
        }
        return false;
    }

    /**
     * 存款直接更新total_balance和available_balance
     */
    @Override
    @Transactional
    public boolean credit(Long accountId, BigDecimal amount) {
        int updated = accountMapper.credit(accountId, amount);
        if (updated > 0) {
            AccountTransactionLog log = new AccountTransactionLog();
            log.setAccountId(accountId);
            log.setOperationType("CREDIT");
            log.setAmount(amount);
            logMapper.insert(log);
            return true;
        }
        return false;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountMapper.selectAll();
    }

    @Override
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountMapper.selectByCustomerId(customerId);
    }
} 