create table residence (
	resID int identity(70000, 1),
	streetNum int not null,
	street varchar(20) not null,
	city varchar(20) not null,
	state varchar(20) not null,
	zip varchar(5) not null,
	phone varchar(10),
	primary key (resID)
);

create table customer (
	custID int identity(1000,1),
	custName varchar(20) not null,
	resID int not null,
	creditCard varchar(20) not null,
	CVV varchar(6) not null,
	expDate date not null,
	primary key (custID),
	foreign key (resID) references residence
);

create table business (
	busID int identity(5000, 1),
	busName varchar(20) not null,
	resID int not null,
	primary key (busID),
	foreign key (resID) references residence
);

create table supplier (
	supID int identity(200, 1),
	supName varchar(20) not null,
	resID int not null,
	primary key(supID),
	foreign key (resID) references residence
);

create table product (
	prodID int identity(200000, 1),
	prodName varchar(20) not null,
	salePrice int not null,
	primary key (prodID)
);
create table supProd (
	prodID int not null,
	supID int not null,
	unitPrice int not null,
	inStock int not null,
	primary key (prodID, supID),
	foreign key (prodID) references product,
	foreign key (supID) references supplier
);

create table custOrder(
	orderID int identity(10000, 1),
	custID int not null,
	busID int not null,
	orderDate date not null,
	shipDate date,
	primary key (orderID),
	foreign key (custID) references customer,
	foreign key (busID) references business,
	check (orderDate <= shipDate)
);

create table orderDetails (
	orderID int not null,
	prodID int not null,
	supID int not null,
	salePrice int not null,
	quantity int not null,
	primary key (orderID, prodID),
	foreign key (orderID) references custOrder,
	foreign key (prodID) references product,
	foreign key (supID) references supplier
);

create index residenceZip_index on residence(zip);
