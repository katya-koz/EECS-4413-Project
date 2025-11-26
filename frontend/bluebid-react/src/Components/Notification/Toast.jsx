import { useEffect } from "react";
import "./Toast.scss";

function Toast({ message, isVisible, onClose, onClick }) {
  useEffect(() => {
    if (!isVisible) return;
    const timer = setTimeout(() => {
      onClose();
    }, 5000);
    return () => clearTimeout(timer);
  }, [isVisible, onClose]);

  return (
    <div
      className={`toast ${isVisible ? "show" : ""}`}
      onClick={onClick}
      style={{ cursor: onClick ? "pointer" : "default" }}
    >
      {message}
    </div>
  );
}

export default Toast;
