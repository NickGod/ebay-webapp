#drop any table existing

mysql TEST < drop.sql

#Build and run your parser to generate fresh load files.

ant run-all

#sort the data in the files

sort -u user.csv -o user.csv
sort -u seller.csv -o seller.csv
sort -u item.csv -o item.csv
sort -u cate.csv -o cate.csv
sort -u bid.csv -o bid.csv

#Load the data into MySQL.

mysql TEST < create.sql
mysql TEST < load.sql

#Delete all temporary files

rm *.csv



