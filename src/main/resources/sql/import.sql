-- contacts
INSERT INTO `contact` (firstName, lastName, streetAddress, city, state, zipCode, email, statusCode, updatedAt, createdAt) VALUES ('HOWARD','WOLOWITZ', '1001 APPLETREE ROAD','SAN FRANCISCO','CA','90016','howardw@example.com', 'ACTIVE', '2019-01-22 09:51:42.324', '2019-01-21 04:11:21.224');
INSERT INTO `contact` (firstName, lastName, streetAddress, city, state, zipCode, email, statusCode, updatedAt, createdAt) VALUES ('SHELDON','COOPER', '1010 GOOGLE WAY','PASADENA','CA','91103','sheldon@google.com','DISABLED', '2019-02-05 20:12:12.150', '2019-02-02 19:22:52.124');
INSERT INTO `contact` (firstName, lastName, streetAddress, city, state, zipCode, email, statusCode, updatedAt, createdAt) VALUES ('LEONARD','HOFSTANDER', '1001 GOOGLE WAY','PASADENA','CA','91102','leonard@google.com', 'ACTIVE', '2019-02-10 20:11:13.144', '2019-02-06 07:12:22.161');
INSERT INTO `contact` (firstName, lastName, streetAddress, city, state, zipCode, email, statusCode, updatedAt, createdAt) VALUES ('RAJESH','KOOTHRAPPALI', '1100 REDWOOD BLVD','PASADENA','CA','91103','rajr@google.com', 'INACTIVE', '2020-02-03 15:11:13.144', '2019-12-03 08:12:31.110');
INSERT INTO `contact` (firstName, lastName, streetAddress, city, state, zipCode, email, statusCode, updatedAt, createdAt) VALUES ('AMY','FOWLER', '1201 MOUNTAINVIEW DR','PASADENA','CA','91102','amyfowler@gmail.com', 'ACTIVE', '2020-03-23 18:22:01.141', '2019-03-15 11:43:10.110');

-- users
INSERT INTO `user` (firstName, lastName, email, userName, password, updatedAt, createdAt) VALUES ('Joe', 'Doe', 'joe.doe@crm.com', 'JDOE', 'password1', '2020-01-05 11:16:25.421', '2019-12-05 11:01:19.109');
INSERT INTO `user` (firstName, lastName, email, userName, password, updatedAt, createdAt) VALUES ('John', 'User', 'john.user@crm.com', 'JUSER', 'password2', '2019-12-23 10:26:21.001', '2019-12-01 15:11:09.003');

-- roles
INSERT INTO `role` (roleName, updatedAt, createdAt) VALUES ('ADMIN', '2019-02-19 12:19:17.000', '2019-01-01 07:10:09.003');
INSERT INTO `role` (roleName, updatedAt, createdAt) VALUES ('READ_ONLY', '2019-02-21 11:04:22.001', '2019-01-02 09:10:09.005');
INSERT INTO `role` (roleName, updatedAt, createdAt) VALUES ('READ_WRITE', '2019-02-22 09:26:11.009', '2019-01-02 10:30:12.007');

-- tasks
INSERT INTO `task` (note, status, contact_id, created_by, assigned_to, dueDate, completedDate, createdAt, updatedAt) VALUES ('Is it Friday yet?', 'OPEN', 2, 2, 2, '2020-10-05', null, '2020-09-12 07:11:15.010', '2020-09-12 07:11:15.010');
INSERT INTO `task` (note, status, contact_id, created_by, assigned_to, dueDate, completedDate, createdAt, updatedAt) VALUES ('Need to finish up the TPS reports, ASAP!', 'OPEN', 10, 1, 1, '2020-10-11', null, '2020-08-02 11:12:18.019', '2020-08-24 17:33:19.011');

-- phones
INSERT INTO `PHONE` (contact_id, phone_number, phones_key) VALUES (2, '9725184400', 'WORK');
INSERT INTO `PHONE` (contact_id, phone_number, phones_key) VALUES (2, '9727722424', 'HOME');
INSERT INTO `PHONE` (contact_id, phone_number, phones_key) VALUES (3, '9726168081', 'MOBILE');