db = db.getSiblingDB('cataloguedb');

db.createUser({
    user: 'root',
    pwd: 'secret',
    roles: [
        { role: 'readWrite', db: 'catalogueDB' }
    ]
});

db.createCollection('items', { capped: false });

db.items.insertMany([
    {
        sellerID: "seller1",
        highestBidderID: null,
        itemName: "Vintage Lamp",
        currentBiddingPrice: 25.0,
        auctionType: "English",
        auctionEndTime: new Date("2025-11-01T20:00:00Z"),
        shippingCost: 5.0,
        expeditedShippingCost: 10.0,
        itemDescription: "A classic vintage lamp, perfect for your living room.",
        shippingDays: 7
    },
    {
        sellerID: "seller2",
        highestBidderID: null,
        itemName: "Antique Vase",
        currentBiddingPrice: 100.0,
        auctionType: "Dutch",
        auctionEndTime: new Date("2025-11-03T20:00:00Z"),
        shippingCost: 15.0,
        expeditedShippingCost: 25.0,
        itemDescription: "Beautiful antique vase from the 19th century.",
        shippingDays: 10
    },
    {
        sellerID: "seller3",
        highestBidderID: null,
        itemName: "Collector's Coin Set",
        currentBiddingPrice: 50.0,
        auctionType: "English",
        auctionEndTime: new Date("2025-11-05T20:00:00Z"),
        shippingCost: 3.0,
        expeditedShippingCost: 8.0,
        itemDescription: "Limited edition coin set, ideal for collectors.",
        shippingDays: 5
    }
]);
