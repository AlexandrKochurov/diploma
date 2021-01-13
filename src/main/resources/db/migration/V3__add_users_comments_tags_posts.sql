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
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(1, 1, 'ACCEPTED', 1, 'First post about Java Collections', '2020-06-30', 'First title', 2, 5);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(2, 1, 'ACCEPTED', 1, 'Second post about Spring', '2020-07-2', 'Second title', 3, 10);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(3, 1, 'ACCEPTED', 1, 'Third post about FlyWay', '2020-08-22', 'Third title', 4, 15);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(4, 1, 'ACCEPTED', 1, 'Fourth post about Hibernate', '2020-09-12', 'Fourth title', 2, 20);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(5, 1, 'ACCEPTED', 1, 'Fifth post about Lombok', '2020-11-14', 'Fifth title', 3, 25);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(6, 1, 'ACCEPTED', 1, 'Sixth post about Swagger', '2020-12-28', 'Sixth title', 4, 30);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(7, 1, 'ACCEPTED', 1, 'Seventh post about ExceptionHandler', '2020-11-19', 'Seventh title', 2, 35);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(8, 1, 'ACCEPTED', 1, 'Eighth post about services', '2020-11-12', 'Eighth title', 3, 40);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(9, 1, 'ACCEPTED', 1, 'Ninth post about controllers', '2020-11-12', 'Ninth title', 2, 45);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(10, 1, 'ACCEPTED', 1, 'Tenth post about repositories', '2020-11-13', 'Tenth title', 3, 50);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(11, 1, 'ACCEPTED', 1, 'Eleventh post about DTO', '2020-11-23', 'Eleventh title', 4, 55);

INSERT INTO posts
(id, is_active, moderator_status, moderator_id, text, instant, title, user_id, view_count)
VALUES(12, 1, 'ACCEPTED', 1, 'Twelve post to check Calendar', '2021-01-01', 'Twelve title', 4, 1);

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

INSERT INTO post_comments
(id, parent_id, post_id, text, time, user_id)
VALUES(4, NULL, 3, 'Fourth comment', '2020-11-12', 2);

INSERT INTO post_comments
(id, parent_id, post_id, text, time, user_id)
VALUES(5, NULL, 3, 'Fifth comment', '2020-11-12', 2);

INSERT INTO post_comments
(id, parent_id, post_id, text, time, user_id)
VALUES(6, NULL, 3, 'Sixth comment', '2020-11-12', 3);

-- tags
INSERT INTO tags (id, name) VALUES(1, 'Java Collections');
INSERT INTO tags (id, name) VALUES(2, 'Spring');
INSERT INTO tags (id, name) VALUES(3, 'FlyWay');

-- tag2post
INSERT INTO tag2post (id, post_id, tag_id) VALUES(1, 1, 1);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(2, 2, 2);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(3, 3, 3);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(4, 10, 2);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(5, 9, 3);
INSERT INTO tag2post (id, post_id, tag_id) VALUES(6, 8, 3);
