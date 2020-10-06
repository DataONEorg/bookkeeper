--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the products table
CREATE SEQUENCE products_id_seq;

CREATE TABLE IF NOT EXISTS products (
    id integer DEFAULT nextval('products_id_seq') PRIMARY KEY,
    object text NOT NULL,
    active boolean,
    amount integer NOT NULL,
    name text,
    caption text,
    currency text NOT NULL,
    description text,
    interval text NOT NULL,
    created timestamp with time zone,
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

COMMENT ON TABLE  products IS 'Products that are offered as services.';
COMMENT ON COLUMN products.id IS 'The unique product identifier';
COMMENT ON COLUMN products.object IS 'The serialized object type, set to "product"';
COMMENT ON COLUMN products.active IS 'The active status for the product, true or false.';
COMMENT ON COLUMN products.amount IS 'The cost amount for the product, in pence of the currency';
COMMENT ON COLUMN products.name IS 'The product name.';
COMMENT ON COLUMN products.caption IS 'The product caption, used for display.';
COMMENT ON COLUMN products.currency IS 'The product currency as a 3-letter ISO currency code.';
COMMENT ON COLUMN products.description IS 'The product general description.';
COMMENT ON COLUMN products.interval IS 'The product payment interval (day, week, month, year).';
COMMENT ON COLUMN products.created IS 'The product creation date.';
COMMENT ON COLUMN products.statementDescriptor IS 'The product statement descriptor used on charge statements.';
COMMENT ON COLUMN products.type IS 'The product type, either service or good.';
COMMENT ON COLUMN products.unitLabel IS 'The product unit label.';
COMMENT ON COLUMN products.url IS 'The product URL used for more information.';
COMMENT ON COLUMN products.metadata IS 'The product metadata JSON object, currently a feature list.';