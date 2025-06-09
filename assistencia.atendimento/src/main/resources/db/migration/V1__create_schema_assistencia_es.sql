DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.schemata WHERE schema_name = 'assistencia_es') THEN
        CREATE SCHEMA assistencia_es;
    END IF;
END $$;