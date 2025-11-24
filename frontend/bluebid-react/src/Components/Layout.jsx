import NavBar from "./NavBar";
import { useUser } from "../Context/UserContext";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import AuctionEndNotification from "./Notification/AuctionNotification"; // your toast component

function Layout({ children }) {
  const { user, token, expiresAt, logout } = useUser();
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    if (!user) return;

    async function subscribeAuctions() {
      const res = await fetch(`/api/auction/user-auctions/${user.id}`);
      const auctions = await res.json();

      const client = new Client({
        brokerURL: "ws://localhost:8080/ws",
        reconnectDelay: 5000,
      });

      // client.onConnect = () => {
      //   auctions.forEach((a) => {
      //     client.subscribe(`/topic/auction/${a.id}`, (msg) => {
      //       const notification = JSON.parse(msg.body);
      //       setNotifications((prev) => [...prev, notification]);
      //     });
      //   });
      // };

      client.onConnect = () => {
        // subscribe to test
        client.subscribe(`/topic/auction/test`, (message) => {
          console.log(message.body);
        });
      };

      client.activate();
    }

    subscribeAuctions();
  }, [user]);

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

      {notifications?.map((n, i) => (
        <AuctionEndNotification
          key={i}
          message={n.message}
          auctionId={n.auctionId}
        />
      ))}
    </div>
  );
}

export default Layout;
