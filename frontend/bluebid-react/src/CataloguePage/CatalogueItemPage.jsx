import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";

function CatalogueItemPage() {
  const { id } = useParams();
  const { authFetch, user, subscribeToAuction } = useUser();
  const [item, setItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const [timeLeft, setTimeLeft] = useState("");
  const [bidAmount, setBidAmount] = useState("");
  const [bidMessage, setBidMessage] = useState("");
  const navigate = useNavigate();
  // fetch info
  async function fetchItemInformation() {
    try {
      const response = await authFetch(
        `http://localhost:8080/api/catalogue/items/${id}`
      );

      if (!response.ok) throw new Error("Failed to fetch item");

      const data = await response.json();
      setItem(data);
      setLoading(false);
      calculateTimeLeft(data.auctionEndTime);

      return data;
    } catch (err) {
      console.error(err);
      setLoading(false);
      return null;
    }
  }

  function calculateTimeLeft(auctionEndTime) {
    const end = new Date(auctionEndTime + "Z"); // z converts to utc
    const now = new Date();

    const diff =
      Date.UTC(
        end.getUTCFullYear(),
        end.getUTCMonth(),
        end.getUTCDate(),
        end.getUTCHours(),
        end.getUTCMinutes(),
        end.getUTCSeconds()
      ) -
      Date.UTC(
        now.getUTCFullYear(),
        now.getUTCMonth(),
        now.getUTCDate(),
        now.getUTCHours(),
        now.getUTCMinutes(),
        now.getUTCSeconds()
      );

    if (diff <= 0) {
      setTimeLeft("Auction ended");
      return;
    }

    let remaining = diff;

    const days = Math.floor(remaining / (1000 * 60 * 60 * 24));
    remaining -= days * 24 * 60 * 60 * 1000;

    const hours = Math.floor(remaining / (1000 * 60 * 60));
    remaining -= hours * 60 * 60 * 1000;

    const minutes = Math.floor(remaining / (1000 * 60));
    remaining -= minutes * 60 * 1000;

    const seconds = Math.floor(remaining / 1000);

    setTimeLeft(`${days}d ${hours}h ${minutes}m ${seconds}s`);
  }

  async function pollBidStatus(bidId, timeout = 10000, interval = 500) {
    setBidMessage("Loading...");
    const start = Date.now();

    while (Date.now() - start < timeout) {
      try {
        const bidResponse = await authFetch(
          `http://localhost:8080/api/bidding/bids/${bidId}`
        );
        const bidData = await bidResponse.json();

        if (bidResponse.ok && bidData.valid) {
          return {
            success: true,
            message: bidData.status || "Bid accepted",
            bidData,
          };
        }
      } catch {}

      await new Promise((r) => setTimeout(r, interval));
    }

    return { success: false, message: "Bid validation timed out" };
  }

  async function placeBid() {
    if (!bidAmount || isNaN(bidAmount)) {
      setBidMessage("Enter a valid bid amount");
      return;
    }

    try {
      const response = await authFetch(
        `http://localhost:8080/api/bidding/bid`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            auctionID: item.auctionId,
            bidRequest: parseFloat(bidAmount),
          }),
        }
      );

      const data = await response.json();

      if (!response.ok || data.isSuccess === false) {
        setBidMessage(`Bid failed: ${data.message}`);
        return;
      }

      setBidAmount("");

      const bidStatus = await pollBidStatus(data.bidId);

      if (!bidStatus.success) {
        setBidMessage(`Bid failed: ${bidStatus.message}`);
        return;
      }

      const updatedItem = await fetchItemInformation();
      setBidMessage("Bid successfully placed!");
      // subscribe to auction
      subscribeToAuction(item.auctionId);
    } catch (err) {
      console.error(err);
      setBidMessage("Error placing bid");
    }
  }

  useEffect(() => {
    if (id) fetchItemInformation();
  }, [id]);

  useEffect(() => {
    const interval = setInterval(() => {
      if (item) calculateTimeLeft(item.auctionEndTime);
    }, 1000);
    return () => clearInterval(interval);
  }, [item]);

  if (loading) return <p>Loading...</p>;
  if (!item) return <p>Item not found.</p>;

  return (
    <div
      style={{
        maxWidth: "700px",
        margin: "30px auto",
        padding: "25px",
        borderRadius: "12px",
        background: "white",
        border: "1px solid #ddd",
        boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
      }}
    >
      <i
        style={{
          textDecoration: "none",
          background: "none",
          border: "none",
          cursor: "pointer",
        }}
        onClick={() => navigate(-1)}
        className="bi bi-arrow-left"
      ></i>

      <h1 style={{ marginTop: "10px" }}>{item.itemName}</h1>
      <p>{item.itemDescription}</p>

      <p>
        <strong>Current Price:</strong> ${item.currentBiddingPrice.toFixed(2)}
      </p>

      <p>
        <strong>Highest Bidder:</strong> {item.highestBidderID || "No bids yet"}
      </p>

      <p>
        <strong>Time Left:</strong> {timeLeft}
      </p>

      {item.sellerID !== user.userid && (
        <div style={{ display: "flex", marginTop: "20px", gap: "10px" }}>
          <input
            type="number"
            value={bidAmount}
            placeholder="Your bid"
            onChange={(e) => setBidAmount(e.target.value)}
            style={{
              flex: 1,
              padding: "8px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <button
            onClick={placeBid}
            style={{
              padding: "10px 16px",
              borderRadius: "6px",
              backgroundColor: "#007bff",
              color: "white",
              border: "none",
              fontWeight: "bold",
            }}
          >
            Place Bid
          </button>
        </div>
      )}

      {item.sellerID === user.userid && (
        <p style={{ marginTop: "10px" }}>You posted this item.</p>
      )}

      {bidMessage && (
        <p style={{ marginTop: "10px", fontWeight: "bold" }}>{bidMessage}</p>
      )}
    </div>
  );
}

export default CatalogueItemPage;
