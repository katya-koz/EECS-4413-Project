import { useLocation, useNavigate } from "react-router-dom";

function Receipt() {
  const { state } = useLocation();
  const navigate = useNavigate();

  if (!state || !state.receipt) {
    return (
      <div>
        <h2>No receipt found</h2>
        <button onClick={() => navigate("/")}>Go Home</button>
      </div>
    );
  }

  const { receipt } = state;

  return (
    <div
      style={{
        maxWidth: "500px",
        margin: "40px auto",
        padding: "20px",
        border: "1px solid #ccc",
        borderRadius: "12px",
      }}
    >
      <h1>Payment Receipt</h1>
      <p>
        <strong>Transaction ID:</strong> {receipt.transactionId}
      </p>
      <p>
        <strong>Item ID:</strong> {receipt.itemId}
      </p>
      <p>
        <strong>Amount Paid:</strong> ${receipt.amount}
      </p>
      <p>
        <strong>Date:</strong> {new Date(receipt.date).toLocaleString()}
      </p>

      <button
        onClick={() => navigate("/")}
        style={{ marginTop: "20px", padding: "10px" }}
      >
        Go to Home
      </button>
    </div>
  );
}

export default Receipt;
