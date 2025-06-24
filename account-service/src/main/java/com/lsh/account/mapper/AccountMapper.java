package com.lsh.account.mapper;

import com.lsh.account.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.math.BigDecimal;

@Mapper
public interface AccountMapper {
    Account selectById(@Param("accountId") Long accountId);
    Account selectByAccountNumber(@Param("accountNumber") String accountNumber);
    List<Account> selectAll();
    int freezeAmount(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
    int unfreezeAmount(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
    int debit(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
    int credit(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
} 