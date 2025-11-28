import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import styles from "./PayNow.module.scss";

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
    // navigate(`/receipt/${id}`);
  };

  return (
    <div className={styles.container}>
      <h1>Pay for Item {id}</h1>

      <div className={styles.summary}>
        <h2>Order Summary</h2>
        <div className={styles.summaryDetails}>
          <p>
            Item Price: <strong>${itemPrice}</strong>
          </p>
          <p>
            Shipping: <strong>${shippingCost}</strong>
          </p>
          <hr />
          <p className="total">
            Total: <strong>${total}</strong>
          </p>
        </div>
      </div>

      <div className={styles.form}>
        <div className={styles.field}>
          <label>Card Number</label>
          <input
            className={styles.input}
            type="text"
            placeholder="1234 5678 1234 5678"
            value={cardNumber}
            onChange={(e) => setCardNumber(e.target.value)}
          />
        </div>

        <div className={styles.field}>
          <label>Security Code (CVV)</label>
          <input
            className={styles.input}
            type="text"
            placeholder="123"
            value={cvv}
            onChange={(e) => setCvv(e.target.value)}
          />
        </div>

        <div className={styles.field}>
          <label>Expiry Date (MM/YY)</label>
          <input
            className={styles.input}
            type="text"
            placeholder="08/27"
            value={expiry}
            onChange={(e) => setExpiry(e.target.value)}
          />
        </div>

        <button onClick={submitPayment} className={styles.button}>
          Submit Payment
        </button>
      </div>
    </div>
  );
}

export default PayNow;
