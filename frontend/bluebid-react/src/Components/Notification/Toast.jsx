import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Toast.scss";

function Toast({ message, isVisible, onClose, navigateTo }) {
  const navigate = useNavigate();

  useEffect(() => {
    if (!isVisible) return;
    const timer = setTimeout(() => {
      onClose();
    }, 5000);
    return () => clearTimeout(timer);
  }, [isVisible, onClose]);

  const onClick = () => {
    if (navigateTo) {
      navigate(navigateTo);
      onClose();
    }
  };

  return (
    <div
      className={`toast ${isVisible ? "show" : ""}`}
      onClick={onClick}
      style={{ cursor: navigateTo ? "pointer" : "default" }}
    >
      {message}
    </div>
  );
}

export default Toast;
