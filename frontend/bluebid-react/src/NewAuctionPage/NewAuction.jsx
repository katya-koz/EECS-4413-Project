import { useState } from "react";
import { useUser } from "../Context/UserContext";
import { useToast } from "../Context/ToastContext";
import "./NewAuction.scss";

function NewAuction() {
  const [newAuctionForm, setNewAuctionForm] = useState({
    itemName: "",
    itemDescription: "",
    basePrice: "",
    seconds: "",
  });
  const { authFetch } = useUser();
  const { showToast } = useToast();
  const [errorMessage, setErrorMessage] = useState("");

  async function postNewAuction() {
    const res = await authFetch(
      "http://localhost:8080/api/auction/new-auction",
      {
        method: "POST",
        body: JSON.stringify(newAuctionForm),
      }
    );

    if (!res.ok) {
      const data = await res.text();
      console.error("Fetch failed: " + data);
      setErrorMessage(data);
      return;
    }

    const data = await res.json();

    if (data.submitSuccess === false) {
      setErrorMessage(data);
    } else {
      setErrorMessage(data.message);
      for (let i = 0; i < 10; i++) {
        const res = await authFetch(
          `http://localhost:8080/api/auction/auctions/${data.auctionId}`
        );
        const auction = await res.json();

        if (auction.catalogueID) {
          showToast(`Auction created successfully for ${auction.itemName}!`);
          return;
        }

        await new Promise((resolve) => setTimeout(resolve, 200));
      }
    }
  }

  const handleSubmitNewAuction = (e) => {
    e.preventDefault();
    postNewAuction();
  };

  const handleChange = (e) => {
    setNewAuctionForm({ ...newAuctionForm, [e.target.name]: e.target.value });
  };

  return (
    <div className="auction-container">
      <div className="auction-card">
        <h2 className="auction-title">Post a New Item for Auction</h2>
        <form onSubmit={handleSubmitNewAuction} className="auction-form">
          <input
            type="text"
            name="itemName"
            placeholder="Item Name"
            value={newAuctionForm.itemName}
            onChange={handleChange}
            className="auction-input"
          />
          <input
            type="text"
            name="itemDescription"
            placeholder="Item Description"
            value={newAuctionForm.itemDescription}
            onChange={handleChange}
            className="auction-input"
          />
          <div className="auction-flex">
            <input
              type="text"
              name="basePrice"
              placeholder="Starting Bid"
              value={newAuctionForm.basePrice}
              onChange={handleChange}
              className="auction-input"
            />
            <input
              type="text"
              name="seconds"
              placeholder="Length of Auction (seconds for test)"
              value={newAuctionForm.seconds}
              onChange={handleChange}
              className="auction-input"
            />
          </div>
          <button type="submit" className="auction-button">
            Post New Item
          </button>
        </form>
      </div>
      <p className="error-message">{errorMessage}</p>
    </div>
  );
}

export default NewAuction;
