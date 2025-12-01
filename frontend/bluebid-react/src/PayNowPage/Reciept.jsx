import { useNavigate, useParams } from "react-router-dom";
import { useUser } from "../Context/UserContext";
import { useEffect, useState, useRef } from "react";

function Receipt() {
  const { id } = useParams();
  const { authFetch } = useUser();
  const navigate = useNavigate();

  const [receipt, setReceipt] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const pollingRef = useRef(null);

  async function fetchReceipt() {
    try {
      const response = await authFetch(
        `http://localhost:8080/api/payment/receipt/${id}`
      );

      if (response.ok) {
        const data = await response.json();
        setReceipt(data);
      } else {
        setError("Could not load receipt");
      }
    } catch (err) {
      setError("Network error");
    }
  }

  useEffect(() => {
    if (!id) return;

    async function initialLoad() {
      await fetchReceipt();
      setLoading(false);
    }

    initialLoad();
  }, [id]);

  useEffect(() => {
    if (!receipt) return;

    const missingInfo =
      !receipt.buyerCity ||
      !receipt.buyerCountry ||
      !receipt.buyerStreetName ||
      !receipt.buyerPostalCode ||
      !receipt.itemName; // also check for itemName

    if (!missingInfo) {
      if (pollingRef.current) clearInterval(pollingRef.current);
      return;
    }

    pollingRef.current = setInterval(async () => {
      await fetchReceipt();
      setReceipt((newReceipt) => {
        const stillMissing =
          !newReceipt.buyerCity ||
          !newReceipt.buyerCountry ||
          !newReceipt.buyerStreetName ||
          !newReceipt.buyerPostalCode ||
          !newReceipt.itemName;

        if (!stillMissing && pollingRef.current) {
          clearInterval(pollingRef.current);
        }

        return newReceipt;
      });
    }, 4000);

    return () => {
      if (pollingRef.current) clearInterval(pollingRef.current);
    };
  }, [receipt]);

  if (loading)
    return (
      <p style={{ textAlign: "center", marginTop: "50px" }}>
        Loading receipt...
      </p>
    );

  if (error || !receipt) {
    return (
      <div style={styles.pageContainer}>
        <div style={styles.card}>
          <h2 style={{ color: "#d9534f", textAlign: "center" }}>
            No receipt found
          </h2>
          <p style={{ textAlign: "center", color: "#666" }}>ID: {id}</p>
          <p style={{ textAlign: "center", color: "#d9534f" }}>{error}</p>
          <button onClick={() => navigate("/")} style={styles.secondaryButton}>
            Go Home
          </button>
        </div>
      </div>
    );
  }

  const totalPrice = receipt.itemCost + receipt.shippingCost;
  const isFailed = receipt.failureReason != null;

  const shippingReady =
    receipt.buyerCity &&
    receipt.buyerCountry &&
    receipt.buyerStreetName &&
    receipt.buyerPostalCode;

  return (
    <div style={styles.pageContainer}>
      <div style={styles.card}>
        {!isFailed && (
          <div style={styles.iconContainer}>
            <i
              className="bi bi-check-circle-fill"
              style={styles.successIcon}
            ></i>
          </div>
        )}

        {isFailed && (
          <div style={styles.iconContainer}>
            <i className="bi bi-x-circle-fill" style={styles.failureIcon}></i>
          </div>
        )}
        <h1 style={styles.heading}>
          {isFailed ? "Payment Unsuccessful" : "Payment Successful"}
        </h1>

        {isFailed && (
          <p
            style={{
              color: "#d9534f",
              marginBottom: "20px",
              fontWeight: "600",
            }}
          >
            {receipt.failureReason}
          </p>
        )}

        <div style={styles.detailsBox}>
          <div style={styles.row}>
            <span style={styles.label}>Item</span>
            <span style={styles.value}>
              {receipt.itemName || "Fetching item name..."}
            </span>
          </div>

          <div style={styles.row}>
            <span style={styles.label}>Transaction ID</span>
            <span style={styles.value}>{receipt.paymentId}</span>
          </div>

          <div style={styles.row}>
            <span style={styles.label}>Date</span>
            <span style={styles.value}>
              {new Date(receipt.timestamp + "Z").toLocaleString()}
            </span>
          </div>

          <div style={styles.divider}></div>

          <h3 style={{ marginTop: "10px", marginBottom: "10px" }}>Shipping</h3>

          {!shippingReady ? (
            <p style={{ color: "#888" }}>Fetching shipping details...</p>
          ) : (
            <>
              <div style={styles.row}>
                <span style={styles.label}>Name</span>
                <span style={styles.value}>
                  {receipt.buyerFirstName} {receipt.buyerLastName}
                </span>
              </div>

              <div style={styles.row}>
                <span style={styles.label}>Address</span>
                <span style={styles.value}>
                  {receipt.buyerStreetNum} {receipt.buyerStreetName},{" "}
                  {receipt.buyerCity}, {receipt.buyerCountry}
                </span>
              </div>

              <div style={styles.row}>
                <span style={styles.label}>Postal Code</span>
                <span style={styles.value}>{receipt.buyerPostalCode}</span>
              </div>
            </>
          )}

          <div style={styles.divider}></div>

          <h3 style={{ marginTop: "10px", marginBottom: "10px" }}>
            Price Breakdown
          </h3>

          <div style={styles.row}>
            <span style={styles.label}>Item Cost</span>
            <span style={styles.value}>${receipt.itemCost.toFixed(2)}</span>
          </div>

          <div style={styles.row}>
            <span style={styles.label}>Shipping Cost</span>
            <span style={styles.value}>${receipt.shippingCost.toFixed(2)}</span>
          </div>

          <div style={styles.row}>
            <span style={styles.label}>Shipping</span>
            <span style={styles.value}>
              {receipt.isExpedited ? "Expedited" : "Standard"}
            </span>
          </div>

          <div style={styles.divider}></div>

          <div style={styles.totalRow}>
            <span>Total</span>
            <span style={styles.totalValue}>${totalPrice.toFixed(2)}</span>
          </div>
        </div>

        <button
          onClick={() => navigate("/")}
          style={styles.button}
          onMouseOver={(e) => (e.target.style.backgroundColor = "#0056b3")}
          onMouseOut={(e) => (e.target.style.backgroundColor = "#007bff")}
        >
          Return to Home
        </button>
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
    maxWidth: "500px",
    backgroundColor: "#fff",
    borderRadius: "12px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    padding: "40px 30px",
    border: "1px solid #e1e4e8",
    height: "fit-content",
    textAlign: "center",
  },
  heading: {
    color: "#2c3e50",
    marginBottom: "10px",
    fontSize: "28px",
  },
  detailsBox: {
    backgroundColor: "#f8f9fa",
    border: "1px solid #e9ecef",
    borderRadius: "8px",
    padding: "20px",
    marginBottom: "30px",
    textAlign: "left",
  },
  row: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "12px",
    alignItems: "center",
  },
  label: {
    color: "#6c757d",
    fontSize: "14px",
    fontWeight: "500",
  },
  value: {
    color: "#212529",
    fontWeight: "600",
    fontSize: "14px",
    textAlign: "right",
    maxWidth: "60%",
    wordBreak: "break-word",
  },
  divider: {
    borderTop: "1px dashed #ced4da",
    margin: "15px 0",
  },
  totalRow: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    fontSize: "18px",
    fontWeight: "bold",
    color: "#2c3e50",
  },
  totalValue: {
    color: "#007bff",
    fontSize: "20px",
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
    transition: "background-color 0.2s",
  },
  secondaryButton: {
    marginTop: "20px",
    padding: "10px 20px",
    backgroundColor: "transparent",
    border: "1px solid #ccc",
    borderRadius: "6px",
    cursor: "pointer",
    color: "#555",
  },

  successIcon: { fontSize: "48px", color: "#28a745" },
  failureIcon: { fontSize: "48px", color: "#c91515ff" },
};

export default Receipt;
