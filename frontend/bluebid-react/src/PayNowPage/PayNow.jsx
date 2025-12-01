import { useParams, useNavigate, useLocation } from "react-router-dom";
import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import "./PayNow.scss";

function PayNow() {
  const { id } = useParams();
  const location = useLocation();
  const [itemPrice, setItemPrice] = useState(0);
  const [itemName, setItemName] = useState("");
  const [paymentError, setPaymentError] = useState("");

  const [shippingCost, setShippingCost] = useState(0);
  const [standardShippingCost, setStandardShippingCost] = useState(0);
  const [expeditedShippingCost, setExpeditedShippingCost] = useState(0);

  const [standardShippingDays, setStandardShippingDays] = useState(0);
  const [expeditedShippingDays, setExpeditedShippingDays] = useState(0);

  const [loading, setLoading] = useState(true);
  const [sellerID, setSellerID] = useState("");
  const [catalogueID, setCatalogueID] = useState("");
  const [isExpedited, setIsExpedited] = useState(false);

  const [cardNumber, setCardNumber] = useState("");
  const [cvv, setCvv] = useState("");
  const [expiryMonth, setExpiryMonth] = useState("01");
  const [expiryYear, setExpiryYear] = useState(
    new Date().getFullYear().toString()
  );

  const [isWinner, setIsWinner] = useState(true);

  const { authFetch, user } = useUser();
  const navigate = useNavigate();

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

  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 10 }, (_, i) =>
    (currentYear + i).toString()
  );

  useEffect(() => {
    async function getAuctionDetails() {
      try {
        const response = await authFetch(
          `http://localhost:8080/api/auction/auctions/${id}`
        );

        if (response.ok) {
          const data = await response.json();
          console.log(data);

          setItemPrice(data.itemPrice || itemPrice);
          setSellerID(data.sellerID);
          setCatalogueID(data.catalogueID);
          setItemName(data.itemName);

          if (data.catalogueID) {
            await fetchCatalogueItem(data.catalogueID);
          }
        }
      } catch (error) {
        console.log("failed to fetch auction item");
      } finally {
        setLoading(false);
      }
    }

    async function fetchCatalogueItem(catalogueID) {
      try {
        const res = await authFetch(
          `http://localhost:8080/api/catalogue/items/${catalogueID}`
        );

        if (res.ok) {
          const catData = await res.json();

          setStandardShippingCost(catData.shippingCost);
          setExpeditedShippingCost(catData.expeditedShippingCost);

          setStandardShippingDays(catData.shippingDays);
          setExpeditedShippingDays(catData.expeditedShippingDays);

          setShippingCost(catData.shippingCost);
          setItemPrice(catData.currentBiddingPrice);
          setIsExpedited(false);
          const isWinner =
            catData.highestBidderID === user.userid ? true : false;
          setIsWinner(isWinner);
        }
      } catch (err) {
        console.log("Failed to fetch catalogue item");
      }
    }

    if (id) {
      getAuctionDetails();
    }
  }, [id]);

  const total = itemPrice + shippingCost;

  const submitPayment = async () => {
    setPaymentError(""); // clear old errors

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
      //paymentTime: new Date().toISOString().slice(0, 19),
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

      const responseData = await response.json();
      console.log("PAYMENT RESPONSE:", responseData);

      if (responseData.paymentSuccess) {
        const paymentID = responseData.paymentId;

        setPaymentError("");

        navigate(`/receipt/${paymentID}`, { state: { itemName: itemName } });
      } else {
        setPaymentError(
          responseData.message || "Payment failed. Please try again."
        );
      }
    } catch (error) {
      console.error("Payment error:", error);
      setPaymentError("A network or server error occurred.");
    }
  };

  if (loading)
    return (
      <p style={{ textAlign: "center", marginTop: "50px" }}>
        Loading payment details...
      </p>
    );
  if (!isWinner)
    return (
      <div style={styles.pageContainer}>
        <div style={styles.card}>
          <h1 style={styles.heading}>{itemName || "Item"}</h1>
          <p
            style={{
              color: "#d9534f",
              textAlign: "center",
              fontWeight: "600",
              marginTop: "30px",
            }}
          >
            You are not the winner of this auction and cannot make a payment.
          </p>
        </div>
      </div>
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

          <label style={styles.radioLabel}>
            <input
              type="radio"
              name="shipping"
              checked={!isExpedited}
              onChange={() => {
                setIsExpedited(false);
                setShippingCost(standardShippingCost);
              }}
              style={styles.radio}
            />
            <div>
              <strong>Standard Shipping</strong> — ${standardShippingCost}
              <div style={{ fontSize: "13px", color: "#666" }}>
                {standardShippingDays} days
              </div>
            </div>
          </label>

          <label style={styles.radioLabel}>
            <input
              type="radio"
              name="shipping"
              checked={isExpedited}
              onChange={() => {
                setIsExpedited(true);
                setShippingCost(expeditedShippingCost);
              }}
              style={styles.radio}
            />
            <div>
              <strong>Expedited Shipping</strong> — ${expeditedShippingCost}
              <div style={{ fontSize: "13px", color: "#666" }}>
                {expeditedShippingDays} days
              </div>
            </div>
          </label>

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

          {paymentError && (
            <p
              style={{
                marginTop: "10px",
                color: "#d9534f",
                textAlign: "center",
                fontWeight: "500",
              }}
            >
              {paymentError}
            </p>
          )}
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
  shippingContainer: {
    backgroundColor: "#fff",
    border: "1px solid #e1e4e8",
    borderRadius: "8px",
    padding: "15px",
    margin: "15px 0",
  },
  radioLabel: {
    display: "flex",
    alignItems: "flex-start",
    gap: "10px",
    marginBottom: "12px",
    cursor: "pointer",
  },
  radio: {
    marginTop: "4px",
    cursor: "pointer",
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
