db = db.getSiblingDB('auctiondb');

db.createUser({
    user: 'root',
    pwd: 'toor',
    roles: [
        { role: 'readWrite', db: 'auctionDB' }
    ]
});

db.createCollection('auctions', { capped: false });

db.auctions.insertMany([
    {
        sellerID: "seller1",
        catalogueID: "1", 
        basePrice: 25.0,
        auctionStartTime: new Date("2025-10-20T12:00:00Z"),
        auctionEndTime: new Date("2025-10-27T12:00:00Z"),
        status: true // active
    },
    {
        sellerID: "seller2",
        catalogueID: "2",
        basePrice: 100.0,
        auctionStartTime: new Date("2025-10-21T14:00:00Z"),
        auctionEndTime: new Date("2025-10-28T14:00:00Z"),
        status: true
    },
    {
        sellerID: "seller3",
        catalogueID: "3",
        basePrice: 50.0,
        auctionStartTime: new Date("2025-10-22T16:00:00Z"),
        auctionEndTime: new Date("2025-10-29T16:00:00Z"),
        status: false // closed
    }
]);

db.createCollection('bids', { capped: false });

db.bids.insertMany([
    {
        auctionID: "1",       
        bidderID: "jdoe",     
        amount: 30.0,
        bidTime: new Date("2025-10-21T12:30:00Z")
    },
    {
        auctionID: "1",
        bidderID: "asmith",
        amount: 35.0,
        bidTime: new Date("2025-10-22T09:15:00Z")
    },
    {
        auctionID: "2",
        bidderID: "jdoe",
        amount: 120.0,
        bidTime: new Date("2025-10-23T10:00:00Z")
    },
    {
        auctionID: "3",
        bidderID: "asmith",
        amount: 55.0,
        bidTime: new Date("2025-10-23T16:45:00Z")
    }
]);

