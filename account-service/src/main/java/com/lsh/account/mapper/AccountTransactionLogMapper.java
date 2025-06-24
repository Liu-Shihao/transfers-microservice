package com.lsh.account.mapper;

import com.lsh.account.entity.AccountTransactionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AccountTransactionLogMapper {
    List<AccountTransactionLog> selectByAccountId(@Param("accountId") Long accountId);
    int insert(AccountTransactionLog log);
} 