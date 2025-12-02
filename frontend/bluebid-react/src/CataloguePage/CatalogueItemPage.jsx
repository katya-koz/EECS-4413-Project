import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import { useParams, useNavigate } from "react-router-dom";
import "./style/CatalogueItemPage.scss";
import { validateFields } from "./CatalogueItemValidations.js";

function CatalogueItemPage() {
	const { id } = useParams();
	const { authFetch, user, subscribeToAuction } = useUser();
	const [item, setItem] = useState(null);
	const [loading, setLoading] = useState(true);
	const [timeLeft, setTimeLeft] = useState("");
	const [bidAmount, setBidAmount] = useState("");
	const [bidMessage, setBidMessage] = useState("");
	const navigate = useNavigate();
	const [errors, setErrors] = useState({});

	const handleBidChange = (e) => {
		setBidAmount(e.target.value);
		if (errors.bidAmount) {
			setErrors({ ...errors, bidAmount: null });
		}
	};

	// fetch info
	async function fetchItemInformation() {
		try {
			const response = await authFetch(
				`http://localhost:8080/api/catalogue/items/${id}`
			);

			if (!response.ok) throw new Error("Failed to fetch item");

			const data = await response.json();
			setItem(data);
			setLoading(false);
			calculateTimeLeft(data.auctionEndTime);

			return data;
		} catch (err) {
			console.error(err);
			setLoading(false);
			return null;
		}
	}

	function calculateTimeLeft(auctionEndTime) {
		const end = new Date(auctionEndTime + "Z");
		const now = new Date();

		const diff =
			Date.UTC(
				end.getUTCFullYear(),
				end.getUTCMonth(),
				end.getUTCDate(),
				end.getUTCHours(),
				end.getUTCMinutes(),
				end.getUTCSeconds()
			) -
			Date.UTC(
				now.getUTCFullYear(),
				now.getUTCMonth(),
				now.getUTCDate(),
				now.getUTCHours(),
				now.getUTCMinutes(),
				now.getUTCSeconds()
			);

		if (diff <= 0) {
			setTimeLeft("Auction ended");
			return;
		}

		let remaining = diff;

		const days = Math.floor(remaining / (1000 * 60 * 60 * 24));
		remaining -= days * 24 * 60 * 60 * 1000;

		const hours = Math.floor(remaining / (1000 * 60 * 60));
		remaining -= hours * 60 * 60 * 1000;

		const minutes = Math.floor(remaining / (1000 * 60));
		remaining -= minutes * 60 * 1000;

		const seconds = Math.floor(remaining / 1000);

		setTimeLeft(`${days}d ${hours}h ${minutes}m ${seconds}s`);
	}

	async function pollBidStatus(bidId, timeout = 10000, interval = 500) {
		setBidMessage("Loading...");
		const start = Date.now();

		while (Date.now() - start < timeout) {
			try {
				const bidResponse = await authFetch(
					`http://localhost:8080/api/bidding/bids/${bidId}`
				);
				const bidData = await bidResponse.json();

				if (bidResponse.ok && bidData.valid) {
					return {
						success: true,
						message: bidData.status || "Bid accepted",
						bidData,
					};
				}
			} catch { }

			await new Promise((r) => setTimeout(r, interval));
		}

		return { success: false, message: "Bid validation timed out" };
	}

	async function placeBid() {

		const validationErrors = validateFields({ bidAmount });

		if (Object.keys(validationErrors).length > 0) {
			setErrors(validationErrors);
			return;
		}
		setErrors({});


		if (!bidAmount || isNaN(bidAmount)) {
			setBidMessage("Enter a valid bid amount");
			return;
		}

		try {
			const response = await authFetch(
				`http://localhost:8080/api/bidding/bid`,
				{
					method: "POST",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({
						auctionID: item.auctionId,
						bidRequest: parseFloat(bidAmount),
					}),
				}
			);

			const data = await response.json();

			if (!response.ok || data.isSuccess === false) {
				setBidMessage(`Bid failed: ${data.message}`);
				return;
			}

			setBidAmount("");

			const bidStatus = await pollBidStatus(data.bidId);

			if (!bidStatus.success) {
				setBidMessage(`Bid failed: ${bidStatus.message}`);
				return;
			}

			const updatedItem = await fetchItemInformation();
			setBidMessage("Bid successfully placed!");
			subscribeToAuction(item.auctionId);
		} catch (err) {
			console.error(err);
			setBidMessage("Error placing bid");
		}
	}

	useEffect(() => {
		if (id) fetchItemInformation();
	}, [id]);

	useEffect(() => {
		const interval = setInterval(() => {
			if (item) calculateTimeLeft(item.auctionEndTime);
		}, 1000);
		return () => clearInterval(interval);
	}, [item]);

	if (loading) return <p className="loadingText">Loading...</p>;
	if (!item) return <p className="notFoundText">Item not found.</p>;

	return (
		<div className="catalogueItemPage">
			<button className="backButton" onClick={() => navigate(-1)}>
				<i className="bi bi-arrow-left"></i>
			</button>

			<h1 className="itemTitle">{item.itemName}</h1>
			<p className="itemDescription">{item.itemDescription}</p>

			<p>
				<strong>Current Price:</strong> ${item.currentBiddingPrice.toFixed(2)}
			</p>

			<p>
				<strong>Highest Bidder:</strong> {item.highestBidderID || "No bids yet"}
			</p>

			<p>
				<strong>Time Left:</strong> {timeLeft}
			</p>

			{item.sellerID !== user.userid && (
				<div className="bidRow" style={{ alignItems: "flex-start" }}> {/* Aligns button to top of input */}


					<div style={{ display: "flex", flexDirection: "column", flex: 1, marginRight: "10px" }}>
						<input
							type="number"
							value={bidAmount}
							placeholder="Your bid"
							onChange={handleBidChange}
							className="bidInput"
							style={{ width: "98%", borderColor: errors.bidAmount ? "red" : "" }}
						/>
						{errors.bidAmount && (
							<span className="error-text" style={{ color: "red", fontSize: "12px", marginTop: "4px" }}>
								{errors.bidAmount}
							</span>
						)}
					</div>

					<button className="bidButton" onClick={placeBid} style={{ height: "fit-content" }}>
						Place Bid
					</button>
				</div>
			)}

			{item.sellerID === user.userid && (
				<p className="sellerNotice">You posted this item.</p>
			)}

			{bidMessage && <p className="bidMessage">{bidMessage}</p>}
		</div>
	);
}

export default CatalogueItemPage;
