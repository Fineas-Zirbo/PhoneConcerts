-- drop database db_pco;
create database db_pco;
-- drop user springuser@'%';
create user 'springuser'@'%' identified by ':Th3@S3cr3t!'; -- Creates the user
grant all on db_pco.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
