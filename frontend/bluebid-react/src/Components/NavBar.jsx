// components/Navbar.jsx
import { Link } from "react-router-dom";
import { useUser } from "../Context/UserContext";
function NavBar() {
  const { logout } = useUser();
  return (
    <nav
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "10px 20px",
        backgroundColor: "#007bff",
        color: "white",
      }}
    >
      <h1 style={{ margin: 0, fontSize: "20px" }}>BlueBid</h1>
      <div style={{ display: "flex", gap: "15px" }}>
        <Link to="/" style={{ color: "white", textDecoration: "none" }}>
          Home
        </Link>
        <Link
          to="/catalogue/items"
          style={{ color: "white", textDecoration: "none" }}
        >
          Catalogue
        </Link>
        <Link
          to="/auction/new-auction"
          style={{ color: "white", textDecoration: "none" }}
        >
          Post an Auction
        </Link>

        <Link
          to="/login"
          onClick={logout}
          style={{ color: "white", textDecoration: "none" }}
        >
          Log out
        </Link>
      </div>
    </nav>
  );
}
export default NavBar;
