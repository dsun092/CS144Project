CREATE INDEX IndexOnSeller ON Item(sellerID);
CREATE INDEX IndexOnBuyPrice ON Item(buyPrice);
CREATE INDEX IndexOnBidder ON Bid(userID);
CREATE INDEX IndexOnEnds ON Item(ends);
