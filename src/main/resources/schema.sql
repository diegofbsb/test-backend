CREATE TABLE IF NOT EXISTS clientes (
                                        id SERIAL PRIMARY KEY,
                                        limite INTEGER NOT NULL,
                                        saldo INTEGER NOT NULL
);

-- Aproveite para colocar os índices que conversamos antes aqui!
CREATE INDEX IF NOT EXISTS idx_clientes_id ON clientes (id);
