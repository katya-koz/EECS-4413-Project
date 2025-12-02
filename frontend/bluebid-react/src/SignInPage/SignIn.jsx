import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "../Context/UserContext";
import "./SignIn.scss";
import { validateFields } from "./SignInValidations.js";

function SignIn() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [message, setMessage] = useState("");

	const [errors, setErrors] = useState({});


	const { login } = useUser();
	const navigate = useNavigate();

	useEffect(() => {
		if (login.username != null) {
			// redirect logic if already logged in
		}
	}, [login]);

	async function handleLogin() {
		setMessage("");

		const res = await fetch("http://localhost:8080/api/authentication/login", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ username, password }),
		});

		const data = await res.json();

		if (!res.ok || !data.token) {
			// show error from backend
			setMessage(data.message || "Login failed");
			return;
		}

		// successful login
		login(
			data.token,
			{ username: data.username, userid: data.userId },
			data.expiresAt
		);
		navigate("/");
	}

	const handleSubmit = (e) => {
		e.preventDefault();

		//validations
		const formData = { username, password };
		const validationErrors = validateFields(formData);

		if (Object.keys(validationErrors).length > 0) {
			setErrors(validationErrors);
			return;
		}
		setErrors({});
		handleLogin();
	};


	return (
		<div className="signInPage">
			<div className="signInCard">
				<h2>Sign In</h2>

				<button
					onClick={() =>
					(window.location.href =
						"http://localhost:8080/oauth2/authorization/github")
					}
					className="oauthBtn github"
				>
					<i className="bi bi-github"></i>
					Sign in with GitHub
				</button>

				<div className="orDivider">─── OR ───</div>

				<form onSubmit={handleSubmit} className="signInForm">

					<div className="input-group">
						<input
							type="username"
							placeholder="Username"
							value={username}
							onChange={(e) => {
								setUsername(e.target.value);
								
								if (errors.username) setErrors({ ...errors, username: null });
							}}
							
							style={{  width: "95%", borderColor: errors.username ? "red" : "" }}
						
						/>
						{/*display validations.js error*/}
						{errors.username && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.username}
							</span>
						)}
					</div>

					<input
					    type="password"
					    placeholder="Password"
					    value={password}
					    onChange={(e) => {
					        setPassword(e.target.value);
					        // clear password error when user types
					        if (errors.password) setErrors({ ...errors, password: null });
					    }}
					    style={{ borderColor: errors.password ? "red" : "" }}
					    
					/>
					{/*display validations.js error*/}
					{errors.password && (
					    <span className="error-text" style={{ color: "red", fontSize: "12px" }}>
					        {errors.password}
					    </span>
					)}

					{message && <p className="loginMessage">{message}</p>}

					<button
						type="button"
						onClick={() => navigate("/reset-password")}
						className="forgotPasswordBtn"
					>
						Forgot password?
					</button>

					<button type="submit" className="submitBtn">
						Sign In
					</button>

					<button
						type="button"
						onClick={() => navigate("/signup")}
						className="createAccountBtn"
					>
						Create Account
					</button>
				</form>
			</div>
		</div>
	);
}

export default SignIn;
