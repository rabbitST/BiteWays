DROP TABLE IF EXISTS ingredient;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS menu_recipe;

CREATE TABLE chef
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE recipe
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255),
    instructions VARCHAR(600),
    downloaded   INT,
    chef_id      BIGINT,
    FOREIGN KEY (chef_id) REFERENCES chef (id)
);

CREATE TABLE ingredient
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255),
    quantity  FLOAT,
    unit_type VARCHAR(10),
    recipe_id BIGINT,
    CONSTRAINT FK_INGREDIENT_RECIPE FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE
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
    recipe_id BIGINT,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE
);

create table customer
(
    id          SERIAL PRIMARY KEY,
    address     varchar(255),
    cell_phone  varchar(255),
    city        varchar(255),
    country     varchar(255),
    first_name  varchar(255),
    last_name   varchar(255),
    postal_code varchar(255)
);

create table order_table
(
    id          serial PRIMARY KEY,
    customer_id bigint,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);