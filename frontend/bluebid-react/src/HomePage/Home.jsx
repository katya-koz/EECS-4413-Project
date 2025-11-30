import { useState } from "react";
import { useUser } from "../Context/UserContext";
import { Link } from "react-router-dom";
import styles from "./Home.scss";
function Home() {
  const [activeAuctions, setActiveAuctions] = useState([]);
  const [wonItems, setWonItems] = useState([]);
  const { user, authFetch } = useUser();

  async function getUserCurrentAuctions() {
    const res = await authFetch("http://localhost:8080/api/auction/auctions/");
    if (!res.ok) {
      const data = await res.json();
      console.error("Fetch failed: " + data.message);
      return;
    }

    const data = await res.json();
    setActiveAuctions(data);
  }

  return (
    <div>
      <h1>Welcome Back, {user?.username}</h1>
      <h2>Items you've bid on:</h2>

      {activeAuctions.length === 0 && <p>You haven't bid on anything yet.</p>}

      {activeAuctions.map((item) => (
        <div key={item.id} className={styles.card}>
          <h3 className={styles.cardTitle}>{item.itemName}</h3>
          {/* <p>Winning Bid: ${item.finalPrice}</p> */}
          {/* 
          <Link to={`/pay/${item.id}`}>
            <button style={{ padding: "8px 12px" }}>Pay Now</button>
          </Link> */}
        </div>
      ))}

      {/* to do: posted auctions, and items won with paynow button */}
      {/* <h2>Your Posted Items</h2>

      {wonItems.length === 0 && <p>You haven't posted anything yet.</p>}


      {wonItems.map((item) => (
        <div key={item.id} className={styles.card}>
          <h3 className={styles.cardTitle}>{item.name}</h3>
          <p>Winning Bid: ${item.finalPrice}</p>
          <Link to={`/pay/${item.id}`} className={styles.payBtn}>
            Pay Now
          </Link>
        </div>
      ))} */}
    </div>
  );
}

export default Home;
