db = db.getSiblingDB('paymentdb');

db.createUser({
    user: 'root',
    pwd: 'toor',
    roles: [
        { role: 'readWrite', db: 'paymentDB' }
    ]
});

db.createCollection('receipts', { capped: false });
