create table User (ID varchar(60) not null, Rating integer not null, Location varchar(60), Country varchar(60), primary key (ID));

create table Seller (ID varchar(60) not null, Rating integer not null, primary key(ID, Rating));

create table Item (ID integer not null, Name varchar(300) not null, Currently decimal(8,2) not null, Buy_price decimal(8,2) default null, First_bid decimal(8,2) not null, Number_bids integer, Latitude decimal(9,6) default null, Longitude decimal(9,6) default null, Location varchar(60), Country varchar(60), Start timestamp not null, End timestamp not null, SellerID varchar(60) not null, Description varchar(4000), primary key(ID), foreign key(SellerID) references Seller(ID));  

create table Category (ItemID integer not null, Category varchar(100) not null, primary key(ItemID, Category), foreign key(ItemID) references Item(ID));

create table Bid (UserID varchar(60) not null, ItemID integer not null, Amount decimal(8,2) not null, Time timestamp not null, primary key(UserID, Time), foreign key(UserID) references User(ID), foreign key(ItemID) references Item(ID));