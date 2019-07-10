--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the products table
CREATE SEQUENCE products_id_seq;

CREATE TABLE IF NOT EXISTS products (
    id bigint DEFAULT nextval('products_id_seq') PRIMARY KEY,
    object text NOT NULL,
    active boolean,
    name text,
    caption text,
    description text,
    created timestamp without time zone,
    statementDescriptor text,
    type text,
    unitLabel text,
    url text,
    metadata json
);
ALTER SEQUENCE products_id_seq OWNED BY products.id;

CREATE INDEX products_active_idx ON products USING btree(active);
CREATE INDEX products_name_idx ON products USING btree(name);
CREATE INDEX products_caption_idx ON products USING btree(caption);
CREATE INDEX products_description_idx ON products USING btree(description);

COMMENT ON TABLE  customers IS 'Products that are offered as services.';
COMMENT ON COLUMN customers.id IS 'The unique product identifier';
COMMENT ON COLUMN customers.object IS 'The serialized object type, set to "product"';
COMMENT ON COLUMN customers.active IS 'The active status for the product, true or false.';
COMMENT ON COLUMN customers.name IS 'The product name.';
COMMENT ON COLUMN customers.caption IS 'The product caption, used for display.';
COMMENT ON COLUMN customers.description IS 'The product general description.';
COMMENT ON COLUMN customers.created IS 'The product creation date.';
COMMENT ON COLUMN customers.statementDescriptor IS 'The product statement descriptor used on charge statements.';
COMMENT ON COLUMN customers.type IS 'The product type, either service or good.';
COMMENT ON COLUMN customers.unitLabel IS 'The product unit label.';
COMMENT ON COLUMN customers.url IS 'The product URL used for more information.';
COMMENT ON COLUMN customers.metadata IS 'The product metadata JSON object, currently a feature list.';



