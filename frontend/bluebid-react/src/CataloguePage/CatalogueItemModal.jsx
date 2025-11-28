import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import styles from "./CatalogueItemModal.module.scss";

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
      const response = await authFetch(`/api/catalogue/items/${id}`);
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
        const bidResponse = await authFetch(`/api/bidding/bids/${bidId}`);
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
      setBidMessage("Enter a valid bid amount");
      return;
    }

    try {
      const response = await authFetch(`/api/bidding/bid`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          auctionID: item.auctionId,
          bidRequest: parseFloat(bidAmount),
        }),
      });

      const data = await response.json();

      if (!response.ok || data.isSuccess == false) {
        setBidMessage(`Bid failed: ${data.message}`);
        return;
      }

      // otherwise, the bid attempt has been sent successfully.
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
    } catch (err) {
      console.error(err);
      setBidMessage("Error placing bid");
    }
  }

  useEffect(() => {
    fetchItemInformation();
  }, [id]);

  // update the item countdown every second
  useEffect(() => {
    const interval = setInterval(() => {
      if (item) calculateTimeLeftDHMS(item.auctionEndTime);
    }, 1000);

    return () => clearInterval(interval);
  }, [item]);

  if (loading) return <p>Loading...</p>;
  if (!item) return <p>Item not found.</p>;

  return (
    <div className={styles.modal}>
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

      <div className={styles.row}>
        <input
          type="number"
          placeholder="Your bid"
          value={bidAmount}
          onChange={(e) => setBidAmount(e.target.value)}
          className={styles.input}
        />
        <button onClick={handlePlaceBid} className={styles.primaryBtn}>
          Place Bid
        </button>
      </div>

      {bidMessage && <p className={styles.message}>{bidMessage}</p>}
    </div>
  );
}

export default CatalogueItemModal;
