# Parole schema
 
# --- !Ups
 
CREATE TABLE parole (
    publish_date date NOT NULL,
    title varchar(255) NOT NULL,
    quote longtext NOT NULL,
    comment longtext NOT NULL,
    author varchar(255) NOT NULL,
    source varchar(255) NOT NULL,
    PRIMARY KEY (publish_date)
);
 
# --- !Downs
 
DROP TABLE parole;
