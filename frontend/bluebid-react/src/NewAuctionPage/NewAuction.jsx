import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useToast } from "../Context/ToastContext";

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
      setErrorMessage(data.message); // not error message, will change later
      // get the catalogue item id to navigate to pop up the modal
      for (let i = 0; i < 10; i++) {
        // kafka stream needs some time to populate catalogueid field. we are polling until we get it.
        const res = await authFetch(
          `http://localhost:8080/api/auction/auctions/${data.auctionId}`
        );
        const auction = await res.json();

        if (auction.catalogueID) {
          showToast(`Auction created successfully for ${auction.itemName}!`);
          return; // success
        }

        await new Promise((resolve) => setTimeout(resolve, 200)); // retry every 200 ms
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
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        padding: "20px",
        flexDirection: "column",
      }}
    >
      <div
        style={{
          width: "100%",
          maxWidth: "500px",
          padding: "30px",
          border: "1px solid #ccc",
          borderRadius: "12px",
          boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
        }}
      >
        <h2
          style={{ fontSize: "24px", fontWeight: "bold", marginBottom: "20px" }}
        >
          Post a New Item for Auction
        </h2>
        <form
          onSubmit={handleSubmitNewAuction}
          style={{ display: "grid", gap: "15px" }}
        >
          <input
            type="itemName"
            name="itemName"
            placeholder="Item Name"
            value={newAuctionForm.itemName}
            onChange={handleChange}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <input
            type="itemDescription"
            name="itemDescription"
            placeholder="Item Description"
            value={newAuctionForm.itemDescription}
            onChange={handleChange}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <div style={{ display: "flex", gap: "10px" }}>
            <input
              type="text"
              name="basePrice"
              placeholder="Starting Bid"
              value={newAuctionForm.basePrice}
              onChange={handleChange}
              style={{
                flex: 1,
                padding: "10px",
                borderRadius: "6px",
                border: "1px solid #ccc",
              }}
            />
            <input
              type="text"
              name="seconds"
              placeholder="Length of Auction (seconds for test)"
              value={newAuctionForm.seconds}
              onChange={handleChange}
              style={{
                flex: 1,
                padding: "10px",
                borderRadius: "6px",
                border: "1px solid #ccc",
              }}
            />
          </div>

          <button
            type="submit"
            style={{
              padding: "12px",
              borderRadius: "8px",
              backgroundColor: "#007bff",
              color: "white",
              fontWeight: "bold",
              border: "none",
              cursor: "pointer",
            }}
          >
            Post New Item
          </button>
        </form>
      </div>

      <p type="error-message">{errorMessage}</p>
    </div>
  );
}

export default NewAuction;

// successful response:
// {
//     "message": "New auction request has successfully been submitted.",
//     "auctionId": "6926215ac55558b3adbbd35b",
//     "submitSuccess": true
// }
