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
        shippingCost: 5.0,
        expeditedShippingCost: 10.0,
        itemDescription: "A classic vintage lamp, perfect for your living room.",
        shippingDays: 7,
		isActive: true
    },
    {
        sellerID: "seller2",
        highestBidderID: null,
        itemName: "Antique Vase",
        currentBiddingPrice: 100.0,
        shippingCost: 15.0,
        expeditedShippingCost: 25.0,
        itemDescription: "Beautiful antique vase from the 19th century.",
        shippingDays: 10,
		isActive: true
    },
    {
        sellerID: "seller3",
        highestBidderID: null,
        itemName: "Collector's Coin Set",
        currentBiddingPrice: 50.0,
        shippingCost: 3.0,
        expeditedShippingCost: 8.0,
        itemDescription: "Limited edition coin set, ideal for collectors.",
        shippingDays: 5,
		isActive: true
    },
	{
        sellerID: "seller4",
        highestBidderID: null,
        itemName: "Retro Vinyl Record",
        currentBiddingPrice: 30.0,
        shippingCost: 4.0,
        expeditedShippingCost: 8.0,
        itemDescription: "Classic rock album on vinyl, in excellent condition.",
        shippingDays: 5,
        isActive: true
    },
    {
        sellerID: "seller5",
        highestBidderID: null,
        itemName: "Handcrafted Wooden Bowl",
        currentBiddingPrice: 45.0,
        shippingCost: 6.0,
        expeditedShippingCost: 12.0,
        itemDescription: "Beautifully handcrafted wooden bowl, perfect as a gift.",
        shippingDays: 7,
        isActive: true
    },
    {
        sellerID: "seller6",
        highestBidderID: null,
        itemName: "Vintage Pocket Watch",
        currentBiddingPrice: 150.0,
        shippingCost: 10.0,
        expeditedShippingCost: 20.0,
        itemDescription: "Elegant vintage pocket watch, fully functional and collectible.",
        shippingDays: 10,
        isActive: true
    },
    {
        sellerID: "seller7",
        highestBidderID: null,
        itemName: "Rare Comic Book",
        currentBiddingPrice: 200.0,
        shippingCost: 8.0,
        expeditedShippingCost: 15.0,
        itemDescription: "First edition comic book, mint condition, collector's item.",
        shippingDays: 7,
        isActive: true
    },
    {
        sellerID: "seller8",
        highestBidderID: null,
        itemName: "Decorative Wall Clock",
        currentBiddingPrice: 60.0,
        shippingCost: 5.0,
        expeditedShippingCost: 10.0,
        itemDescription: "Stylish wall clock with unique vintage design.",
        shippingDays: 6,
        isActive: true
    },
    {
        sellerID: "seller9",
        highestBidderID: null,
        itemName: "Antique Silver Spoon Set",
        currentBiddingPrice: 80.0,
        shippingCost: 7.0,
        expeditedShippingCost: 15.0,
        itemDescription: "Set of 6 antique silver spoons, great for collectors.",
        shippingDays: 8,
        isActive: true
    },
    {
        sellerID: "seller10",
        highestBidderID: null,
        itemName: "Classic Board Game",
        currentBiddingPrice: 35.0,
        shippingCost: 3.0,
        expeditedShippingCost: 7.0,
        itemDescription: "Vintage board game in good condition, fun for all ages.",
        shippingDays: 5,
        isActive: true
    }
]);
