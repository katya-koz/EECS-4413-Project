import { createContext, useContext, useState, useCallback } from "react";
import Toast from "../Components/Notification/Toast";
import { useUser } from "../Context/UserContext";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const ToastContext = createContext();
export const useToast = () => useContext(ToastContext);

export function ToastProvider({ children }) {
  const [toastMessage, setToastMessage] = useState("");
  const [isVisible, setIsVisible] = useState(false);
  const [toastNavigate, setToastNavigate] = useState(null);

  const showToast = (msg, navigate = null) => {
    setToastMessage(msg);
    setToastNavigate(navigate);
    setIsVisible(true);
  };

  const closeToast = () => setIsVisible(false);
  const { notifications, user } = useUser();

  useEffect(() => {
    if (!notifications.length) return;
    if (!user || !user.userid) return;

    const latest = notifications[notifications.length - 1];
    const isWinner = latest.winnerId === user.userid;

    console.log("DETAILS: ", latest);
    if (isWinner) {
      showToast(
        `Auction for ${latest.auction.itemName} has finished. Click to pay!`,
        `/auction/auction-end?auctionID=${latest.auction.id}&finalPrice=${latest.finalPrice}`
      );
    } else {
      showToast(
        `Auction for ${latest.auction.itemName} has finished with a final bid of $${latest.finalPrice}. User #${latest.winnerId} has won the bid!`
      );
    }
  }, [notifications, user?.userid]);

  return (
    <ToastContext.Provider value={{ showToast }}>
      {children}
      <Toast
        message={toastMessage}
        isVisible={isVisible}
        onClose={closeToast}
        navigateTo={toastNavigate}
      />
    </ToastContext.Provider>
  );
}
