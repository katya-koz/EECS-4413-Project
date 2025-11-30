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
  const [clickHandler, setClickHandler] = useState(null);

  const showToast = useCallback((msg, onClick = null) => {
    setToastMessage(msg);
    setClickHandler(onClick);
    setIsVisible(true);
  }, []);

  const closeToast = () => setIsVisible(false);
  const { notifications, user } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    if (!notifications.length) return;

    const latest = notifications[notifications.length - 1];
    const isWinner = latest.winnerId == user.userid ? true : false;
	
	console.log("DETAILS: ", latest);
    if (isWinner) {
		
      showToast(
        `Auction for ${latest.auction.itemName} has finished. You've won with the highest bid of $${latest.finalPrice}!`
      );
	  const timer = setTimeout(() => {
	            
	            navigate("/auction/auction-end", {state: {auctionID: latest.auction.id, finalPrice: latest.finalPrice}});
	        }, 3000);

	      
	        return () => clearTimeout(timer);
    } else {
      showToast(
        `Auction for ${latest.auction.itemName} has finished with a final bid of $${latest.finalPrice}. User #${latest.winnerId} has won the bid!`
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
