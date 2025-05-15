create or alter procedure updateCustomerResidence
	@custID as int,
	@streetNum as varchar(20),
	@street as varchar(20),
	@city as varchar(20),
	@state as varchar(20),
	@zip as varchar(5),
	@phone as varchar(10)
as
begin
	begin tran tranUpdCustRes
		update residence set streetNum = @streetNum, street = @street, city = @city, state=@state, zip=@zip, phone=@phone
		where (resID = (select resID from customer where custID = @custID));
	commit tran tranUpdCustRes;
end;

create or alter procedure insertCustOrder
	@custID as int,
	@busID as int,
	@orderDate as date,
	@shipDate as date,
	@ID int OUTPUT
as
	begin
		insert into custOrder (custID, busID, orderDate, shipDate)
		values (@custID, @busID, @orderDate, @shipDate);
		set @ID = scope_identity();
	end;

create or alter procedure insertOrderDetails
	@orderID int,
	@prodID int,
	@supID int,
	@salePrice int,
	@quantity int
as
	begin
		insert into orderDetails (orderID, prodID, supID, salePrice, quantity)
		values (@orderID, @prodID, @supID, @salePrice, @quantity);
	end;

CREATE or ALTER procedure insertNewResidence
	@streetNum as int, -- input parameter
	@street as varchar(20), -- input parameter
	@city as varchar(20), -- input parameter
	@state as varchar(20), -- input parameter
	@zip as varchar(5), -- input parameter
	@phone as varchar(20), -- input parameter
	@id int OUTPUT
as
begin
	begin tran insertResidence
		insert into residence (streetNum, street, city, state, zip, phone)
		values (@streetNum, @street, @city, @state, @zip, @phone)
		set @ID = scope_identity()
	commit tran insertResidence
end;

CREATE or ALTER procedure insertNewBusiness
	@name as varchar(20), 
	@resID as int,
	@id int OUTPUT
as
	begin
		begin tran insertBusiness
			insert into business (busName, resID)
			values (@name, @resID)
			set @ID = scope_identity()
		commit tran insertBusiness
	end;

CREATE or ALTER procedure findLocalSupplier
	@custID as int -- input parameter
as
	begin
		begin tran findSup
		SELECT supID
			FROM customer join residence on customer.resID = residence.resID
			join supplier on supplier.resID = residence.resID
			WHERE customer.custID = @custID
		commit tran findSup
	end;

GRANT execute on object::updateCustomerResidence to dbuser;
GRANT execute on object::insertCustOrder to dbuser;
GRANT execute on object::insertOrderDetails to dbuser;
GRANT EXECUTE ON OBJECT::insertNewResidence TO dbuser;
GRANT EXECUTE ON OBJECT::insertNewBusiness TO dbuser;
GRANT EXECUTE ON OBJECT::findLocalSupplier TO dbuser;

