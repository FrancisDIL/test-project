alter sequence hibernate_sequence restart with 20000;

--users, user_personal_details 100 - 300
INSERT INTO public.users (id, created_date, last_modified_date, created_by_user_id, last_modified_by_user_id, account_non_expired, account_non_locked, credentials_non_expired, enabled, first_name, last_name, phone_number, password, email, account_deleted, account_deleted_by_user_id, account_deleted_date, name) VALUES (1, now(), now(), null, null, true, true, true, true, 'user_first_1', 'user_last_1', '0700000001', '$2a$10$AS1PLLtkaTtvq3Rxw8yYNOLqPIbn0fqmtXtFDEURU0ZSYjjSRaLEa', 'user1@dev.com', false, null, null, 'user_first_1 user_last_1');
INSERT INTO public.user_roles (user_id, role) VALUES (1, 'LIBRARIAN');
INSERT INTO public.users (id, created_date, last_modified_date, created_by_user_id, last_modified_by_user_id, account_non_expired, account_non_locked, credentials_non_expired, enabled, first_name, last_name, phone_number, password, email, account_deleted, account_deleted_by_user_id, account_deleted_date, name) VALUES (2, now(), now(), null, null, true, true, true, true, 'user_first_2', 'user_last_2', '0700000002', '$2a$10$AS1PLLtkaTtvq3Rxw8yYNOLqPIbn0fqmtXtFDEURU0ZSYjjSRaLEa', 'user2@dev.com', false, null, null, 'user_first_2 user_last_2');
INSERT INTO public.user_roles (user_id, role) VALUES (2, 'MEMBER');
INSERT INTO public.books (id, author, title, status) VALUES(1,'Carl Sagan', 'Pale Blue Dot', 0);
INSERT INTO public.books (id, author, title, status) VALUES(2,'Paul Johnson', 'Intellectuals', 0);
INSERT INTO public.books (id, author, title, status) VALUES(3,'Tolstoy', 'War and Peace', 0);
INSERT INTO public.books (id, author, title, status) VALUES(4,'Fyodor Dostoyevsky', 'The Brothers Karamazov', 0);
INSERT INTO public.books (id, author, title, status) VALUES(5,'Fyodor Dostoyevsky', 'The Idiot', 1);
INSERT INTO public.books (id, author, title, status) VALUES(6,'Gil Smolin', 'The Reign of the Rat', 0);
INSERT INTO public.books (id, author, title, status) VALUES(7,'Stephen Hawking', 'A Brief History of the Universe', 0);
INSERT INTO public.books (id, author, title, status) VALUES(8,'OUP', 'Modern English Dictionary', 0);
INSERT INTO public.books (id, author, title, status) VALUES(9,'J.K. Sprawlings', 'The Theory of Returnables', 1);