import NavBar from "./NavBar";
import { useUser } from "../Context/UserContext";
import { useEffect } from "react";

function Layout({ children }) {
  const { user, token, expiresAt, logout, loaded } = useUser();

  useEffect(() => {
    if (!loaded) return; // wait for user to load before possible redirect

    if (!user || !token || !expiresAt) {
      logout();
    }
  }, [loaded, user, token, expiresAt]);

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
