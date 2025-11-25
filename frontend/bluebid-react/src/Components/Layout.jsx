import NavBar from "./NavBar";
import { useUser } from "../Context/UserContext";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import AuctionEndNotification from "./Notification/AuctionNotification";

function Layout({ children }) {
  const { user, token, expiresAt, logout, authFetch } = useUser();
  const [notifications, setNotifications] = useState([]);

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
    const res = await authFetch("http://localhost:8080/api/auction/auctions/");

    const response = await res.json();

    console.log(response);
    return response;
  }

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
          auctionId={n.auction?.id}
          onClose={() =>
            setNotifications((prev) => prev.filter((_, idx) => idx !== i))
          }
        />
      ))}
    </div>
  );
}

export default Layout;
