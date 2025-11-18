// this will be the notification on auction end (like a toast)
import { useEffect, useState } from "react";

export default AuctionNotification() {
  const [msg, setMsg] = useState(null);
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    function onEnded(e) {
      const { title, id } = e.detail || {};
      setMsg(`Auction ended: ${title || id || ""}`.trim());
      setVisible(true);
      clearTimeout(onEnded._t || 0);
      onEnded._t = setTimeout(() => setVisible(false), 3500);
    }
    window.addEventListener("auction:end", onEnded);
    return () => {
      window.removeEventListener("auction:end", onEnded);
      clearTimeout(onEnded._t || 0);
    };
  }, []);

  if (!visible || !msg) return null;

  const box = {
    position: "fixed",
    top: 16,
    right: 16,
    zIndex: 9999,
    minWidth: 260,
    maxWidth: 360,
    padding: "10px 12px",
    borderRadius: 8,
    boxShadow: "0 4px 10px rgba(0,0,0,0.12)",
    border: "1px solid #bfdbfe",
    background: "#eff6ff",
    fontSize: 14,
    cursor: "pointer",
  };

  return (
    <div style={box} onClick={() => setVisible(false)} aria-live="polite">
      {msg}
    </div>
  );
}
