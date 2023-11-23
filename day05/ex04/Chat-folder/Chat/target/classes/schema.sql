-- user's table
CREATE TABLE users
(
    user_id  SERIAL PRIMARY KEY,
    login    VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- chatroom's table
CREATE TABLE chatrooms
(
    chatroom_id    SERIAL PRIMARY KEY,
    chatroom_name  VARCHAR(50) NOT NULL UNIQUE,
    chatroom_owner INTEGER     NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);

-- message's table
CREATE TABLE messages
(
    message_id       SERIAL PRIMARY KEY,
    message_author   INTEGER   NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    message_room     INTEGER   NOT NULL REFERENCES chatrooms (chatroom_id) ON DELETE CASCADE,
    message_text     TEXT      NOT NULL,
    message_datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_chatroom
(
    user_id     INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    chatroom_id INTEGER NOT NULL REFERENCES chatrooms (chatroom_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, chatroom_id)
);