-- Placeholder user until auth is implemented. Used by BusinessService.getCurrentUser().
INSERT INTO user (user_id, profile_name, email)
VALUES ('uuid-007', 'Mohan JP', 'jpmohan@gmail.com')
ON DUPLICATE KEY UPDATE user_id = user_id;
