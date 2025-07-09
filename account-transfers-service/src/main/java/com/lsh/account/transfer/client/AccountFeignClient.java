package com.lsh.account.transfer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import com.lsh.account.transfer.model.AccountDTO;

@FeignClient(name = "account-service")
public interface AccountFeignClient {
    @GetMapping("/api/account/all")
    List<AccountDTO> fetchAllAccounts();

    @GetMapping("/api/account/byUser/{userId}")
    List<AccountDTO> fetchAccountsByUserId(@PathVariable("userId") Long userId);
} 