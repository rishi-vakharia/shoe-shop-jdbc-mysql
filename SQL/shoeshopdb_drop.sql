ALTER TABLE Bill 
DROP FOREIGN KEY fk_cust_id;

ALTER TABLE Bill 
DROP FOREIGN KEY fk_shoe_id;

drop table Bill;
drop table Customer;
drop table Employee;
drop table Shoes;

drop database shoeshopdb;
