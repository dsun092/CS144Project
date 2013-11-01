CREATE TABLE Item (itemID INT PRIMARY KEY, name TEXT, currently DECIMAL(8,2), buyPrice DECIMAL(8,2), firstBid DECIMAL(8,2), numberOfBids INT, started TIMESTAMP, ends TIMESTAMP, sellerID VARCHAR(255), description VARCHAR(4000));
CREATE TABLE Category (itemID INT, category TEXT);
CREATE TABLE User (userID VARCHAR(255) PRIMARY KEY, rating INT, location TEXT, country TEXT);
CREATE TABLE Bid (itemID INT, userID VARCHAR(255), time TIMESTAMP, amount DECIMAL(8,2));
