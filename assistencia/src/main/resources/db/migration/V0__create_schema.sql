DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.schemata WHERE schema_name = 'assistencia') THEN
        CREATE SCHEMA assistencia;
    END IF;
END $$;