import { createContext, useContext, useState, useEffect } from "react";

const UserContext = createContext();

export function UserProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem("token"));

  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem("user");
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const [expiresAt, setExpiresAt] = useState(() => {
    const saved = localStorage.getItem("expiresAt");
    return saved ? new Date(saved).getTime() : null;
  });

  useEffect(() => {
    if (token) localStorage.setItem("token", token);
    else localStorage.removeItem("token");
  }, [token]);

  useEffect(() => {
    if (user) localStorage.setItem("user", JSON.stringify(user));
    else localStorage.removeItem("user");
  }, [user]);

  useEffect(() => {
    if (expiresAt) localStorage.setItem("expiresAt", expiresAt.toString());
    else localStorage.removeItem("expiresAt");
  }, [expiresAt]);

  useEffect(() => {
    if (!expiresAt) return;

    const now = Date.now();

    if (now >= expiresAt) {
      logout();
      return;
    }

    const timeout = setTimeout(() => {
      logout();
    }, expiresAt - now);

    return () => clearTimeout(timeout);
  }, [expiresAt]);

  const login = (jwt, userData, expiry) => {
    setToken(jwt);
    setUser(userData || null);

    setExpiresAt(expiry ? new Date(expiry).getTime() : null);
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    setExpiresAt(null);
  };

  const authFetch = async (url, options = {}) => {
    const headers = {
      ...(options.headers || {}),
      "Content-Type": "application/json",
    };

    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    }

    const res = await fetch(url, {
      ...options,
      headers,
    });

    if (res.status === 401) logout();

    return res;
  };

  return (
    <UserContext.Provider
      value={{ token, user, expiresAt, login, logout, authFetch }}
    >
      {children}
    </UserContext.Provider>
  );
}

export function useUser() {
  return useContext(UserContext);
}
