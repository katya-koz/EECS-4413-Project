import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import "./Home.scss";
import { Link } from "react-router-dom";

function Home() {
  const [activeAuctions, setActiveAuctions] = useState([]);
  const [wonItems, setWonItems] = useState([]);
  const { user, authFetch } = useUser();

  // Fetch auctions the user is currently bidding on
  async function getUserCurrentAuctions() {
    try {
      const res = await authFetch(
        "http://localhost:8080/api/auction/auctions/"
      );

      if (!res.ok) {
        const data = await res.json();
        console.error("Fetch failed: " + (data.message || res.statusText));
        return;
      }

      const data = await res.json();
      const auctions = (data._embedded && data._embedded.auctionList) || [];
      setActiveAuctions(auctions);
    } catch (err) {
      console.error("Error fetching auctions:", err);
    }
  }

  // Fetch auctions the user has won and needs to pay
  async function getUserWonItems() {
    try {
      const res = await authFetch(
        "http://localhost:8080/api/auction/auctions/to-pay"
      );

      if (!res.ok) {
        const data = await res.json();
        console.error("Fetch failed: " + (data.message || res.statusText));
        return;
      }

      const data = await res.json();
      const auctions = (data._embedded && data._embedded.auctionList) || [];
      setWonItems(auctions);
    } catch (err) {
      console.error("Error fetching won items:", err);
    }
  }

  useEffect(() => {
    getUserCurrentAuctions();
    getUserWonItems();
  }, []);

  return (
    <div>
      <h1>Welcome Back, {user?.username}</h1>

      <h2>Items you've bid on:</h2>
      {(!activeAuctions || activeAuctions.length === 0) && (
        <p>You haven't bid on anything yet.</p>
      )}
      {activeAuctions &&
        activeAuctions.map((item) => (
          <div key={item.id || item._links?.self?.href} className="card">
            <h3 className="cardTitle">{item.itemName || "Unnamed Item"}</h3>
            <p className="cardDesc">{item.itemDescription}</p>
          </div>
        ))}

      <h2>Items you've won (awaiting payment):</h2>
      {(!wonItems || wonItems.length === 0) && (
        <p>You don't have any items to pay for.</p>
      )}
      {wonItems &&
        wonItems.map((item) => (
          <div key={item.id || item._links?.self?.href} className="card">
            <h3 className="cardTitle">{item.itemName || "Unnamed Item"}</h3>
            <p className="cardDesc">{item.itemDescription}</p>
            <Link to={`/pay/${item.id}`} className="payBtn">
              Pay Now
            </Link>
          </div>
        ))}
    </div>
  );
}

export default Home;
