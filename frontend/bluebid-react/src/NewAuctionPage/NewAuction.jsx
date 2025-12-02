import { useState } from "react";
import { useUser } from "../Context/UserContext";
import { useToast } from "../Context/ToastContext";
import "./NewAuction.scss";
import { validateFields } from "./NewAuctionValidations.js"

function NewAuction() {
	const [newAuctionForm, setNewAuctionForm] = useState({
		itemName: "",
		itemDescription: "",
		basePrice: "",
		seconds: "",
	});
	const { authFetch } = useUser();
	const { showToast } = useToast();
	const [errorMessage, setErrorMessage] = useState("");

	const [errors, setErrors] = useState({});

	async function postNewAuction() {
		const res = await authFetch(
			"http://localhost:8080/api/auction/new-auction",
			{
				method: "POST",
				body: JSON.stringify(newAuctionForm),
			}
		);

		if (!res.ok) {
			const data = await res.text();
			console.error("Fetch failed: " + data);
			setErrorMessage(data);
			return;
		}

		const data = await res.json();

		if (data.submitSuccess === false) {
			setErrorMessage(data);
		} else {
			setErrorMessage(data.message);
			for (let i = 0; i < 10; i++) {
				const res = await authFetch(
					`http://localhost:8080/api/auction/auctions/${data.auctionId}`
				);
				const auction = await res.json();

				if (auction.catalogueID) {
					showToast(`Auction created successfully for ${auction.itemName}!`);
					return;
				}

				await new Promise((resolve) => setTimeout(resolve, 200));
			}
		}
	}

	const handleSubmitNewAuction = (e) => {
		e.preventDefault();

		const formData = {
			...newAuctionForm
		};
		const validationErrors = validateFields(formData);

		if (Object.keys(validationErrors).length > 0) {
			setErrors(validationErrors);
			return;
		}
		setErrors({});
		
		postNewAuction();
	};

	const handleChange = (e) => {
		const { name, value } = e.target;

		setNewAuctionForm({
			...newAuctionForm,
			[name]: value,
		});


		if (errors[name]) {
			setErrors({
				...errors,
				[name]: null
			});
		}
	};

	return (
		<div className="auction-container">
			<div className="auction-card">
				<h2 className="auction-title">Post a New Item for Auction</h2>
				<form onSubmit={handleSubmitNewAuction} className="auction-form">
					<div className="input-group">
						<input
							type="text"
							name="itemName"
							placeholder="Item Name"
							value={newAuctionForm.itemName}
							onChange={handleChange}
							className="auction-input"
							style={{ borderColor: errors.itemName ? "red" : "" }}
						/>
						{errors.itemName && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.itemName}
							</span>
						)}
					</div>
					<div className="input-group">
						<input
							type="text"
							name="itemDescription"
							placeholder="Item Description"
							value={newAuctionForm.itemDescription}
							onChange={handleChange}
							className="auction-input"
							style={{ borderColor: errors.itemDescription ? "red" : "" }}
						/>
						{errors.itemDescription && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.itemDescription}
							</span>
						)}
					</div>
					<div className="auction-flex">
						<div className="input-group" style={{ flex: 1 }}>
							<input
								type="text"
								name="basePrice"
								placeholder="Starting Bid"
								value={newAuctionForm.basePrice}
								onChange={handleChange}
								className="auction-input"
								style={{ width: "100%", borderColor: errors.basePrice ? "red" : "" }}
							/>
							{errors.basePrice && (
								<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
									{errors.basePrice}
								</span>
							)}
						</div>

						<div className="input-group" style={{ flex: 1 }}>
							<input
								type="text"
								name="seconds"
								placeholder="Auction length (seconds for test)"
								value={newAuctionForm.seconds}
								onChange={handleChange}
								className="auction-input"
								style={{ width: "100%", borderColor: errors.seconds ? "red" : "" }}
							/>
							{errors.seconds && (
								<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
									{errors.seconds}
								</span>
							)}
						</div>
					</div>
					<button type="submit" className="auction-button">
						Post New Item
					</button>
				</form>
			</div>
			<p className="error-message">{errorMessage}</p>
		</div>
	);
}

export default NewAuction;
