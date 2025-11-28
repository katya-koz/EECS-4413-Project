import { useState } from "react";
import { useUser } from "../Context/UserContext";
import { Link } from "react-router-dom";
import styles from "./Home.module.scss";

function Home() {
  const [wonItems, setWonItems] = useState([]); // placeholder data source
  const { user } = useUser();

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Welcome Back, {user?.username}</h1>

      {/* Won items */}
      <h2 className={styles.sectionTitle}>Your Won Items</h2>
      {wonItems.length === 0 && (
        <p className={styles.empty}>You haven&apos;t won anything yet.</p>
      )}
      {wonItems.map((item) => (
        <div key={item.id} className={styles.card}>
          <h3 className={styles.cardTitle}>{item.name}</h3>
          <p>Winning Bid: ${item.finalPrice}</p>
          <Link to={`/pay/${item.id}`} className={styles.payBtn}>
            Pay Now
          </Link>
        </div>
      ))}

      {/* Posted items (uses same array in your current code; keep as-is) */}
      <h2 className={styles.sectionTitle}>Your Posted Items</h2>
      {wonItems.length === 0 && (
        <p className={styles.empty}>You haven&apos;t posted anything yet.</p>
      )}
      {wonItems.map((item) => (
        <div key={item.id} className={styles.card}>
          <h3 className={styles.cardTitle}>{item.name}</h3>
          <p>Winning Bid: ${item.finalPrice}</p>
          <Link to={`/pay/${item.id}`} className={styles.payBtn}>
            Pay Now
          </Link>
        </div>
      ))}
    </div>
  );
}

export default Home;
