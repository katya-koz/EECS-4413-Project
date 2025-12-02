const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const usernameRegex = /^[a-zA-Z0-9._-]+$/;
const nameRegex = /^[a-zA-Z\s\-']+$/;
const streetNumRegex = /^[1-9]\d*$/;

export const validateEmail = (email) => {

	if (!email) {
		return "Email is required";
	}

	if (!emailRegex.test(email)) {
		return "You must enter a valid email address";
	}
	return null;


};

export const validateUsername = (username) => {

	if (!username) {
		return "Username is required";
	}
	if (!usernameRegex.test(username)) {
		return "Please enter a valid username";
	}
	return null;


};


export const validateFirstName = (firstName) => {
	if (!firstName) {
		return "First Name is required";
	}
	if (!nameRegex.test(firstName)) {
		return "Please enter a valid first name";
	}
	return null;
};

export const validateLastName = (lastName) => {
	if (!lastName) {
		return "Last Name is required";
	}
	if (!nameRegex.test(lastName)) {
		return "Please enter a valid last name";
	}
	return null;
};

export const validateCountry = (country) => {
	if (!country) {
		return "Country is required";
	}

};

export const validateCity = (city) => {
	if (!city) {
		return "City is required";
	}

	return null;
};

export const validatePostalCode = (postalCode) => {
	if (!postalCode) {
		return "Postal Code is required";
	}

	return null;
};

export const validateStreetName = (streetName) => {
	if (!streetName) {
		return "Street Name is required";
	}

	return null;
};

export const validatestreetNum = (streetNum) => {

	if (!streetNum) {
		return "Street Number is required";
	}

	if (!streetNumRegex.test(streetNum.toString())) {
		return "Please enter a valid number";
	}

	return null;
};

export const validatePassword = (password) => {

	if (!password) {
		return "Password is required";
	}
	return null;


};

export const validateFields = (values) => {
	const errors = {};


	const emailError = validateEmail(values.email);
	const usernameError = validateUsername(values.username);
	const firstNameError = validateFirstName(values.firstName);
	const lastNameError = validateLastName(values.lastName);
	const countryError = validateCountry(values.country);
	const cityError = validateCity(values.city);
	const postalCodeError = validatePostalCode(values.postalCode);
	const streetNameError = validateStreetName(values.streetName);
	const streetNumError = validatestreetNum(values.streetNum);
	const passwordError = validatePassword(values.password);

	if (emailError) {
		errors.email = emailError;
	}
	if (usernameError) {
		errors.username = usernameError;
	}
	if (firstNameError) {
		errors.firstName = firstNameError;
	}
	if (lastNameError) {
		errors.lastName = lastNameError;
	}
	if (countryError) {
		errors.country = countryError;
	}
	if (cityError) {
		errors.city = cityError;
	}
	if (postalCodeError) {
		errors.postalCode = postalCodeError;
	}
	if (streetNameError) {
		errors.streetName = streetNameError;
	}
	if (streetNumError) {
		errors.streetNum = streetNumError;
	}
	if (passwordError) {
		errors.password = passwordError;
	}

	return errors;
};
