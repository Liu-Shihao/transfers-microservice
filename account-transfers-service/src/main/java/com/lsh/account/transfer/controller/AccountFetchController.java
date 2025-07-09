package com.lsh.account.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.lsh.account.transfer.client.AccountFeignClient;
import com.lsh.account.transfer.model.AccountDTO;

@RestController
public class AccountFetchController {
    @Autowired
    private AccountFeignClient accountFeignClient;

    @GetMapping("/fetchAccount")
    public List<AccountDTO> fetchAccount() {
        return accountFeignClient.fetchAllAccounts();
    }

    @GetMapping("/fetchAccount/byUser/{userId}")
    public List<AccountDTO> fetchAccountByUserId(@PathVariable("userId") Long userId) {
        return accountFeignClient.fetchAccountsByUserId(userId);
    }
} 