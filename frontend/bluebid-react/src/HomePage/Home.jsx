import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";

function Home() {
  const [wonItems, setWonItems] = useState([]);
  const { user } = useUser();

  return (
    <div>
      <h1>Welcome Back, {user?.username}</h1>
      <h2>Your Won Items</h2>

      {wonItems.length === 0 && <p>You haven't won anything yet.</p>}

      {wonItems.map((item) => (
        <div
          key={item.id}
          style={{
            padding: "12px",
            border: "1px solid #ddd",
            borderRadius: "8px",
            marginBottom: "10px",
          }}
        >
          <h3>{item.name}</h3>
          <p>Winning Bid: ${item.finalPrice}</p>

          <Link to={`/pay/${item.id}`}>
            <button style={{ padding: "8px 12px" }}>Pay Now</button>
          </Link>
        </div>
      ))}

      <h2>Your Posted Items</h2>

      {wonItems.length === 0 && <p>You haven't posted anything yet.</p>}

      {wonItems.map((item) => (
        <div
          key={item.id}
          style={{
            padding: "12px",
            border: "1px solid #ddd",
            borderRadius: "8px",
            marginBottom: "10px",
          }}
        >
          <h3>{item.name}</h3>
          <p>Winning Bid: ${item.finalPrice}</p>

          <Link to={`/pay/${item.id}`}>
            <button style={{ padding: "8px 12px" }}>Pay Now</button>
          </Link>
        </div>
      ))}
    </div>
  );
}

export default Home;
