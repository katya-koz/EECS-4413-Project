import { useState } from "react";
import { useUser } from "../Context/UserContext";
import { useNavigate } from "react-router-dom";
import "./SignUp.scss";

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

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
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
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={form.email}
            onChange={handleChange}
            className="signup-input"
          />
          <input
            type="text"
            name="username"
            placeholder="Username / Display name"
            value={form.username}
            onChange={handleChange}
            className="signup-input"
          />

          <div className="signup-flex">
            <input
              type="text"
              name="firstName"
              placeholder="First Name"
              value={form.firstName}
              onChange={handleChange}
              className="signup-input"
            />
            <input
              type="text"
              name="lastName"
              placeholder="Last Name"
              value={form.lastName}
              onChange={handleChange}
              className="signup-input"
            />
          </div>

          <input
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            className="signup-input"
          />

          <h3 className="shipping-title">Shipping Information</h3>

          <input
            type="text"
            name="country"
            placeholder="Country"
            value={form.country}
            onChange={handleChange}
            className="signup-input"
          />

          <div className="signup-flex">
            <input
              type="text"
              name="city"
              placeholder="City"
              value={form.city}
              onChange={handleChange}
              className="signup-input"
            />
            <input
              type="text"
              name="postalCode"
              placeholder="Postal Code"
              value={form.postalCode}
              onChange={handleChange}
              className="signup-input"
            />
          </div>

          <input
            type="text"
            name="streetName"
            placeholder="Street Name"
            value={form.streetName}
            onChange={handleChange}
            className="signup-input"
          />
          <input
            type="text"
            name="streetNum"
            placeholder="Street #"
            value={form.streetNum}
            onChange={handleChange}
            className="signup-input"
          />

          <button type="submit" className="signup-button">
            Sign Up
          </button>
        </form>
        <button onClick={handleBack} className="signup-button">
          Back
        </button>
      </div>
    </div>
  );
}

export default SignUp;
