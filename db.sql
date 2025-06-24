-- ============================================
-- 1. account-service 数据库结构
-- ============================================

-- 客户信息表
CREATE TABLE customer (
    customer_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name         VARCHAR(50) NOT NULL,
    last_name          VARCHAR(50) NOT NULL,
    email              VARCHAR(100),
    phone              VARCHAR(20),
    address_line1      VARCHAR(255),
    address_line2      VARCHAR(255),
    city               VARCHAR(50),
    state              VARCHAR(50),
    postal_code        VARCHAR(20),
    country            VARCHAR(50),
    customer_type      VARCHAR(20) DEFAULT 'INDIVIDUAL', -- INDIVIDUAL / CORPORATE
    status             VARCHAR(20) DEFAULT 'ACTIVE',
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 账户信息表
CREATE TABLE account (
    account_id         BIGINT PRIMARY KEY,
    customer_id        BIGINT NOT NULL,
    account_number     VARCHAR(32) NOT NULL UNIQUE,
    account_type       VARCHAR(20) NOT NULL, -- checking, savings, loan, etc.
    currency           VARCHAR(10) NOT NULL,
    total_balance      DECIMAL(18, 2) NOT NULL,
    available_balance  DECIMAL(18, 2) NOT NULL,
    frozen_amount      DECIMAL(18, 2) NOT NULL DEFAULT 0,
    status             VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 账户操作流水
CREATE TABLE account_transaction_log (
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id         BIGINT NOT NULL,
    operation_type     VARCHAR(20) NOT NULL, -- FREEZE, UNFREEZE, DEBIT, CREDIT
    amount             DECIMAL(18, 2) NOT NULL,
    related_transfer_id BIGINT,
    remark             VARCHAR(255),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 2. account-transfers-service
-- ============================================

-- 转账发起请求表（客户端请求记录）
CREATE TABLE account_transfer_request (
    request_id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    from_account_id    BIGINT NOT NULL,
    to_account_id      BIGINT NOT NULL,
    amount             DECIMAL(18, 2) NOT NULL,
    currency           VARCHAR(10) NOT NULL,
    schedule_type      VARCHAR(20) NOT NULL, -- onetime_immediate, onetime_future, recurring
    transfer_time      TIMESTAMP NULL,
    frequency          VARCHAR(20) NULL, -- for recurring: DAILY, WEEKLY, MONTHLY
    status             VARCHAR(20) DEFAULT 'PENDING',
    description        VARCHAR(255),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 批量转账任务主表
CREATE TABLE batch_transfer_request (
    batch_id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    from_account_id    BIGINT NOT NULL,
    currency           VARCHAR(10) NOT NULL,
    total_amount       DECIMAL(18, 2) NOT NULL,
    schedule_type      VARCHAR(20) NOT NULL, -- batch_immediate, batch_future
    transfer_time      TIMESTAMP NULL,
    status             VARCHAR(20) DEFAULT 'PENDING',
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 批量转账子项明细表
CREATE TABLE batch_transfer_item (
    item_id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    batch_id           BIGINT NOT NULL,
    to_account_id      BIGINT NOT NULL,
    amount             DECIMAL(18, 2) NOT NULL,
    remark             VARCHAR(255),
    status             VARCHAR(20) DEFAULT 'PENDING',
    request_id         BIGINT, -- 对应 account_transfer_request.request_id
    transfer_id        BIGINT, -- 对应 transfer_order.transfer_id
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 3. payment-transfers-service
-- ============================================

-- 实际支付转账订单表
CREATE TABLE transfer_order (
    transfer_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_id         BIGINT NOT NULL,
    from_account_id    BIGINT NOT NULL,
    to_account_id      BIGINT NOT NULL,
    amount             DECIMAL(18, 2) NOT NULL,
    currency           VARCHAR(10) NOT NULL,
    status             VARCHAR(20) NOT NULL DEFAULT 'INIT', -- INIT, FROZEN, PROCESSING, SUCCESS, FAILED
    transfer_mode      VARCHAR(20), -- WIRE, ACH, INTERNAL
    schedule_type      VARCHAR(20), -- onetime_immediate, onetime_future, recurring, batch
    transfer_time      TIMESTAMP NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 4. scheduler-service
-- ============================================

-- 周期或定时任务调度表
CREATE TABLE transfer_schedule_task (
    task_id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    transfer_id        BIGINT NOT NULL,
    next_run_time      TIMESTAMP NOT NULL,
    frequency          VARCHAR(20), -- DAILY, WEEKLY, MONTHLY
    status             VARCHAR(20) DEFAULT 'SCHEDULED',
    retry_count        INT DEFAULT 0,
    max_retries        INT DEFAULT 3,
    last_attempt_time  TIMESTAMP NULL
);

-- ============================================
-- 5. approval-service
-- ============================================

-- 审批记录表
CREATE TABLE transfer_approval (
    approval_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    transfer_id        BIGINT NOT NULL,
    approver_id        BIGINT NOT NULL,
    approved           BOOLEAN DEFAULT FALSE,
    approved_at        TIMESTAMP NULL,
    comment            VARCHAR(255),
    status             VARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 审批规则配置表
CREATE TABLE approval_rule (
    rule_id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    min_amount         DECIMAL(18, 2) NOT NULL,
    max_amount         DECIMAL(18, 2),
    required_role      VARCHAR(50) NOT NULL,
    description        VARCHAR(255)
);

-- ============================================
-- 常用查询示例
-- ============================================

-- 查询账户余额
SELECT account_number, total_balance, available_balance, frozen_amount
FROM account WHERE account_id = ?;

-- 查询账户转账请求状态
SELECT * FROM account_transfer_request WHERE request_id = ?;

-- 查询转账订单状态
SELECT * FROM transfer_order WHERE transfer_id = ?;

-- 查询某账户的最近转账流水
SELECT * FROM account_transaction_log WHERE account_id = ? ORDER BY created_at DESC LIMIT 10;

-- 查询某笔转账是否需要审批
SELECT * FROM approval_rule WHERE min_amount <= ? AND (max_amount IS NULL OR max_amount >= ?);

-- 查询批量转账主记录
SELECT * FROM batch_transfer_request WHERE from_account_id = ? ORDER BY created_at DESC;

-- 查询某批次下所有子项
SELECT * FROM batch_transfer_item WHERE batch_id = ? ORDER BY created_at;
