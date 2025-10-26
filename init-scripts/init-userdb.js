db = db.getSiblingDB('userdb');

db.createUser({
    user: 'root',
    pwd: 'toor',
    roles: [
        { role: 'readWrite', db: 'userDB' }
    ]
});

db.createCollection('users', { capped: false });
//customers
db.users.insertMany([
    {
        username: "jdoe",
        password: "hashedpassword1",
        firstName: "John",
        lastName: "Doe",
        userType: "CUSTOMER",
        email: "jdoe@example.com",
        dateRegistered: new Date("2025-10-01T10:00:00Z"),
        streetName: "Maple Street",
        streetNum: "123",
        city: "Toronto",
        postalCode: "M5V1E3",
        country: "Canada"
    },
    {
        username: "asmith",
        password: "hashedpassword2",
        firstName: "Alice",
        lastName: "Smith",
        userType: "CUSTOMER",
        email: "asmith@example.com",
        dateRegistered: new Date("2025-10-05T15:30:00Z"),
        streetName: "Oak Avenue",
        streetNum: "45",
        city: "Montreal",
        postalCode: "H2X1T4",
        country: "Canada"
    }
]);
// sellers
db.users.insertMany([
    {
        username: "seller1",
        password: "hashedpassword3",
        firstName: "Bob",
        lastName: "Builder",
        userType: "SELLER",
        email: "bob.builder@example.com",
        dateRegistered: new Date("2025-09-20T09:00:00Z")
    },
    {
        username: "seller2",
        password: "hashedpassword4",
        firstName: "Carol",
        lastName: "White",
        userType: "SELLER",
        email: "carol.white@example.com",
        dateRegistered: new Date("2025-09-25T12:15:00Z")
    }
]);
