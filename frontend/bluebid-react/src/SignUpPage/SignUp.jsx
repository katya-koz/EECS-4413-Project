import { useState } from "react";
import { useUser } from "../Context/UserContext";
import { useNavigate } from "react-router-dom";

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

  async function handleSignUp() {
    const username = form.username;
    const password = form.password;
    const firstName = form.firstName;
    const lastName = form.lastName;
    const streetName = form.streetName;
    const streetNum = form.streetNum;
    const city = form.city;
    const postalCode = form.postalCode;
    const country = form.country;
    const email = form.email;

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

    // if account was created successfully, log user in.
    handleLogin();
  }
  async function handleLogin() {
    const username = form.username;
    const password = form.password;
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
    // navigate to home page
    navigate("/");
  }

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        padding: "20px",
      }}
    >
      <div
        style={{
          width: "100%",
          maxWidth: "500px",
          padding: "30px",
          border: "1px solid #ccc",
          borderRadius: "12px",
          boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
        }}
      >
        <h2
          style={{ fontSize: "24px", fontWeight: "bold", marginBottom: "20px" }}
        >
          Create Account
        </h2>
        <form onSubmit={handleSubmit} style={{ display: "grid", gap: "15px" }}>
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={form.email}
            onChange={handleChange}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <input
            type="username"
            name="username"
            placeholder="Username / Display name"
            value={form.username}
            onChange={handleChange}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <div style={{ display: "flex", gap: "10px" }}>
            <input
              type="text"
              name="firstName"
              placeholder="First Name"
              value={form.firstName}
              onChange={handleChange}
              style={{
                flex: 1,
                padding: "10px",
                borderRadius: "6px",
                border: "1px solid #ccc",
              }}
            />
            <input
              type="text"
              name="lastName"
              placeholder="Last Name"
              value={form.lastName}
              onChange={handleChange}
              style={{
                flex: 1,
                padding: "10px",
                borderRadius: "6px",
                border: "1px solid #ccc",
              }}
            />
          </div>

          <input
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <h3 style={{ marginTop: "10px" }}>Shipping Information</h3>

          <input
            type="text"
            name="country"
            placeholder="Country"
            value={form.country}
            onChange={handleChange}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <div style={{ display: "flex", gap: "10px" }}>
            <input
              type="text"
              name="city"
              placeholder="City"
              value={form.city}
              onChange={handleChange}
              style={{
                flex: 1,
                padding: "10px",
                borderRadius: "6px",
                border: "1px solid #ccc",
              }}
            />
            <input
              type="text"
              name="postalCode"
              placeholder="Postal Code"
              value={form.postalCode}
              onChange={handleChange}
              style={{
                flex: 1,
                padding: "10px",
                borderRadius: "6px",
                border: "1px solid #ccc",
              }}
            />
          </div>

          <input
            type="text"
            name="streetName"
            placeholder="Street Name"
            value={form.streetName}
            onChange={handleChange}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <input
            type="text"
            name="streetNum"
            placeholder="Street #"
            value={form.streetNum}
            onChange={handleChange}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <button
            type="submit"
            style={{
              padding: "12px",
              borderRadius: "8px",
              backgroundColor: "#007bff",
              color: "white",
              fontWeight: "bold",
              border: "none",
              cursor: "pointer",
            }}
          >
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
}
export default SignUp;
