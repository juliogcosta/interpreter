DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.schemata WHERE schema_name = 'id') THEN
        CREATE SCHEMA id;
    END IF;
END $$;