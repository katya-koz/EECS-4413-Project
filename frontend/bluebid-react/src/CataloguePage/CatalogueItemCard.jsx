import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import styles from "./CatalogueItemCard.module.scss";

function CatalogueItemCard({ item }) {
  const navigate = useNavigate();
  const location = useLocation();
  const [timeLeft, setTimeLeft] = useState("");

  const endTime = new Date(item.auctionEndTime);

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
    diff -= hours * (1000 * 60 * 60);

    const minutes = Math.floor(diff / (1000 * 60));
    diff -= minutes * (1000 * 60);

    const seconds = Math.floor(diff / 1000);
    setTimeLeft(`${days}d ${hours}h ${minutes}m ${seconds}s`);
  }

  useEffect(() => {
    calculateTimeLeftDHMS(); // initial paint
    const interval = setInterval(calculateTimeLeftDHMS, 1000);
    return () => clearInterval(interval);
  }, [endTime]);

  return (
    <div
      className={styles.card}
      onClick={() =>
        navigate(`/catalogue/items/${item.id}`, {
          state: { backgroundLocation: location },
        })
      }
    >
      <h2 className={styles.title}>{item.itemName}</h2>

      <p className={`${styles.row} ${styles.muted}`}>
        <span className="label"><strong>Current Price:</strong></span>{" "}
        ${item.currentBiddingPrice.toFixed(2)}
      </p>

      <p className={`${styles.row} ${styles.muted}`}>
        <span className="label"><strong>Auction Type:</strong></span>{" "}
        Forward
      </p>

      <p className={`${styles.row} ${styles.time}`}>
        <span className="label"><strong>Time Left:</strong></span>{" "}
        {timeLeft}
      </p>
    </div>
  );
}

export default CatalogueItemCard;
