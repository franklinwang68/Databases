CREATE or ALTER procedure cancelOrder
	@orderID as int
as
begin
	begin tran cancelOrder
		delete from orderDetails
		where orderID = @orderID;

		delete FROM custOrder
		WHERE orderID = @orderID;
	commit tran cancelOrder
end;


EXEC cancelOrder @orderID = 10002


GRANT EXECUTE ON OBJECT::cancelOrder TO dbuser


CREATE or ALTER procedure findTotalSales
	@custID as int,
	@busID as int
as
begin
	begin tran findTotalSales
		select custOrder.custID, sum(salePrice*quantity) as 'totalSales'
		from custOrder join customer on customer.custID = custORder.custID
		join business on custOrder.busID = business.busID
		join orderDetails on custOrder.orderID = orderDetails.orderID
		where custOrder.custID = @custID and custOrder.busID = @busID
		group by custOrder.custID
	commit tran findTotalSales
end;


EXEC findTotalSales @custID = 1000, @busID = 5000


GRANT EXECUTE ON OBJECT::findTotalSales TO dbuser

