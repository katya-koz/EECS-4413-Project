import requests
import random
import time

URL = "http://localhost:8080/api/auction/new-auction"
TOKEN = ""  # replace with valid jwt login token

# list of item names (generated with the help of chatgpt)
ITEM_NAMES = [
    "Laptop", "Smart Watch", "Headphones", "Camera", "Bicycle",
    "Backpack", "Desk Lamp", "Gaming Chair", "Keyboard", "Mouse", "Vintage Monkey Lamp", "Artisinal Jam Collection",
    "Retro Vinyl Record", "Bluetooth Speaker", "Antique Pocket Watch",
    "Handcrafted Wooden Bowl", "Designer Sunglasses", "Electric Scooter",
    "Limited Edition Figurine", "Collectible Comic Book", "Smart Thermostat",
    "Luxury Fountain Pen", "Ceramic Tea Set", "Miniature Bonsai Tree",
    "Wireless Charger", "LED Desk Organizer", "Noise-Cancelling Earbuds",
    "Vintage Board Game", "Retro Camera Lens", "Handmade Leather Wallet",
    "3D Puzzle Set", "Premium Coffee Beans", "Silk Scarf",
    "Wooden Chess Set", "Decorative Wall Clock", "Smart Light Bulb",
    "Vintage Typewriter", "Eco-friendly Water Bottle", "Puzzle Box",
    "Retro Game Console", "Handcrafted Candle Set", "Solar Powered Lantern",
    "Mini Drone", "Custom Phone Case", "Art Print Poster", "Collector’s Mug",
    "Gourmet Chocolate Box", "Decorative Plant Pot", "Smart Door Lock",
    "Antique Globe", "Hand-painted Ceramic Vase", "Retro Alarm Clock", "Designer Backpack",
    "Wireless Headset", "Miniature Sculpture", "Collector’s Coin Set", "Luxury Scented Candle",
    "Vintage Leather Journal", "Bluetooth Beanie Hat", "Retro Wall Art", "Portable Projector",
    "Handcrafted Cutting Board", "Custom Skateboard Deck", "Smart Fitness Tracker",
    "Artisanal Soap Collection", "Handmade Quilt", "Vintage Wine Bottle", "Luxury Watch Strap",
    "Decorative Mirror", "Custom Puzzle", "Retro Radio", "Miniature Guitar Model",
    "LED Desk Lamp", "Eco-friendly Notebook", "Vintage Camera Tripod", "Collector’s Pin Set",
    "Smart Plant Sensor", "Hand-painted Tote Bag", "Art Print Calendar", "Luxury Tea Sampler",
    "Retro Soda Crate", "Wooden Jewelry Box", "Solar Desk Clock", "Custom Engraved Pen",
    "Handmade Coasters", "Limited Edition Figurine Set", "Retro Gaming Controller",
    "Ceramic Plant Holder", "Bluetooth Record Player", "Handcrafted Wooden Toy", "Miniature Book Collection",
    "Vintage Poster", "Smart Mug", "Leather Coin Purse", "Antique Lantern", "Custom Phone Stand",
    "Artisanal Cheese Board", "Decorative Throw Pillow", "Mini Puzzle Game",
     "Vintage Pocket Radio", "Handmade Ceramic Mug", "Retro Wall Clock", "Custom Leather Keychain",
    "Portable Bluetooth Speaker", "Miniature Train Set", "Antique Book Collection", "Luxury Candle Holder",
    "Handcrafted Wooden Chair", "Designer Notebook", "Vintage Film Camera", "Smart Home Hub",
    "Artisanal Chocolate Box", "Decorative Ceramic Plate", "Retro Video Game Cartridge", "LED String Lights",
    "Custom Painted Sneakers", "Miniature Dollhouse", "Vintage Globe Lamp", "Eco-friendly Lunch Box",
    "Collector’s Stamps", "Handmade Silk Tie", "Luxury Bathrobe", "Retro Jukebox Model",
    "Wooden Puzzle Box", "Bluetooth Headphones", "Antique Wall Mirror", "Custom Embroidered Pillow",
    "Miniature Bonsai Kit", "Decorative Metal Sculpture", "Smart Thermometer", "Vintage Vinyl Turntable",
    "Art Print Canvas", "Handcrafted Jewelry Set", "Retro Desk Fan", "Luxury Pen Set",
    "Miniature Model Airplane", "Custom Coffee Mug", "Antique Music Box", "LED Desk Organizer",
    "Handmade Wooden Coaster Set", "Retro Arcade Machine", "Portable Power Bank", "Designer Sunglasses Case",
    "Eco-friendly Travel Mug", "Miniature Bookends", "Luxury Soap Set", "Custom Wall Art", "Hand-painted Vase"
    "Vintage Travel Trunk", "Handcrafted Wooden Box", "Retro Metal Lamp", "Custom Engraved Necklace",
    "Portable Mini Fridge", "Miniature Chess Set", "Antique Oil Painting", "Luxury Throw Blanket",
    "Handmade Leather Satchel", "Designer Wall Clock", "Vintage Film Reel", "Smart Light Switch",
    "Artisanal Honey Jar", "Decorative Ceramic Bowl", "Retro Typewriter", "LED Night Light",
    "Custom Painted Canvas", "Miniature Doll Figurine", "Vintage Compass", "Eco-friendly Tote Bag",
    "Collector’s Medal Set", "Handmade Wool Scarf", "Luxury Desk Organizer", "Retro Pinball Machine",
    "Wooden Jewelry Organizer", "Bluetooth Smartwatch", "Antique Candle Holder", "Custom Embroidered Apron",
    "Miniature Terrarium", "Decorative Picture Frame", "Smart Doorbell", "Vintage Record Box",
    "Art Print Poster Set", "Handcrafted Wooden Puzzle", "Retro Radio Receiver", "Luxury Cosmetic Bag",
    "Miniature Airship Model", "Custom Coffee Table Book", "Antique Silver Spoon", "LED Floor Lamp",
    "Handmade Wooden Tray", "Retro Arcade Button Set", "Portable Bluetooth Turntable", "Designer Key Holder",
    "Eco-friendly Notebook Set", "Miniature Garden Kit", "Luxury Leather Belt", "Custom Wall Clock", "Hand-painted Mug Set"



]

# auction setings
MIN_BASE_PRICE = 10.0
MAX_BASE_PRICE = 1000.0
MIN_DURATION_SEC = 60 * 24  # 1 day
MAX_DURATION_SEC = 10 * 24 * 60 * 60  # 10 days in seconds

NUM_AUCTIONS = 50

# posting auction
def post_auction(item_name):
    base_price = round(random.uniform(MIN_BASE_PRICE, MAX_BASE_PRICE), 2)
    duration_sec = random.randint(MIN_DURATION_SEC, MAX_DURATION_SEC)
    description = f"This is the description for {item_name}."

    payload = {
        "itemName": item_name,
        "itemDescription": description,
        "basePrice": base_price,
        "seconds": duration_sec
    }

    headers = {
        "Authorization": f"Bearer {TOKEN}",
        "Content-Type": "application/json"
    }

    response = requests.post(URL, json=payload, headers=headers)
    if response.ok:
        print(f"Successfully posted auction: {item_name}, Price: {base_price}, Duration: {duration_sec}s")
    else:
        print(f"Failed to post auction: {item_name}, Status: {response.status_code}, Response: {response.text}")

for i in range(NUM_AUCTIONS):
    item_name = random.choice(ITEM_NAMES)
    post_auction(item_name)
    time.sleep(0.1)  # short delay to avoid overloading the server
