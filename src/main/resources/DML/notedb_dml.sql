CREATE TRIGGER tsvectorupdate
    BEFORE INSERT OR UPDATE
    ON notes
    FOR EACH ROW
EXECUTE PROCEDURE
    tsvector_update_trigger(content_search_vector, 'pg_catalog.english', title, 'content');