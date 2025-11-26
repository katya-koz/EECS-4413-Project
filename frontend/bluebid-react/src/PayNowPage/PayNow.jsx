import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

// pay now page
function PayNow() {
  const { id } = useParams();
  const [itemPrice, setItemPrice] = useState(0);
  const [shippingCost, setShippingCost] = useState(0);

  const [cardNumber, setCardNumber] = useState("");
  const [cvv, setCvv] = useState("");
  const [expiry, setExpiry] = useState("");

  useEffect(() => {
    // fetch values here, set to temp for now
    setItemPrice(200);
    setShippingCost(25);
  }, [id]);

  const total = itemPrice + shippingCost;
  const navigate = useNavigate();

  const submitPayment = async () => {
    // submit payment info
    // go to reciept page...
  };

  return (
    <div style={{ maxWidth: "400px", margin: "40px auto" }}>
      <h1>Pay for Item {id}</h1>

      <div
        style={{
          border: "1px solid #ddd",
          borderRadius: "8px",
          padding: "15px",
          marginTop: "20px",
          marginBottom: "25px",
          background: "#f9f9f9",
        }}
      >
        <h2>Order Summary</h2>
        <div style={{ marginTop: "10px" }}>
          <p>
            Item Price: <strong>${itemPrice}</strong>
          </p>
          <p>
            Shipping: <strong>${shippingCost}</strong>
          </p>
          <hr />
          <p style={{ fontSize: "18px" }}>
            Total: <strong>${total}</strong>
          </p>
        </div>
      </div>

      <div style={{ display: "flex", flexDirection: "column", gap: "15px" }}>
        <div>
          <label>Card Number</label>
          <input
            type="text"
            placeholder="1234 5678 1234 5678"
            value={cardNumber}
            onChange={(e) => setCardNumber(e.target.value)}
            style={{ width: "100%", padding: "10px", marginTop: "5px" }}
          />
        </div>

        <div>
          <label>Security Code (CVV)</label>
          <input
            type="text"
            placeholder="123"
            value={cvv}
            onChange={(e) => setCvv(e.target.value)}
            style={{ width: "100%", padding: "10px", marginTop: "5px" }}
          />
        </div>

        <div>
          <label>Expiry Date (MM/YY)</label>
          <input
            type="text"
            placeholder="08/27"
            value={expiry}
            onChange={(e) => setExpiry(e.target.value)}
            style={{ width: "100%", padding: "10px", marginTop: "5px" }}
          />
        </div>

        <button
          onClick={submitPayment}
          style={{ padding: "12px", marginTop: "20px", fontSize: "16px" }}
        >
          Submit Payment
        </button>
      </div>
    </div>
  );
}

export default PayNow;
