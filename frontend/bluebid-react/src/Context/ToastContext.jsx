import { createContext, useContext, useState, useCallback } from "react";
import Toast from "../Components/Notification/Toast";
import { useUser } from "../Context/UserContext";
import { useEffect } from "react";

const ToastContext = createContext();
export const useToast = () => useContext(ToastContext);

export function ToastProvider({ children }) {
  const [toastMessage, setToastMessage] = useState("");
  const [isVisible, setIsVisible] = useState(false);
  const [clickHandler, setClickHandler] = useState(null);

  const showToast = useCallback((msg, onClick = null) => {
    setToastMessage(msg);
    setClickHandler(onClick);
    setIsVisible(true);
  }, []);

  const closeToast = () => setIsVisible(false);
  const { notifications, user } = useUser();

  useEffect(() => {
    if (!notifications.length) return;

    const latest = notifications[notifications.length - 1];
    const isWinner = latest.winnerId == user.userid ? true : false;

    if (isWinner) {
      showToast(
        `Auction for ${latest.auction.itemName} has finished. You've won the bid!`
      );
    } else {
      showToast(
        `Auction for ${latest.auction.itemName} has finished with a final bid of $${latest.auction.finalPrice}. User #${latest.auction.winnerId} has won the bid!`
      );
    }
  }, [notifications]);
  return (
    <ToastContext.Provider value={{ showToast }}>
      {children}
      <Toast
        message={toastMessage}
        isVisible={isVisible}
        onClose={closeToast}
        onClick={clickHandler}
      />
    </ToastContext.Provider>
  );
}
