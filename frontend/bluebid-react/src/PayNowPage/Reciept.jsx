import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useUser } from "../Context/UserContext";
import { useEffect, useState } from "react";

function Receipt() {

	const { id } = useParams();
	const { authFetch } = useUser();
	const { state } = useLocation();
	const navigate = useNavigate();

	const [receipt, setReceipt] = useState(null);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);
	const location = useLocation();
	const {itemName} = location.state;


	useEffect(() => {


		async function getReceipt() {
			try {
				const response = await authFetch(`http://localhost:8080/api/payment/receipt/${id}`);

				if (response.ok) {
					const data = await response.json();
					console.log("RECEIPT DETAILS", data);
					
					
					setReceipt(data);

				}
				else {
					setError("Could not load receipt");
				}
			} catch (err) {
				setError("Network error");
			} finally {
				setLoading(false);
			}
		}

		if (id) {
			getReceipt();
		}
	}, [id, authFetch]);


	if (loading) return <p>Loading receipt...</p>;

	if (error || !receipt) {
		return (
			<div style={{ padding: "20px", textAlign: "center" }}>
				<h2>No receipt found</h2>
				<p>ID: {id}</p>
				<p>{error}</p>
				<button onClick={() => navigate("/")}>Go Home</button>
			</div>
		);
	}

	const totalPrice = receipt.itemCost + receipt.shippingCost;

	return (
		<div
			style={{
				maxWidth: "500px",
				margin: "40px auto",
				padding: "20px",
				border: "1px solid #ccc",
				borderRadius: "12px",
			}}
		>
			<h1>Payment Receipt</h1>
			
			{/*<p>
				<strong>Item ID:</strong> {receipt.itemId}
			</p>
			*/}
			<p>
				<strong>Item:</strong> {itemName}
			</p>
			
			<p>
				<strong>Amount Paid: </strong> ${totalPrice}
			</p>
			<p>
				<strong>Date:</strong> {new Date(receipt.timestamp).toLocaleString()}
			</p>
			<p>
							<strong>Transaction ID:</strong> {receipt.paymentId}
						</p>

			<button
				onClick={() => navigate("/")}
				style={{ marginTop: "20px", padding: "10px" }}
			>
				Go to Home
			</button>
		</div>
	);
}

export default Receipt;
