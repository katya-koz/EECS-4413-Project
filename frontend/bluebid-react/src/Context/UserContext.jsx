import { createContext, useContext, useState, useEffect } from "react";

const UserContext = createContext();

export function UserProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem("token")); // get token from local storage
  const [loaded, setLoaded] = useState(false); // load user flag

  // load userr from local storage
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem("user");
    return savedUser ? JSON.parse(savedUser) : null;
  });
  // load expiresAt from local storage
  const [expiresAt, setExpiresAt] = useState(() => {
    const saved = localStorage.getItem("expiresAt");
    return saved ? Number(saved) : null;
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
    setLoaded(true);
  }, []);

  useEffect(() => {
    if (!expiresAt) {
      // log out if there's no 'expires at'
      logout();
      return;
    }

    const now = Date.now();

    if (now >= expiresAt) {
      // log out if token has expired
      logout();
      return;
    }

    const timeout = setTimeout(() => {
      // automatically log out on token expiry
      logout();
    }, expiresAt - now);

    return () => clearTimeout(timeout);
  }, [expiresAt]);

  const login = (jwt, userData, expiry) => {
    // on login, set storage
    setToken(jwt);
    setUser(userData || null);

    setExpiresAt(expiry ? new Date(expiry).getTime() : null);
  };

  const logout = () => {
    // clear local storage
    setToken(null);
    setUser(null);
    setExpiresAt(null);
  };

  // automatically applies jwt to header (Authorization Bearer), and correct content type
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

    if (res.status === 401) {
      console.log("logging out 4");
      logout();
    }

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
