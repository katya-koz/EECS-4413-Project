import { useParams, useNavigate, useLocation } from "react-router-dom";
import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import "./PayNow.scss";

function PayNow() {
  const { id } = useParams();
  const location = useLocation();
  const passedPrice = location.state?.finalPrice || 0;
  const [itemPrice, setItemPrice] = useState(passedPrice);
  const [itemName, setItemName] = useState("");

  const [shippingCost, setShippingCost] = useState(5);
  const [loading, setLoading] = useState(true);
  const [sellerID, setSellerID] = useState("");
  const [catalogueID, setCatalogueID] = useState("");
  const [isExpedited, setIsExpidited] = useState(false);

  const [cardNumber, setCardNumber] = useState("");
  const [cvv, setCvv] = useState("");
  const [expiryMonth, setExpiryMonth] = useState("01");
  const [expiryYear, setExpiryYear] = useState(
    new Date().getFullYear().toString()
  );
  const [checked, setChecked] = useState(false);
  const validMonths = [
    "01",
    "02",
    "03",
    "04",
    "05",
    "06",
    "07",
    "08",
    "09",
    "10",
    "11",
    "12",
  ];
  const { authFetch } = useUser();

  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 10 }, (_, i) =>
    (currentYear + i).toString()
  );

  const handleChange = (e) => {
    const isChecked = e.target.checked;
    setChecked(isChecked);

    if (isChecked) {
      setShippingCost(10);
      setIsExpidited(true);
    } else {
      setShippingCost(5);
      setIsExpidited(false);
    }
  };

  useEffect(() => {
    async function getAuctionDetails() {
      try {
        const response = await authFetch(
          `http://localhost:8080/api/auction/auctions/${id}`
        );

        if (response.ok) {
          const data = await response.json();
          console.log("AUCTION DATA:", data);
          setItemPrice(data.itemPrice || itemPrice);
          setSellerID(data.sellerID);
          setCatalogueID(data.catalogueID);
          setItemName(data.itemName);
        }
      } catch (error) {
        console.log("failed to fetch auction item");
      } finally {
        setLoading(false);
      }
    }

    if (id) {
      getAuctionDetails();
    }
  }, [id]);

  const total = itemPrice + shippingCost;
  const navigate = useNavigate();

  const submitPayment = async () => {
    const paymentRequest = {
      cardNumber: cardNumber,
      expiryMonth: expiryMonth,
      expiryYear: expiryYear,
      securityCode: cvv,
      itemPrice: itemPrice,
      sellerID: sellerID,
      catalogueID: catalogueID,
      isExpedited: isExpedited,
      shippingCost: shippingCost,
      paymentTime: new Date().toISOString().slice(0, 19),
    };

    try {
      const response = await authFetch(
        "http://localhost:8080/api/payment/payment",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(paymentRequest),
        }
      );

      if (response.ok) {
        const responseData = await response.json();
        const paymentID = responseData.paymentId;
        navigate(`/receipt/${paymentID}`, { state: { itemName: itemName } });
      } else {
        alert("Payment failed. Please check your details and try again!");
      }
    } catch (error) {
      console.error("Payment error: ", error);
    }
  };

  if (loading)
    return (
      <p style={{ textAlign: "center", marginTop: "50px" }}>
        Loading payment details...
      </p>
    );

  return (
    <div style={styles.pageContainer}>
      <div style={styles.card}>
        <h1 style={styles.heading}>Pay for {itemName || "Item"}</h1>

        <div style={styles.summaryBox}>
          <h2 style={styles.subHeading}>Order Summary</h2>

          <div style={styles.summaryRow}>
            <span>Item Price:</span>
            <strong>${itemPrice.toFixed(2)}</strong>
          </div>

          <div style={styles.checkboxContainer}>
            <label style={styles.checkboxLabel}>
              <input
                type="checkbox"
                checked={checked}
                onChange={handleChange}
                style={{ marginRight: "10px", accentColor: "#007bff" }}
              />
              Expedited Shipping (Add $5.00)
            </label>
          </div>

          <div style={styles.summaryRow}>
            <span>Shipping:</span>
            <strong>${shippingCost.toFixed(2)}</strong>
          </div>

          <div style={styles.totalRow}>
            <span>Total:</span>
            <span>${total.toFixed(2)}</span>
          </div>
        </div>

        <div style={styles.formContainer}>
          <div style={styles.formGroup}>
            <label style={styles.label}>Card Number</label>
            <input
              type="text"
              placeholder="1234 5678 1234 5678"
              value={cardNumber}
              onChange={(e) => setCardNumber(e.target.value)}
              style={styles.input}
            />
          </div>

          <div style={styles.formGroup}>
            <label style={styles.label}>Security Code (CVV)</label>
            <input
              type="text"
              placeholder="123"
              value={cvv}
              onChange={(e) => setCvv(e.target.value)}
              style={styles.input}
            />
          </div>

          <div style={styles.formGroup}>
            <label style={styles.label}>Expiry Date</label>
            <div style={styles.row}>
              <select
                value={expiryMonth}
                onChange={(e) => setExpiryMonth(e.target.value)}
                style={styles.select}
              >
                {validMonths.map((m) => (
                  <option key={m} value={m}>
                    {m}
                  </option>
                ))}
              </select>

              <select
                value={expiryYear}
                onChange={(e) => setExpiryYear(e.target.value)}
                style={styles.select}
              >
                {years.map((y) => (
                  <option key={y} value={y}>
                    {y}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <button
            onClick={submitPayment}
            style={styles.button}
            onMouseOver={(e) => (e.target.style.backgroundColor = "#0056b3")}
            onMouseOut={(e) => (e.target.style.backgroundColor = "#007bff")}
          >
            Submit Payment
          </button>
        </div>
      </div>
    </div>
  );
}

const styles = {
  pageContainer: {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    paddingTop: "60px",
    paddingBottom: "60px",
  },
  card: {
    width: "100%",
    maxWidth: "450px",
    backgroundColor: "#fff",
    borderRadius: "12px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    padding: "30px",
    border: "1px solid #e1e4e8",
    height: "fit-content",
  },
  heading: {
    textAlign: "center",
    color: "#2c3e50",
    marginBottom: "20px",
    marginTop: "0",
    fontSize: "24px",
  },
  subHeading: {
    fontSize: "18px",
    color: "#34495e",
    marginTop: "0",
    marginBottom: "15px",
    borderBottom: "1px solid #e1e4e8",
    paddingBottom: "10px",
  },
  summaryBox: {
    backgroundColor: "#f8f9fa",
    border: "1px solid #e9ecef",
    borderRadius: "8px",
    padding: "20px",
    marginBottom: "25px",
  },
  summaryRow: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "12px",
    color: "#555",
    fontSize: "15px",
  },
  checkboxContainer: {
    margin: "15px 0",
    padding: "10px",
    backgroundColor: "#fff",
    borderRadius: "6px",
    border: "1px solid #e1e4e8",
  },
  checkboxLabel: {
    display: "flex",
    alignItems: "center",
    cursor: "pointer",
    fontSize: "14px",
    color: "#555",
    fontWeight: "500",
  },
  totalRow: {
    display: "flex",
    justifyContent: "space-between",
    marginTop: "15px",
    paddingTop: "15px",
    borderTop: "2px solid #e9ecef",
    fontWeight: "bold",
    fontSize: "18px",
    color: "#2c3e50",
  },
  formContainer: {
    display: "flex",
    flexDirection: "column",
    gap: "5px",
  },
  formGroup: {
    marginBottom: "15px",
  },
  label: {
    display: "block",
    marginBottom: "8px",
    fontWeight: "600",
    color: "#34495e",
    fontSize: "14px",
  },
  input: {
    width: "100%",
    padding: "12px",
    borderRadius: "6px",
    border: "1px solid #ced4da",
    fontSize: "16px",
    boxSizing: "border-box",
  },
  select: {
    width: "50%",
    padding: "12px",
    borderRadius: "6px",
    border: "1px solid #ced4da",
    fontSize: "16px",
    backgroundColor: "#fff",
  },
  row: {
    display: "flex",
    gap: "15px",
  },
  button: {
    width: "100%",
    padding: "14px",
    backgroundColor: "#007bff",
    color: "white",
    border: "none",
    borderRadius: "8px",
    fontSize: "16px",
    fontWeight: "bold",
    cursor: "pointer",
    marginTop: "10px",
    transition: "background-color 0.2s",
  },
};

export default PayNow;
