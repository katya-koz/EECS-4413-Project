import { useState } from "react";
import { useUser } from "../Context/UserContext";
import { useNavigate } from "react-router-dom";
import "./SignUp.scss";
import { validateFields } from "./SignUpValidations.js";

function SignUp() {
	const [form, setForm] = useState({
		email: "",
		firstName: "",
		lastName: "",
		password: "",
		country: "",
		city: "",
		postalCode: "",
		streetName: "",
		streetNum: "",
		username: "",
	});
	const navigate = useNavigate();
	const { login } = useUser();
	const [errors, setErrors] = useState({});

	const handleChange = (e) => {
		const { name, value } = e.target;

		setForm({
			...form,
			[name]: value,
		});


		if (errors[name]) {
			setErrors({
				...errors,
				[name]: null
			});
		}
	};

	const handleSubmit = (e) => {
		e.preventDefault();

		const formData = {
			...form
		};
		const validationErrors = validateFields(formData);

		if (Object.keys(validationErrors).length > 0) {
			setErrors(validationErrors);
			return;
		}
		setErrors({});


		handleSignUp();
	};

	const handleBack = (e) => {
		e.preventDefault();
		navigate("/login");
	};

	async function handleSignUp() {
		const {
			username,
			password,
			firstName,
			lastName,
			streetName,
			streetNum,
			city,
			postalCode,
			country,
			email,
		} = form;

		const res = await fetch("http://localhost:8080/api/account/signup", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({
				username,
				password,
				firstName,
				lastName,
				streetName,
				streetNum,
				city,
				postalCode,
				country,
				email,
			}),
		});

		if (!res.ok) {
			const message = await res.text();
			console.error("Create account failed: " + message);
			return;
		}

		handleLogin();
	}

	async function handleLogin() {
		const { username, password } = form;
		const res = await fetch("http://localhost:8080/api/authentication/login", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ username, password }),
		});

		if (!res.ok) {
			const data = await res.json();
			console.error("Login failed: " + data.message);
			return;
		}

		const data = await res.json();
		login(data.token, { username: data.username }, data.expiresAt);
		navigate("/");
	}

	return (
		<div className="signup-container">
			<div className="signup-card">
				<h2 className="signup-title">Create Account</h2>
				<form onSubmit={handleSubmit} className="signup-form">

					<div className="input-group">
						<input
							type="text"
							name="email"
							placeholder="Email"
							value={form.email}
							onChange={handleChange}
							className="signup-input"
							style={{ borderColor: errors.email ? "red" : "" }}
						/>
						{errors.email && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.email}
							</span>
						)}
					</div>

					<div className="input-group">
						<input
							type="text"
							name="username"
							placeholder="Username / Display name"
							value={form.username}
							onChange={handleChange}
							className="signup-input"
							style={{ borderColor: errors.username ? "red" : "" }}
						/>
						{errors.username && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.username}
							</span>
						)}
					</div>

					<div className="signup-flex">
						<div className="input-group" style={{ flex: 1 }}>
							<input
								type="text"
								name="firstName"
								placeholder="First Name"
								value={form.firstName}
								onChange={handleChange}
								className="signup-input"
								style={{ borderColor: errors.firstName ? "red" : "" }}
							/>
							{errors.firstName && (
								<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
									{errors.firstName}
								</span>
							)}
						</div>

						<div className="input-group" style={{ flex: 1 }}>
							<input
								type="text"
								name="lastName"
								placeholder="Last Name"
								value={form.lastName}
								onChange={handleChange}
								className="signup-input"
								style={{ borderColor: errors.lastName ? "red" : "" }}
							/>
							{errors.lastName && (
								<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
									{errors.lastName}
								</span>
							)}
						</div>
					</div>

					<div className="input-group">
						<input
							type="password"
							name="password"
							placeholder="Password"
							value={form.password}
							onChange={handleChange}
							className="signup-input"
							style={{ borderColor: errors.password ? "red" : "" }}
						/>
						{errors.password && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.password}
							</span>
						)}
					</div>

					<h3 className="shipping-title">Shipping Information</h3>

					<div className="input-group">
						<input
							type="text"
							name="country"
							placeholder="Country"
							value={form.country}
							onChange={handleChange}
							className="signup-input"
							style={{ borderColor: errors.country ? "red" : "" }}
						/>
						{errors.country && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.country}
							</span>
						)}
					</div>

					<div className="signup-flex">
						<div className="input-group" style={{ flex: 1 }}>
							<input
								type="text"
								name="city"
								placeholder="City"
								value={form.city}
								onChange={handleChange}
								className="signup-input"
								style={{ borderColor: errors.city ? "red" : "" }}
							/>
							{errors.city && (
								<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
									{errors.city}
								</span>
							)}
						</div>
						<div className="input-group" style={{ flex: 1 }}>
							<input
								type="text"
								name="postalCode"
								placeholder="Postal Code"
								value={form.postalCode}
								onChange={handleChange}
								className="signup-input"
								style={{ borderColor: errors.postalCode ? "red" : "" }}
							/>
							{errors.postalCode && (
								<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
									{errors.postalCode}
								</span>
							)}
						</div>
					</div>

					<div className="input-group">
						<input
							type="text"
							name="streetName"
							placeholder="Street Name"
							value={form.streetName}
							onChange={handleChange}
							className="signup-input"
							style={{ borderColor: errors.streetName ? "red" : "" }}
						/>
						{errors.streetName && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.streetName}
							</span>
						)}
					</div>

					<div className="input-group">
						<input
							type="text"
							name="streetNum"
							placeholder="Street #"
							value={form.streetNum}
							onChange={handleChange}
							className="signup-input"
							style={{ borderColor: errors.streetNum ? "red" : "" }}
						/>
						{errors.streetNum && (
							<span className="error-text" style={{ color: "red", fontSize: "12px" }}>
								{errors.streetNum}
							</span>
						)}
					</div>

					<div style={{ marginTop: "20px", display: "flex", flexDirection: "column", gap: "10px" }}>

						<button type="submit" className="signup-button">
							Sign Up
						</button>

						<button
							type="button"
							onClick={handleBack}
							className="signup-button"
							style={{ backgroundColor: "#6c757d" }} 
						>
							Back
						</button>
					</div>

				</form>
			</div>
		</div>
	);
}
export default SignUp;
