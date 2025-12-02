const validNumRegex = /^\d+(\.\d{1,2})?$/;

export const validateBidItem = (bidAmount) => {

	if (!bidAmount) {
		return "Bid Value is required";
	}
	if (!validNumRegex.test(bidAmount.toString())) {
		return "Enter a valid bid";
	}
	
	return null;
}

export const validateFields = (values) => {
	const errors = {};
	const bidItemError = validateBidItem(values.bidAmount);
	
	if (bidItemError){
		errors.bidAmount = bidItemError;
	}
	
	return errors;
}