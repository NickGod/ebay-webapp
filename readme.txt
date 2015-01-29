create table User (ID varchar(60) not null, Rating integer not null, Location varchar(60), Country varchar(60), primary key (ID));

create table Seller (ID varchar(60) not null, Rating integer not null, primary key(ID, Rating));

create table Item (ID integer not null, Name varchar(300) not null, Currently decimal(8,2) not null, Buy_price decimal(8,2) default null, First_bid decimal(8,2) not null, Number_bids integer, Latitude decimal(9,6) default null, Longitude decimal(9,6) default null, Location varchar(60), Country varchar(60), Start timestamp not null, End timestamp not null, SellerID varchar(60) not null, Description varchar(4000), primary key(ID), foreign key(SellerID) references Seller(ID));  

create table Category (ItemID integer not null, Category varchar(100) not null, primary key(ItemID, Category), foreign key(ItemID) references Item(ID));

create table Bid (UserID varchar(60) not null, ItemID integer not null, Amount decimal(8,2) not null, Time timestamp not null, primary key(UserID, Time), foreign key(UserID) references User(ID), foreign key(ItemID) references Item(ID));
1. Relations:

User(UserID, Rating, Location, Country)
Primary Key: UserID

Seller(UserID, Rating)
Primary Key: UserID, Rating

Category(ItemID, Category)
Primary Key: ItemID(Foreign Key), Category

Bids(UserID, ItemID, amount, bidtime)
Primary Key: UserID, bidtime

Item(ItemID, Currently, Buy_Price, First_bid, Number_of_Bids, started, ends, UserID, description)
Primary Key: ItemID

2. Non-trivial functional dependencies:

(Note that except nontrivial functional dependencies involved with keys, there are no other ones.)
For User:
UserID -> Rating

For Seller:
None

For Category:
ItemId -> Category

For Bids:
UserID, bidtime -> ItemID
UserID, bidtime -> amount

For Item:
ItemID -> Currently, Buy_Price, First_bid, Number_of_Bids, started, ends, UserID, description

3. Is this design in BCNF form?
	BCNF: For any nontrivial FD X->Y, X must contain a key.
	Obviously this schema design satisfies BCNF rule.

4. Is this design in 4NF form?
	4NF: For any nontrivial Multivalued dependency X->->Y, X must contain a key.

	Does our design have any multivalued dependency?

	No. Since we have unique keys for all these relations, there won't be any multivalued dependency.

	THus this design is in 4NF, because there is nothing more to reduce after applying BCNF rule.

	
