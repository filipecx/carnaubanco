CREATE TABLE wallets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    currency VARCHAR(10) NOT NULL DEFAULT 'PALHA',
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT chk_wallet_balance_positive CHECK (balance >= 0.00)
);

CREATE INDEX idx_wallet_user_id ON wallets(user_id);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    source_wallet_id UUID NOT NULL REFERENCES wallets(id),
    target_wallet_id UUID NOT NULL REFERENCES wallets(id),
    amount DECIMAL(15, 2) NOT NULL,
    type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL

    CONSTRAINT chk_transaction_amount_positive CHECK (amount > 0.00),
    CONSTRAINT chk_transaction_different_wallets CHECK (source_wallet_id <> target_wallet_id)
);

-- indices para consulta de extrato 

CREATE INDEX idx_transactions_source ON transactions(source_wallet_id);
CREATE INDEX idx_transactions_target ON transactions(target_wallet_id);
CREATE INDEX idx_transactions_created_at ON transactions(created_at DESC);