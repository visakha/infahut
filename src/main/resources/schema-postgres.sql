-- DDL for PostgreSQL: schema hut in huddb
CREATE SCHEMA IF NOT EXISTS hut;

CREATE TABLE IF NOT EXISTS hut.login_sessions (
    id SERIAL PRIMARY KEY,
    seq INTEGER,
    session_id VARCHAR(255),
    base_api_url VARCHAR(255),
    response TEXT,
    timestamp VARCHAR(255)
);

-- Add more tables here as you add more entities
