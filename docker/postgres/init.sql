CREATE DATABASE db_user_service;
CREATE DATABASE db_wallet_service;

//Conceder privilégios ao usuário padrão
GRANT ALL PRIVILEGES ON DATABASE db_user_service TO postgres;
GRANT ALL PRIVILEGES ON DATABASE db_wallet_service TO postgres;