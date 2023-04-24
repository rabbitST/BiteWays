DROP TABLE IF EXISTS chef;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS ingredient;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS recipe;


CREATE TABLE chef
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255),
    chef_account INT
);

CREATE TABLE customer
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(255),
    address VARCHAR(255)
);


CREATE TABLE recipe
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255),
    downloaded INT
);


CREATE TABLE ingredient
(
    id SERIAL PRIMARY KEY ,
    name          VARCHAR(255),
    quantity      FLOAT,
    unit_type      VARCHAR(10),
    recipe_id     BIGINT
);

CREATE TABLE menu
(
    id SERIAL PRIMARY KEY
);


-- ALTER TABLE ingredient DROP CONSTRAINT IF EXISTS FK_RECIPEID_RECIPE;
ALTER TABLE ingredient
    ADD CONSTRAINT FK_INGREDIENT_RECIPE FOREIGN KEY (RECIPE_ID) REFERENCES recipe (id);
-- ALTER TABLE ingredient DROP CONSTRAINT ingredient_recipe_id_fkey;
-- ALTER TABLE ingredient DROP CONSTRAINT fk_recipeid_recipe;



