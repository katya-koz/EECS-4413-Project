import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useUser } from "../Context/UserContext";
import { useEffect, useState } from "react";

function Receipt() {
  const { id } = useParams();
  const { authFetch } = useUser();
  const navigate = useNavigate();
  const location = useLocation();
  
  const { itemName } = location.state || {};

  const [receipt, setReceipt] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function getReceipt() {
      try {
        const response = await authFetch(`http://localhost:8080/api/payment/receipt/${id}`);

        if (response.ok) {
          const data = await response.json();
          console.log("RECEIPT DETAILS", data);
          setReceipt(data);
        } else {
          setError("Could not load receipt");
        }
      } catch (err) {
        setError("Network error");
      } finally {
        setLoading(false);
      }
    }

    if (id) {
      getReceipt();
    }
  }, [id, authFetch]);

  if (loading) return <p style={{ textAlign: "center", marginTop: "50px" }}>Loading receipt...</p>;

  if (error || !receipt) {
    return (
      <div style={styles.pageContainer}>
        <div style={styles.card}>
          <h2 style={{ color: "#d9534f", textAlign: "center" }}>No receipt found</h2>
          <p style={{ textAlign: "center", color: "#666" }}>ID: {id}</p>
          <p style={{ textAlign: "center", color: "#d9534f" }}>{error}</p>
          <button onClick={() => navigate("/")} style={styles.secondaryButton}>Go Home</button>
        </div>
      </div>
    );
  }

  const totalPrice = receipt.itemCost + receipt.shippingCost;

  return (
    <div style={styles.pageContainer}>
      <div style={styles.card}>
        <div style={styles.iconContainer}>
          <i className="bi bi-check-circle-fill" style={styles.successIcon}></i>
        </div>
        
        <h1 style={styles.heading}>Payment Successful</h1>
        <p style={styles.subText}>Here is your payment receipt.</p>

        <div style={styles.detailsBox}>
          <div style={styles.row}>
            <span style={styles.label}>Item</span>
            <span style={styles.value}>{itemName || "Unknown Item"}</span>
          </div>

          <div style={styles.row}>
            <span style={styles.label}>Transaction ID</span>
            <span style={styles.value}>{receipt.paymentId}</span>
          </div>

          <div style={styles.row}>
            <span style={styles.label}>Date</span>
            <span style={styles.value}>{new Date(receipt.timestamp).toLocaleString()}</span>
          </div>
          
          <div style={styles.divider}></div>

          <div style={styles.totalRow}>
            <span>Amount Paid</span>
            <span style={styles.totalValue}>${totalPrice.toFixed(2)}</span>
          </div>
        </div>

        <button
          onClick={() => navigate("/")}
          style={styles.button}
          onMouseOver={(e) => e.target.style.backgroundColor = "#0056b3"}
          onMouseOut={(e) => e.target.style.backgroundColor = "#007bff"}
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
  iconContainer: {
    marginBottom: "20px",
  },
  successIcon: {
    fontSize: "48px",
    color: "#28a745",
  },
  heading: {
    color: "#2c3e50",
    marginBottom: "10px",
    marginTop: "0",
    fontSize: "28px",
  },
  subText: {
    color: "#666",
    fontSize: "16px",
    marginBottom: "30px",
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
  }
};

export default Receipt;