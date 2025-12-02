const validNumRegex = /^[1-9]\d*$/;

export const validateItemName = (itemName) => {

	if (!itemName) {
		return "Item Name is required";
	}
	
	return null;


};

export const validateItemDescription = (itemDescription) => {

	if (!itemDescription) {
		return "Item Description is required";
	}
	
	return null;


};

export const validatebasePrice = (basePrice) => {

	if (!basePrice) {
		return "Starting Bid is required";
	}
	
	if (!validNumRegex.test(basePrice.toString())) {
			return "Please enter a valid number";
		}
	return null;


};


export const validateseconds = (seconds) => {

	if (!seconds) {
		return "Auction Length is required";
	}
	
	if (!validNumRegex.test(seconds.toString())) {
			return "Please enter a valid number";
		}
	return null;


};

export const validateFields = (values) => {
	const errors = {};


	const itemNameError = validateItemName(values.itemName);
	const itemDescriptionError = validateItemDescription(values.itemDescription);
	const basePriceError = validatebasePrice(values.basePrice);
	const secondsError = validateseconds(values.seconds);
	
	if (itemNameError) {
		errors.itemName = itemNameError;
	}
	
	if (itemDescriptionError) {
		errors.itemDescription = itemDescriptionError;
	}
	
	if (basePriceError) {
		errors.basePrice = basePriceError;
	}
	
	if (secondsError) {
		errors.seconds = secondsError;
	}
	

	return errors;
};




