CREATE TABLE IF NOT EXISTS my_users (
    id BIGINT  PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS films (
    id BIGINT  PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    releaseDate TIMESTAMP,
    duration INTEGER,
    like_count BIGINT ,
    genre_id INTEGER,
    rating_id INTEGER
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT  NOT NULL,
    friend_id BIGINT  NOT NULL,
    status_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS userLikes (
    user_id BIGINT  NOT NULL,
    film_id BIGINT  NOT NULL,
    PRIMARY KEY (user_id, film_id)
);