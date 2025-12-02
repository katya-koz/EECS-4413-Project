const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const usernameRegex = /^[a-zA-Z0-9._-]+$/;

export const validateEmail = (email) => {

	if (!email || !emailRegex.test(email)) {
		return "You must enter a valid email address";
	}
	return null;
	

};

export const validateUsername = (username) => {

	if (!username || !usernameRegex.test(username)) {
		return "Please enter a valid username";
	}
	return null;
	

};

