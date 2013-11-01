
--Number 1
SELECT COUNT(*) FROM User;

--Number 2
SELECT COUNT(DISTINCT(s.sellerID)) FROM User u, Item s where u.userID = s.sellerID and u.location = "New York" collate latin1_general_cs;

--Number 3
SELECT COUNT(*) FROM (
			SELECT DISTINCT itemID
			FROM Category
			GROUP BY itemID
			HAVING COUNT(*) = 4) as a;
--Number 4
SELECT DISTINCT i.itemID FROM Item i, Bid b WHERE i.itemID = b.itemID and i.currently = (SELECT MAX(Amount) FROM Bid);

--Number 5
SELECT COUNT(DISTINCT s.sellerID) FROM Item s, User u where s.sellerID = u.userID and u.rating > 1000;

--Number 6
SELECT COUNT(*) FROM (SELECT DISTINCT userID FROM Bid) AS a, (SELECT DISTINCT sellerID FROM Item) AS b WHERE a.userID = b.sellerID;

--Number 7
SELECT COUNT(DISTINCT c.category) fROM Category c, Bid b where c.itemID = b.itemID and b.amount > 100;

