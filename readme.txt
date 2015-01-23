1. Relations:

User(UserID, Rating, Location, Country)
Primary Key: UserID

Category(ItemID, Category)
Primary Key: ItemID(Foreign Key), Category

Bids(UserID, ItemID, amount, bidtime)
Primary Key: UserID, bidtime

Item(ItemID, Currently, Buy_Price, First_bid, Number_of_Bids, started, ends, UserID, description)
Primary Key: ItemID

2. Non-trivial functional dependencies:

For User:

User -> Rating

For Category:
ItemId -> Category

For Bids:
UserID, bidtime -> ItemID
UserID, bidtime -> amount

For Item:
ItemID -> Currently, Buy_Price, First_bid, Number_of_Bids, started, ends, UserID, description