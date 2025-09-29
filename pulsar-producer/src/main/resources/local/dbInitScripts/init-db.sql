CREATE OR REPLACE PROCEDURE manage_schema(schema_name text)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Drop schema if it exists and create it
EXECUTE format('DROP SCHEMA IF EXISTS %I CASCADE;', schema_name);
EXECUTE format('CREATE SCHEMA IF NOT EXISTS %I;', schema_name);

-- Check if the user exists, and create the user if it does not exist
IF NOT EXISTS (
            SELECT 1
            FROM pg_roles
            WHERE rolname = format('nimbus_app_%I', schema_name)
        ) THEN
            EXECUTE format('CREATE USER nimbus_app_%I WITH PASSWORD %L;', schema_name, schema_name);
ELSE
            RAISE NOTICE 'User nimbus_app_% already exists, skipping creation.', schema_name;
END IF;

EXECUTE format('GRANT ALL PRIVILEGES ON SCHEMA %I TO nimbus_app_%I;', schema_name, schema_name);
-- Grant all privileges on all tables and sequences within the schema to the user
EXECUTE format('GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA %I TO nimbus_app_%I;', schema_name, schema_name);
EXECUTE format('GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA %I TO nimbus_app_%I;', schema_name, schema_name);

-- Grant all privileges on all functions within the schema to the user (if applicable)
EXECUTE format('GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA %I TO nimbus_app_%I;', schema_name, schema_name);
-- set search path
EXECUTE format('ALTER SCHEMA %I OWNER TO nimbus_app_%I;', schema_name, schema_name);

END $$;


CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_raster;
-- station must go last so the text, netcdf, and cave users exist
CALL manage_schema('station');