ALTER TABLE Bill 
ADD CONSTRAINT fk_cust_id FOREIGN KEY(cust_id) REFERENCES Customer(cust_id);

ALTER TABLE Bill 
ADD CONSTRAINT fk_shoe_id FOREIGN KEY(shoe_id) REFERENCES Shoes(shoe_id);
