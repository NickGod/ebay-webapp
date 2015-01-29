
select count(*) from (select ID from User union select ID from Seller) a;

select count(*) from Item where Binary Location = 'New York';

select count(*) from (select ItemID from Category group by ItemID having count(*)=4) a;

select ID from Item where ID = (select ID from Item where Number_bids > 0 order by Currently desc limit 1) and End > '2001-12-20 00:00:01';

select count(*) from Seller where Rating > 1000;

select count(*) from User inner join Seller on User.ID = Seller.ID;

select count(distinct Category) from Category where ItemID in (select ID from Item where Currently > 100 and Number_bids > 0);