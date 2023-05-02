DROP TABLE IF EXISTS ingredient;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS menu_recipe;

CREATE TABLE recipe
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255),
    instructions VARCHAR(600),
    downloaded   INT
);

CREATE TABLE ingredient
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255),
    quantity  FLOAT,
    unit_type VARCHAR(10),
    recipe_id BIGINT
);

CREATE TABLE menu
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE menu_recipe
(
    id        SERIAL PRIMARY KEY,
    menu_id   BIGINT,
    recipe_id BIGINT
);

ALTER TABLE ingredient
    ADD CONSTRAINT FK_INGREDIENT_RECIPE FOREIGN KEY (RECIPE_ID) REFERENCES recipe (id) ON DELETE CASCADE;


