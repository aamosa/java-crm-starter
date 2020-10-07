-- contacts
INSERT INTO `contact` (firstName, lastName, email, status, updatedAt, createdAt) VALUES ('LEONARD','HOFSTANDER','leonard@google.com','ACTIVE', '2019-02-10 20:11:13.144', '2019-02-06 07:12:22.161');
INSERT INTO `contact` (firstName, lastName, email, status, updatedAt, createdAt) VALUES ('RAJESH','KOOTHRAPPALI','rajr@google.com','INACTIVE', '2020-02-03 15:11:13.144', '2019-12-03 08:12:31.110');
INSERT INTO `contact` (firstName, lastName, email, status, updatedAt, createdAt) VALUES ('HOWARD','WOLOWITZ','howardw@example.com','ACTIVE', '2019-01-22 09:51:42.324', '2019-01-21 04:11:21.224');
INSERT INTO `contact` (firstName, lastName, email, status, updatedAt, createdAt) VALUES ('WILL','WHEATON','w.wheaton101@gmail.com','ACTIVE', '2020-04-01 18:22:01.141', '2019-04-01 13:46:12.301');
INSERT INTO `contact` (firstName, lastName, email, status, updatedAt, createdAt) VALUES ('SHELDON','COOPER','sheldon@google.com','DISABLED', '2019-02-05 20:12:12.150', '2019-02-02 19:22:52.124');
INSERT INTO `contact` (firstName, lastName, email, status, updatedAt, createdAt) VALUES ('AMY','FOWLER','amyfowler@gmail.com','ACTIVE', '2020-03-23 18:22:01.141', '2019-03-15 11:43:10.110');

-- address
INSERT INTO `address` (contact_id,  address1, city, postalCode, stateProvince, country, countyCode) VALUES (1,'5168 ELECTRON BLVD','PASADENA','90103','CA','United States','USA');
INSERT INTO `address` (contact_id,  address1, city, postalCode, stateProvince, country, countyCode) VALUES (1,'446 EINSTEIN WAY','SAN JOSE','90014','CA','United States','USA');

-- users
INSERT INTO `user` (firstName, lastName, email, userName, password, updatedAt, createdAt) VALUES ('JOHN', 'ROSS', 'jross@crm.com', 'JROSS', 'password2', '2019-12-23 10:26:21.001', '2019-12-01 15:11:09.003');
INSERT INTO `user` (firstName, lastName, email, userName, password, updatedAt, createdAt) VALUES ('JOE', 'GUMBI', 'jgumbi@crm.com', 'JGUMBI', 'password1', '2020-01-05 11:16:25.421', '2019-12-05 11:01:19.109');

-- roles
INSERT INTO `role` (roleName, updatedAt, createdAt) VALUES ('ADMIN', '2019-02-19 12:19:17.000', '2019-01-01 07:10:09.003');
INSERT INTO `role` (roleName, updatedAt, createdAt) VALUES ('READ_ONLY', '2019-02-21 11:04:22.001', '2019-01-02 09:10:09.005');
INSERT INTO `role` (roleName, updatedAt, createdAt) VALUES ('READ_WRITE', '2019-02-22 09:26:11.009', '2019-01-02 10:30:12.007');

-- tasks
INSERT INTO `task` (note, status, contact_id, created_by, assigned_to, dueDate, completedDate, createdAt, updatedAt) VALUES ('Is it Friday yet?', 'OPEN', 2, 2, 2, '2020-10-05', null, '2020-09-12 07:11:15.010', '2020-09-12 07:11:15.010');
INSERT INTO `task` (note, status, contact_id, created_by, assigned_to, dueDate, completedDate, createdAt, updatedAt) VALUES ('Need to finish up the TPS reports, ASAP!', 'OPEN', 1, 1, 1, '2020-10-11', null, '2020-08-02 11:12:18.019', '2020-08-24 17:33:19.011');

-- phones
INSERT INTO `PHONE` (contact_id, phone_number, phones_key) VALUES (1, '9195551236', 'MOBILE');
INSERT INTO `PHONE` (contact_id, phone_number, phones_key) VALUES (2, '9725184400', 'WORK');
INSERT INTO `PHONE` (contact_id, phone_number, phones_key) VALUES (2, '9727722424', 'HOME');
INSERT INTO `PHONE` (contact_id, phone_number, phones_key) VALUES (2, '9727723350', 'MOBILE');
INSERT INTO `PHONE` (contact_id, phone_number, phones_key) VALUES (3, '9726168081', 'MOBILE');