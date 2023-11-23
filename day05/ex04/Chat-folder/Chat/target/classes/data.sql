
BEGIN;

-- user's dummy data
INSERT INTO users (login, password) 
VALUES
('ahmed', '1234'),
('mohammed', '1234'),
('islam', '1234'),
('khabib', '1234'),
('salah-din', '1234');



-- chatrooms dummy data
INSERT INTO chatrooms (chatroom_name, chatroom_owner)
VALUES
('General', 1),
('Sports', 2),
('Music', 3),
('Movies', 4),
('Games', 5);

-- messages dummy data
INSERT INTO messages (message_author, message_room, message_text) VALUES
(1, 1, 'Hello everyone!'),
(2, 1, 'Hi Alice!'),
(3, 1, 'Greetings!'),
(4, 2, 'Who watched the last soccer match?'),
(5, 2, 'I did, it was awesome!'),
(1, 3, 'What is your favorite song?'),
(2, 3, 'I like rock music'),
(3, 3, 'I prefer jazz'),
(4, 4, 'Have you seen the new Spider-Man movie?'),
(5, 4, 'Not yet, is it good?'),
(1, 5, 'Anyone up for a game of chess?'),
(2, 5, 'Sure, why not?'),
(3, 5, 'Im in too'),
(4, 5, 'Me too'),
(5, 5, 'Lets play then');






-- join table dummy data
INSERT INTO user_chatroom (user_id, chatroom_id) VALUES
(1, 1),
(1, 3),
(1, 5),
(2, 1),
(2, 2),
(2, 3),
(2, 5),
(3, 1),
(3, 3),
(3, 5),
(4, 2),
(4, 4),
(4, 5),
(5, 2),
(5, 4),
(5, 5);


COMMIT;
