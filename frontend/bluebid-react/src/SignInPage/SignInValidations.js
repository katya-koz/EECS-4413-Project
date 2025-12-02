const usernameRegex = /^[a-zA-Z0-9._-]+$/;


export const validateUsername = (username) => {

	if (!username || !usernameRegex.test(username)) {
		return "Please enter a valid username";
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
	
	const usernameError = validateUsername(values.username);
	const passwordError = validatePassword(values.password);
	
	if (!values.username || values.username.trim() === "")
		{
			errors.username = "Username is required";
		}
		if (!values.password || values.password.trim() === "")
				{
					errors.password = "Username is required";
				}
	if (usernameError)
		{
			errors.username = usernameError;
		}
		if (passwordError)
			{
				errors.password = passwordError;
			}
		
		
		
		return errors;
}