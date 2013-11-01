Student 1:
Name: Chan Young Kim
SID: 503900335

Student 2:
Name: Zhongtai David Sun
SID: 903909635

Relational Database Schema Design
Part B
1.
Item
(itemID, name, currently, buyPrice, firstBid, numberOfBids, started, ends, sellerID, description)
Primary Key: itemID

Category
(itemID, category)

User
(userID, rating, location, country)
Primary Key: userID

Bid
(itemID, userID, time, amount)

2.
Item:
itemID -> name, currently, buyPrice, firstBid, numberOfBids, started, ends, sellerID, description

Category:

User:
userID -> rating, location, country

Bid:
itemID, userID, time -> amount

3.
Yes, all our relations are in BCNF.


