import NavBar from "./NavBar";
import { useUser } from "../Context/UserContext";
import { useEffect, useState } from "react";

function Layout({ children }) {
  const { user, token, expiresAt, logout, authFetch } = useUser();

  useEffect(() => {
    if (user === null || token === null || expiresAt === null) {
      logout();
    }
  }, [user, token, expiresAt]);

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
