import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./style/CatalogueItemCard.scss";

function CatalogueItemCard({ item }) {
  const navigate = useNavigate();
  const [timeLeft, setTimeLeft] = useState("");

  const endTime = new Date(item.auctionEndTime + "Z");

  // Calculate remaining time
  function calculateTimeLeftDHMS() {
    const now = new Date();
    let diff = endTime - now;

    if (diff <= 0) {
      setTimeLeft("Auction ended");
      return;
    }

    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    diff -= days * (1000 * 60 * 60 * 24);

    const hours = Math.floor(diff / (1000 * 60 * 60));
    diff -= hours * 60 * 60 * 1000;

    const minutes = Math.floor(diff / (1000 * 60));
    diff -= minutes * 60 * 1000;

    const seconds = Math.floor(diff / 1000);

    setTimeLeft(`${days}d ${hours}h ${minutes}m ${seconds}s`);
  }

  useEffect(() => {
    const interval = setInterval(() => {
      calculateTimeLeftDHMS();
    }, 1000);

    return () => clearInterval(interval);
  }, [endTime]);

  return (
    <div
      className="catalogueItemCard"
      onClick={() => navigate(`/catalogue/items/${item.id}`)}
    >
      <h2 className="itemName">{item.itemName}</h2>

      <p className="itemPrice">
        <span className="label">Current Price:</span> $
        {item.currentBiddingPrice.toFixed(2)}
      </p>

      <p className="itemType">
        <span className="label">Auction Type:</span> Forward
      </p>

      <p className="itemTimeLeft">
        <span className="label">Time Left:</span> {timeLeft}
      </p>
    </div>
  );
}

export default CatalogueItemCard;
