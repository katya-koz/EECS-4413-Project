import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

// pay now page
function PayNow() {
  const { id } = useParams();
  const [itemPrice, setItemPrice] = useState(0);
  const [shippingCost, setShippingCost] = useState(5);
  const [loading, setLoading] = useState(true);
  const [sellerID, setSellerID] = useState("");
  const [catalogueID, setCatalogueID] = useState("");
  const [isExpedited, setIsExpidited] = useState(false);

  const [cardNumber, setCardNumber] = useState("");
  const [cvv, setCvv] = useState("");
  const [expiryMonth, setExpiryMonth] = useState("01");
  const [expiryYear, setExpiryYear] = useState(new Date().getFullYear().toString());
  const [checked, setChecked] = useState(false);
  const validMonths = [
	"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
  ];
  
  const currentYear = new Date().getFullYear();
  const years = Array.from({ length : 10 }, (_, i) => (currentYear + i).toString());
  const handleChange = (e) => {
      const isChecked = e.target.checked; 
      
      setChecked(isChecked);

      if (isChecked) {
          setShippingCost(10);
		  setIsExpidited(true);
      } else {
          setShippingCost(5);
		  setIsExpidited(false);
      }
  };

  useEffect(() => {
    // fetch values here, set to temp for now
    async function getAuctionDetails() {
		try{
			const response = await fetch(`http://localhost:8080/api/auction/auctions/${id}`);
			
			if (response.ok) {
				const data = await response.json();
				
				//set values here
				setItemPrice(data.itemPrice);
				//setShippingCost(data.shippingCost);
				setSellerID(data.sellerID);
				setCatalogueID(data.catalogueID);
			}
		} catch (error) {
			console.log("failed to fetch auction item");
		} finally{
			setLoading(false);
		}
	}
	
	if (id){
		getAuctionDetails();
	}
	
  }, [id]);

  const total = itemPrice + shippingCost;
  const navigate = useNavigate();

  const submitPayment = async () => {
	const paymentRequest = {
		"cardNumber":  cardNumber,
		"expiryMonth": expiryMonth,
		"expiryYear": expiryYear,
		"securityCode": cvv,
		"itemPrice": itemPrice,
		"sellerID": sellerID,
		"catalogueID": catalogueID,
		"isExpedited": isExpedited,
		"shippingCost": shippingCost,
		"paymentTime": new Date().toISOString().slice(0, 19)
	};
	//attempt to submit payment
	
	try {
	        const response = await fetch("http://localhost:8080/api/payment/payment", {
	            method: "POST",
	            headers: { "Content-Type": "application/json" },
	            body: JSON.stringify(paymentRequest)
	        });
			
			if (response.ok)
				{
					const responseData = await response.json();
					
					const paymentID = responseData.paymentId;
					navigate(`/receipt/${paymentID}`);
				}
				else
				{
					alert("Payment failed. Please check your details and try again!");
				}
			
	}
	
	catch (error){
		console.error("Payment error: ", error);
	}
  };
  
  if (loading) return <p> Loading payment details... </p>;
  
  

  return (
    <div style={{ maxWidth: "400px", margin: "40px auto" }}>
      <h1>Pay for Item {id}</h1>

      <div
        style={{
          border: "1px solid #ddd",
          borderRadius: "8px",
          padding: "15px",
          marginTop: "20px",
          marginBottom: "25px",
          background: "#f9f9f9",
        }}
      >
        <h2>Order Summary</h2>
        <div style={{ marginTop: "10px" }}>
          <p>
            Item Price: <strong>${itemPrice.toFixed(2)}</strong>
          </p>
		  <div style={{ margin: "10px 0" }}>
		                <label style={{ display: "flex", alignItems: "center", cursor: "pointer" }}>
		                    <input 
		                        type="checkbox"
		                        checked={checked}
		                        onChange={handleChange}
		                        style={{ marginRight: "8px" }}
		                    />
		                    Expedited Shipping (Add $5.00)
		                </label>
		            </div>
          <p>
            Shipping: <strong>${shippingCost}</strong>
          </p>
          <hr />
          <p style={{ fontSize: "18px" }}>
            Total: <strong>${total}</strong>
          </p>
        </div>
      </div>

      <div style={{ display: "flex", flexDirection: "column", gap: "15px" }}>
        <div>
          <label>Card Number</label>
          <input
            type="text"
            placeholder="1234 5678 1234 5678"
            value={cardNumber}
            onChange={(e) => setCardNumber(e.target.value)}
            style={{ width: "100%", padding: "10px", marginTop: "5px" }}
          />
        </div>

        <div>
          <label>Security Code (CVV)</label>
          <input
            type="text"
            placeholder="123"
            value={cvv}
            onChange={(e) => setCvv(e.target.value)}
            style={{ width: "100%", padding: "10px", marginTop: "5px" }}
          />
        </div>

		<div>
		          <label>Expiry Date</label>
		          <div style={{ display: "flex", gap: "10px", marginTop: "5px" }}>
		            
		          
		            <select 
		              value={expiryMonth} 
		              onChange={(e) => setExpiryMonth(e.target.value)}
		              style={{ width: "50%", padding: "10px" }}
		            >
		              {validMonths.map(m => (
		                <option key={m} value={m}>{m}</option>
		              ))}
		            </select>

		          
		            <select 
		              value={expiryYear} 
		              onChange={(e) => setExpiryYear(e.target.value)}
		              style={{ width: "50%", padding: "10px" }}
		            >
		              {years.map(y => (
		                <option key={y} value={y}>{y}</option>
		              ))}
		            </select>
		          
		          </div>
		        </div>

        <button
          onClick={submitPayment}
          style={{ padding: "12px", marginTop: "20px", fontSize: "16px" }}
        >
          Submit Payment
        </button>
      </div>
    </div>
  );
}

export default PayNow;
