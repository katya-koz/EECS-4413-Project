import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate,
} from "react-router-dom";
import PayNow from "../PayNowPage/PayNow";
import Home from "../HomePage/Home";
import { useNavigate } from "react-router-dom";
import { useLocation } from "react-router-dom"

function AuctionEnd() {
    const navigate = useNavigate();
    const location = useLocation();
    
    const { auctionID, finalPrice } = location.state || {};

    const handleRedirect = () => {
        navigate(`/pay/${auctionID}`, { state: { finalPrice: finalPrice } });
    };

    if (!auctionID) {
        return (
            <div style={styles.container}>
                <div style={styles.card}>
                    <h2 style={{ color: "#d9534f" }}>No Auction Selected</h2>
                    <p style={styles.text}>
                        It looks like you navigated here directly or refreshed the page.
                    </p>
                    <button onClick={() => navigate("/")} style={styles.secondaryButton}>
                        Return Home
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h1 style={styles.heading}>🎉 You Won!</h1>

                <p style={styles.text}>
                    The auction is over. You have the winning bid for Auction 
                    <strong> #{auctionID}</strong>.
                </p>

                <div style={styles.priceBox}>
                    <span style={styles.priceLabel}>Final Price</span>
                    <span style={styles.priceValue}>
                        ${Number(finalPrice || 0).toFixed(2)}
                    </span>
                </div>

                <p style={{ color: "#666", fontSize: "14px", marginBottom: "25px" }}>
                    Please complete your payment to claim this item.
                </p>

                <button
                    onClick={handleRedirect}
                    style={styles.primaryButton}
                    onMouseOver={(e) => e.target.style.backgroundColor = "#0056b3"}
                    onMouseOut={(e) => e.target.style.backgroundColor = "#007bff"}
                >
                    Pay Now
                </button>
            </div>
        </div>
    );
}

const styles = {
    container: {
        maxWidth: "500px",
        margin: "60px auto",
        padding: "0 20px",
    },
    card: {
        background: "#fff",
        border: "1px solid #ddd",
        borderRadius: "12px",
        padding: "30px",
        textAlign: "center",
        boxShadow: "0 4px 12px rgba(0,0,0,0.05)",
    },
    heading: {
        color: "#2c3e50",
        marginBottom: "15px",
        marginTop: "0",
    },
    text: {
        fontSize: "16px",
        lineHeight: "1.5",
        color: "#555",
        marginBottom: "20px",
    },
    priceBox: {
        background: "#f9f9f9",
        padding: "20px",
        borderRadius: "8px",
        marginBottom: "20px",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: "5px",
    },
    priceLabel: {
        textTransform: "uppercase",
        fontSize: "12px",
        letterSpacing: "1px",
        color: "#888",
        fontWeight: "bold",
    },
    priceValue: {
        fontSize: "32px",
        fontWeight: "bold",
        color: "#28a745",
    },
    primaryButton: {
        width: "100%",
        padding: "14px",
        borderRadius: "8px",
        backgroundColor: "#007bff",
        color: "white",
        fontSize: "16px",
        fontWeight: "bold",
        border: "none",
        cursor: "pointer",
        transition: "background 0.2s",
    },
    secondaryButton: {
        marginTop: "15px",
        padding: "10px 20px",
        borderRadius: "8px",
        backgroundColor: "transparent",
        border: "1px solid #ccc",
        color: "#555",
        cursor: "pointer",
    }
};

export default AuctionEnd;