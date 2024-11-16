CREATE TABLE IF NOT EXISTS my_users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    releaseDate TIMESTAMP,
    duration INTEGER,
    like_count INTEGER,
    genre_id INTEGER REFERENCES genre (id) ON DELETE CASCADE,
    rating_id INTEGER REFERENCES rating (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS genre (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    genre VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS rating (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    rating VARCHAR(5)
);


CREATE TABLE IF NOT EXISTS status (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20)
);

create table if not exists userLikes (
user_id integer references my_users (id),
film_id integer references films (id),
PRIMARY KEY (user_id,film_id)
);

create table if not exists friends (
user_id integer not null references my_users (id),
friend_id integer not null references my_users (id),
PRIMARY KEY (user_id,friend_id)
);


