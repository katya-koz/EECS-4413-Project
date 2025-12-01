import { useNavigate, useLocation, useSearchParams } from "react-router-dom";
import "./AuctionEnd.scss";

function AuctionEnd() {
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams] = useSearchParams();

  const stateAuctionID = location.state?.auctionID;
  const stateFinalPrice = location.state?.finalPrice;

  const auctionID = stateAuctionID || searchParams.get("auctionID");
  const finalPrice = stateFinalPrice || searchParams.get("finalPrice");

  const handleRedirect = () => {
    navigate(`/pay/${auctionID}`, { state: { finalPrice: finalPrice } });
  };

  if (!auctionID) {
    return (
      <div className="auctionEnd-container">
        <div className="auctionEnd-card">
          <h2 className="auctionEnd-error">No Auction Selected</h2>
          <p className="auctionEnd-text">
            It looks like you navigated here directly or refreshed the page.
          </p>
          <button
            onClick={() => navigate("/")}
            className="auctionEnd-button secondary"
          >
            Return Home
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="auctionEnd-container">
      <div className="auctionEnd-card">
        <h1 className="auctionEnd-heading">🎉 You Won!</h1>

        <p className="auctionEnd-text">
          The auction is over. You have the winning bid for Auction
          <strong> #{auctionID}</strong>.
        </p>

        <div className="auctionEnd-priceBox">
          <span className="auctionEnd-priceLabel">Final Price</span>
          <span className="auctionEnd-priceValue">
            ${Number(finalPrice || 0).toFixed(2)}
          </span>
        </div>

        <p className="auctionEnd-subtext">
          Please complete your payment to claim this item.
        </p>

        <button onClick={handleRedirect} className="auctionEnd-button primary">
          Pay Now
        </button>
      </div>
    </div>
  );
}

export default AuctionEnd;
