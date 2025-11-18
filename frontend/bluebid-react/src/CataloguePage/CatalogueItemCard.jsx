import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

function CatalogueItemCard({ item }) {
  const navigate = useNavigate();
  const location = useLocation();
  const [timeLeft, setTimeLeft] = useState("");

  const endTime = new Date(item.auctionEndTime);

  // calc the time left in days/hours/min/sec format
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
    // update time left every second
    const interval = setInterval(() => {
      calculateTimeLeftDHMS();
    }, 1000);

    return () => clearInterval(interval);
  }, [endTime]);

  return (
    <div
      onClick={() =>
        navigate(`/catalogue/items/${item.id}`, {
          state: { backgroundLocation: location },
        })
      }
      className="cursor-pointer p-4 border rounded-xl shadow-md hover:shadow-lg transition bg-white"
    >
      <h2 className="text-lg font-semibold">{item.itemName}</h2>

      <p className="mt-2 text-gray-700">
        <span className="font-medium">Current Price:</span> $
        {item.currentBiddingPrice.toFixed(2)}
      </p>

      <p className="text-gray-700">
        <span className="font-medium">Auction Type:</span> Forward
      </p>

      <p className="text-gray-800 mt-1">
        <span className="font-medium">Time Left:</span> {timeLeft}
      </p>
    </div>
  );
}
export default CatalogueItemCard;
