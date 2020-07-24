--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the update quota usage function for inserts and updates
CREATE OR REPLACE FUNCTION update_quota_usage_on_insert_or_update()
    RETURNS trigger
    AS $update_quota_usage_on_insert_or_update$
    BEGIN
        -- Check that quotaId is given
        IF NEW.quotaId IS NULL THEN
            RAISE EXCEPTION 'quotaId cannot be null';
        END IF;

        -- Update the quotas.totalUsage column
        UPDATE quotas q
            SET totalUsage = (SELECT SUM(u.quantity) FROM usages u WHERE u.status != 'inactive' AND u.quotaId = NEW.quotaId)
                WHERE q.id = NEW.quotaId;
        RETURN NEW;
    END;
    $update_quota_usage_on_insert_or_update$
    LANGUAGE plpgsql;

--- Create the update quota totalUsage trigger
DROP TRIGGER IF EXISTS update_quotas_usage_on_insert_or_update ON usages;
CREATE TRIGGER update_quotas_usage_on_insert_or_update AFTER INSERT OR UPDATE ON usages
    FOR EACH ROW
        EXECUTE PROCEDURE update_quota_usage_on_insert_or_update();

--- Create the update quota totalUsage function for deletes
CREATE OR REPLACE FUNCTION update_quota_usage_on_delete()
    RETURNS trigger
    AS $update_quota_usage_on_delete$
    BEGIN
        -- Check that quotaId is given
        IF OLD.quotaId IS NULL THEN
            RAISE EXCEPTION 'quotaId cannot be null';
        END IF;

        -- Update the quotas.usage column
        UPDATE quotas q
            SET totalUsage = (SELECT SUM(u.quantity) FROM usages u WHERE u.status != 'inactive' AND u.quotaId = OLD.quotaId)
                WHERE q.id = OLD.quotaId;
        RETURN OLD;
    END;
    $update_quota_usage_on_delete$
    LANGUAGE plpgsql;

--- Create the update quota totalUsage trigger
DROP TRIGGER IF EXISTS update_quotas_usage_on_delete ON usages;
CREATE TRIGGER update_quotas_usage_on_delete AFTER DELETE ON usages
    FOR EACH ROW
        EXECUTE PROCEDURE update_quota_usage_on_delete();
