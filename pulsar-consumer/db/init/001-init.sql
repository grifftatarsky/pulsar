-- Producer (SoT) DB
CREATE DATABASE station_sot;

-- A couple of local consumer DBs (you can add more)
CREATE DATABASE consumer_a;
CREATE DATABASE consumer_b;

-- Least-priv users
CREATE ROLE producer_app WITH LOGIN PASSWORD 'producer_pw' NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT;
CREATE ROLE consumer_a_app WITH LOGIN PASSWORD 'consumer_a_pw' NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT;
CREATE ROLE consumer_b_app WITH LOGIN PASSWORD 'consumer_b_pw' NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT;

GRANT CONNECT ON DATABASE station_sot TO producer_app;
GRANT CONNECT ON DATABASE consumer_a TO consumer_a_app;
GRANT CONNECT ON DATABASE consumer_b TO consumer_b_app;

\connect station_sot
GRANT USAGE, CREATE ON SCHEMA public TO producer_app;
GRANT TEMP ON DATABASE station_sot TO producer_app;

\connect consumer_a
GRANT USAGE, CREATE ON SCHEMA public TO consumer_a_app;
GRANT TEMP ON DATABASE consumer_a TO consumer_a_app;

\connect consumer_b
GRANT USAGE, CREATE ON SCHEMA public TO consumer_b_app;
GRANT TEMP ON DATABASE consumer_b TO consumer_b_app;
