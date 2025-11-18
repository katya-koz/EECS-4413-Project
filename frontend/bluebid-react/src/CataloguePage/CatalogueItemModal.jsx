import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";

function CatalogueItemModal({ id, onUpdate }) {
  const { authFetch } = useUser();
  const [item, setItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const [timeLeft, setTimeLeft] = useState("");
  const [bidAmount, setBidAmount] = useState("");
  const [bidMessage, setBidMessage] = useState("");

  // fetch the item info
  async function fetchItemInformation() {
    try {
      const url = new URL(`http://localhost:8080/api/catalogue/items/${id}`);
      const response = await authFetch(url.toString());

      if (!response.ok) throw new Error("Failed to fetch item " + id);

      const data = await response.json();
      setItem(data);
      setLoading(false);
      calculateTimeLeftDHMS(data.auctionEndTime);

      return data;
    } catch (err) {
      console.error(err);
      setLoading(false);
      return null;
    }
  }

  // calc the time left in days/hours/min/sec format
  function calculateTimeLeftDHMS(auctionEndTime) {
    const end = new Date(auctionEndTime);
    const now = new Date();
    let diff = end - now;

    if (diff <= 0) {
      setTimeLeft("Auction ended");
      return;
    }

    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    diff -= days * (1000 * 60 * 60 * 24);

    const hours = Math.floor(diff / (1000 * 60 * 60));
    diff -= hours * (1000 * 60 * 60);

    const minutes = Math.floor(diff / (1000 * 60));
    diff -= minutes * (1000 * 60);

    const seconds = Math.floor(diff / 1000);

    setTimeLeft(`${days}d ${hours}h ${minutes}m ${seconds}s`);
  }

  // submit the bid
  const handlePlaceBid = () => {
    placeBid();
  };
  // we need to keep polling the bid status since kafka can take some time.
  async function pollBidStatus(bidId, timeout = 10000, interval = 500) {
    setBidMessage("Loading...");
    const start = Date.now();

    while (Date.now() - start < timeout) {
      try {
        const bidResponse = await authFetch(
          `http://localhost:8080/api/bidding/bids/${bidId}`
        );
        const bidData = await bidResponse.json();

        if (!bidResponse.ok)
          return { success: false, message: "Bid lookup failed" };

        // Only exit if bid is valid
        if (bidData.valid === true) {
          return {
            success: true,
            message: bidData.status || "Bid accepted",
            bidData,
          };
        }
      } catch (err) {
        console.error("Polling error:", err);
      }

      // wait before next poll
      await new Promise((res) => setTimeout(res, interval));
    }

    return {
      success: false,
      message: "Timed out waiting for bid to be validated",
    };
  }

  async function placeBid() {
    if (!bidAmount || isNaN(bidAmount)) {
      // some sort of front end validation here
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

      if (!response.ok || data.isSuccess == false) {
        // is bid immedietly unsuccessful?
        setBidMessage(`Bid failed: ${data.message}`);
        return;
      }

      // otherwise, the bid attempt has been sent successfully. we need to poll the bid request now to check its status if it was accepted by the backend

      setBidAmount("");

      const bidResponse = await pollBidStatus(data.bidId);

      if (!bidResponse.success) {
        setBidMessage(
          `Bid failed: ${bidResponse.status || bidResponse.message}`
        );
      } else {
        const updatedItem = await fetchItemInformation();
        setBidMessage("Bid successfully placed!");

        if (onUpdate && updatedItem) {
          onUpdate(updatedItem);
        }
      }
      // onUpdate(
      //   {
      //   ...item,
      //   currentBiddingPrice: item.currentBiddingPrice,
      //   highestBidderID: item.highestBidderID,
      //   }
      // );
    } catch (err) {
      console.error(err);
      setBidMessage("Error placing bid");
    }
  }

  useEffect(() => {
    fetchItemInformation();
  }, [id]); // initial update of item info

  // upaate the item countdown every second
  useEffect(() => {
    const interval = setInterval(() => {
      if (item) calculateTimeLeftDHMS(item.auctionEndTime);
    }, 1000);

    return () => clearInterval(interval);
  }, [item]);

  if (loading) return <p>Loading...</p>;
  if (!item) return <p>Item not found.</p>;

  return (
    <div
      style={{
        maxWidth: "600px",
        margin: "20px auto",
        padding: "20px",
        border: "1px solid #ccc",
        borderRadius: "8px",
        backgroundColor: "white",
      }}
    >
      <h1>{item.itemName}</h1>
      <p>{item.itemDescription}</p>
      <p>
        <strong>Current Price:</strong> ${item.currentBiddingPrice.toFixed(2)}
      </p>
      <p>
        <strong>Highest Bidder:</strong>{" "}
        {item.highestBidderID ? item.highestBidderID : "No one has bidded yet"}
      </p>
      <p>
        <strong>Time Left:</strong> {timeLeft}
      </p>

      <div style={{ display: "flex", marginTop: "20px", gap: "10px" }}>
        <input
          type="number"
          placeholder="Your bid"
          value={bidAmount}
          onChange={(e) => setBidAmount(e.target.value)}
          style={{
            flex: 1,
            padding: "8px",
            borderRadius: "4px",
            border: "1px solid #ccc",
          }}
        />
        <button
          onClick={handlePlaceBid}
          style={{ padding: "8px 16px", borderRadius: "4px" }}
        >
          Place Bid
        </button>
      </div>
      {bidMessage && <p style={{ marginTop: "10px" }}>{bidMessage}</p>}
    </div>
  );
}

export default CatalogueItemModal;
