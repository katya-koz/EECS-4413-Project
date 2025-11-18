import NavBar from "./NavBar";
import { useUser } from "../Context/UserContext";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Layout({ children }) {
  const { user } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    if (user === null) {
      navigate("/signin");
    }
  }, [user]);

  return (
    <div
      style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}
    >
      <NavBar />
      <main style={{ flex: 1, padding: "20px" }}>{children}</main>
    </div>
  );
}

export default Layout;
