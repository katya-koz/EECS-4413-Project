import { createContext, useContext, useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Client } from "@stomp/stompjs";

const UserContext = createContext();

export function UserProvider({ children }) {
  const [notifications, setNotifications] = useState([]);
  const [token, setToken] = useState(() => localStorage.getItem("token")); // get token from local storage
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

  const navigate = useNavigate();
  const clientRef = useRef(null);
  const subscribedAuctions = useRef(new Set());

  const subscribeToAuction = (auctionId) => {
    if (!clientRef.current || subscribedAuctions.current.has(auctionId)) return;

    clientRef.current.subscribe(`/topic/auction/${auctionId}`, (message) => {
      const data = JSON.parse(message.body);
      setNotifications((prev) => [...prev, data]);
    });
    subscribedAuctions.current.add(auctionId);
  };

  // TO DO: we need to store the auctions we are already subscribed to, so that we dont subscribe to the same topic multiple times
  useEffect(() => {
    if (!user) return;

    // Create the client only once
    clientRef.current = new Client({
      brokerURL: "ws://localhost:8080/ws",
      reconnectDelay: 5000,
    });

    clientRef.current.onConnect = async () => {
      const auctions = await getAuctions();
      auctions.forEach((a) => subscribeToAuction(a.id));
    };

    clientRef.current.onStompError = (frame) => {
      console.error("STOMP Error:", frame);
    };

    clientRef.current.activate();

    return () => {
      clientRef.current.deactivate();
      subscribedAuctions.current.clear();
    };
  }, [user]);

  // get the user's bids
  async function getAuctions() {
    if (!user) return;
    const res = await authFetch("http://localhost:8080/api/auction/auctions/");

    const response = await res.json();
    return response;
  }

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

    navigate("/signin");
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
      logout();
    }

    return res;
  };

  return (
    <UserContext.Provider
      value={{
        notifications,
        token,
        user,
        expiresAt,
        login,
        logout,
        authFetch,
        subscribeToAuction,
      }}
    >
      {children}
    </UserContext.Provider>
  );
}

export function useUser() {
  return useContext(UserContext);
}

/*
  useEffect(() => {
    if (!user) return;

    const connectAuctions = async () => {
      const auctions = await getAuctions();

      const client = new Client({
        brokerURL: "ws://localhost:8080/ws",
        reconnectDelay: 5000,
      });

      client.onConnect = () => {
        auctions.forEach((a) => {
          console.log(`subbed to /topic/auction/${a.id}`);

          client.subscribe(`/topic/auction/${a.id}`, (message) => {
            const data = JSON.parse(message.body);
            setNotifications((prev) => [...prev, data]);
          });
        });
      };

      client.onStompError = (frame) => {
        console.error("STOMP error", frame);
      };

      client.activate();

      return () => client.deactivate();
    };

    connectAuctions();
  }, [user]);

  async function getAuctions() {
    if (!user) return;
    const res = await authFetch("http://localhost:8080/api/auction/auctions/");

    const response = await res.json();

    console.log(response);
    return response;
  }
 
 */
