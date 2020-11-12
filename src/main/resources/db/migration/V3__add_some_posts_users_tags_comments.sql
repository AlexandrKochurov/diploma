--admin account
INSERT INTO users
(id, code, email, is_moderator, name, password, photo, reg_time)
VALUES(1, NULL, 'admin@admin.ru', 1, 'Admin', '12345678', NULL, '2020-11-12');

--users accounts
INSERT INTO users
(id, code, email, is_moderator, name, password, photo, reg_time, posts)
VALUES(2, NULL, 'user1@user.ru', 0, 'User1', '12345678', NULL, '2020-11-12');

INSERT INTO users
(id, code, email, is_moderator, name, password, photo, reg_time)
VALUES(3, NULL, 'user2@user.ru', 0, 'User2', '12345678', NULL, '2020-11-12');

INSERT INTO users
(id, code, email, is_moderator, name, password, photo, reg_time)
VALUES(4, NULL, 'user3@user.ru', 0, 'User3', '12345678', NULL, '2020-11-12');

--posts
INSERT INTO posts
(id, is_active, moderation_status, text, time, title, view_count, moderator_id, user_id)
VALUES(1, 1, 'ACCEPTED', 'First post about Java Collections', '2020-11-12', 'First title', 5, 1, 2);

INSERT INTO posts
(id, is_active, moderation_status, text, time, title, view_count, moderator_id, user_id)
VALUES(2, 1, 'ACCEPTED', 'Second post about Spring', '2020-11-12', 'Second title', 10, 1, 3);

INSERT INTO posts
(id, is_active, moderation_status, text, time, title, view_count, moderator_id, user_id)
VALUES(3, 1, 'ACCEPTED', 'Third post about FlyWay', '2020-11-12', 'Third title', 15, 1, 4);

--comments
INSERT INTO post_comments
(id, parent_id, text, time, post_id, user_id)
VALUES(1, NULL, 'First comment', '2020-11-12', 1, 2);

INSERT INTO post_comments
(id, parent_id, text, time, post_id, user_id)
VALUES(2, NULL, 'Second comment', '2020-11-12', 2, 3);

INSERT INTO post_comments
(id, parent_id, text, time, post_id, user_id)
VALUES(3, NULL, 'Third comment', '2020-11-12', 3, 4);

--tags
INSERT INTO tags (id, name) VALUES(1, "Java Collections")
INSERT INTO tags (id, name) VALUES(2, "Spring")
INSERT INTO tags (id, name) VALUES(3, "FlyWay")

--tag2post
INSERT INTO tag2post (id, post_id, tag_id) VALUES(1, 1, 1);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(2, 2, 2);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(3, 3, 3);