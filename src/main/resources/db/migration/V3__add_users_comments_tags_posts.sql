-- admin account
INSERT INTO users
(id, code, email, is_moderator, name, password, photo, reg_time)
VALUES(1, NULL, 'admin@admin.ru', 1, 'Admin', '12345678', NULL, '2020-11-12');

-- users accounts
INSERT INTO users
(id, code, email, is_moderator, name, password, photo, reg_time)
VALUES(2, NULL, 'user1@user.ru', 0, 'User1', '12345678', NULL, '2020-11-12');

INSERT INTO users
(id, code, email, is_moderator, name, password, photo, reg_time)
VALUES(3, NULL, 'user2@user.ru', 0, 'User2', '12345678', NULL, '2020-11-12');

INSERT INTO users
(id, code, email, is_moderator, name, password, photo, reg_time)
VALUES(4, NULL, 'user3@user.ru', 0, 'User3', '12345678', NULL, '2020-11-12');

-- posts
INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, time, title, user_id, view_count)
VALUES(1, 1, 'ACCEPTED', 1, 'First post about Java Collections', '2020-11-12', 'First title', 2, 5);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, time, title, user_id, view_count)
VALUES(2, 1, 'ACCEPTED', 1, 'Second post about Spring', '2020-11-12', 'Second title', 3, 10);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, time, title, user_id, view_count)
VALUES(3, 1, 'ACCEPTED', 1, 'Third post about FlyWay', '2020-11-12', 'Third title', 4, 15);

-- comments
INSERT INTO post_comments
(id, parent_id, post_id, text, time, user_id)
VALUES(1, NULL, 1, 'First comment', '2020-11-12', 2);

INSERT INTO post_comments
(id, parent_id, post_id, text, time, user_id)
VALUES(2, NULL, 2, 'Second comment', '2020-11-12', 3);

INSERT INTO post_comments
(id, parent_id, post_id, text, time, user_id)
VALUES(3, NULL, 3, 'Third comment', '2020-11-12', 4);

-- tags
INSERT INTO tags (id, name) VALUES(1, "Java Collections")
INSERT INTO tags (id, name) VALUES(2, "Spring")
INSERT INTO tags (id, name) VALUES(3, "FlyWay")

-- tag2post
INSERT INTO tag2post (id, post_id, tag_id) VALUES(1, 1, 1);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(2, 2, 2);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(3, 3, 3);
