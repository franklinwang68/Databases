CREATE TRIGGER updInStock
ON orderDetails
INSTEAD OF INSERT
AS
BEGIN
    -- Check if there is enough stock for each inserted row
    IF EXISTS (
        SELECT 1
        FROM inserted i
        JOIN supProd s ON i.supID = s.supID AND i.prodID = s.prodID
        WHERE s.inStock < i.quantity
    )
    BEGIN
        -- If there isn't enough stock for at least one row, raise an error
        RAISERROR('Not enough stock for one or more products in the insert operation.', 16, 1);
        ROLLBACK TRANSACTION; -- Optionally rollback the transaction
        RETURN; -- Exit the trigger
    END

    -- Update stock in supProd for the quantities being inserted
    UPDATE s
    SET s.inStock = s.inStock - i.quantity
    FROM supProd s
    JOIN inserted i ON s.supID = i.supID AND s.prodID = i.prodID;

    -- Insert the new order details
    INSERT INTO orderDetails (orderID, supID, prodID, salePrice, quantity)
    SELECT orderID, supID, prodID, salePrice, quantity FROM inserted;
END;

insert into residence (streetNum, street, city, state, zip, phone) values ('15', 'Murray Hill', 'Cleveland', 'OH', '44106', '9735684482');
insert into residence (streetNum, street, city, state, zip, phone) values ('100', 'Euclid', 'Cleveland', 'OH', '44106', '1234567890');
insert into residence (streetNum, street, city, state, zip, phone) values ('7', 'Midhurst', 'Short Hills', 'NJ', '07078', '9178802789');
insert into customer (custName, resID, creditCard, CVV, expDate) 
values ('Alex Holden', (select resID from residence where street = 'Midhurst'), '98377189028', '4472','01-11-2027');
insert into supplier(supName, resID) values ('The Franklin Store', (select resID from residence where street = 'Euclid'));
insert into business (busName, resID) values ('Franklin Inc.', (select resID from residence where street = 'Murray Hill'));
Insert into product (prodName, salePrice) values ('shampoo', 1500);
insert into product (prodName, salePrice) values ('soap', 400);
insert into custOrder (custID, busID, orderDate, shipDate) values
((select custID from customer where custName = 'Alex Holden'), (select busID from business where busName = 'Franklin Inc.'), '11-01-2024', '11-03-2024');
insert into orderDetails (orderID, prodID, supID, salePrice, quantity)
values ((select orderID from custOrder where custID = '1000'), (select prodID from product where prodName = 'shampoo'), (select supID from supplier where supName = 'The Franklin Store'), 1800, 3);
