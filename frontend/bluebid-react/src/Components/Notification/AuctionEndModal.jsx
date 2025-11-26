import { useNavigate } from "react-router-dom";

function AuctionEndModal({ open, onClose, result }) {
  const navigate = useNavigate();

  if (!open || !result) return null;

  const { itemId, result: outcome, finalPrice } = result;
  const won = outcome === "WIN";

  return (
    <div
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        background: "rgba(0,0,0,0.5)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: 9998,
      }}
    >
      <div
        style={{
          background: "white",
          padding: "20px",
          borderRadius: "10px",
          minWidth: "300px",
        }}
      >
        <h2>{won ? "Auction Over — You Won!" : "Auction Over — You Lost"}</h2>
        <p>Item ID: {itemId}</p>

        {won ? (
          <>
            <p>Final Price: ${finalPrice}</p>
            <button
              style={{ padding: "10px", marginTop: "15px" }}
              onClick={() => navigate(`/pay/${itemId}`)}
            >
              Pay Now
            </button>
          </>
        ) : (
          <p>Unfortunately, your bid was not the highest.</p>
        )}

        <button style={{ marginTop: "20px", padding: "8px" }} onClick={onClose}>
          Close
        </button>
      </div>
    </div>
  );
}

export default AuctionEndModal;
