import { useState } from "react";
import { useUser } from "../Context/UserContext";
import { useNavigate } from "react-router-dom";
import styles from "./SignUp.module.scss";

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

    const res = await fetch("/api/account/signup", {
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
      const message = await res.text().catch(() => "");
      console.error("Create account failed: " + (message || res.status));
      return;
    }

    // if account was created successfully, log user in.
    handleLogin();
  }

  async function handleLogin() {
    const { username, password } = form;

    const res = await fetch("/api/authentication/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (!res.ok) {
      const data = await res.json().catch(() => ({}));
      console.error("Login failed: " + (data.message || res.status));
      return;
    }

    const data = await res.json();
    login(data.token, { username: data.username }, data.expiresAt);
    navigate("/");
  }

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <h2 className={styles.title}>Create Account</h2>

        <form onSubmit={handleSubmit} className={styles.form}>
          <input
            className={styles.input}
            type="email"
            name="email"
            placeholder="Email"
            value={form.email}
            onChange={handleChange}
          />

          <input
            className={styles.input}
            type="text"
            name="username"
            placeholder="Username / Display name"
            value={form.username}
            onChange={handleChange}
          />

          <div className={styles.row}>
            <input
              className={styles.input}
              type="text"
              name="firstName"
              placeholder="First Name"
              value={form.firstName}
              onChange={handleChange}
            />
            <input
              className={styles.input}
              type="text"
              name="lastName"
              placeholder="Last Name"
              value={form.lastName}
              onChange={handleChange}
            />
          </div>

          <input
            className={styles.input}
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
          />

          <h3 className={styles.sectionHeading}>Shipping Information</h3>

          <input
            className={styles.input}
            type="text"
            name="country"
            placeholder="Country"
            value={form.country}
            onChange={handleChange}
          />

          <div className={styles.row}>
            <input
              className={styles.input}
              type="text"
              name="city"
              placeholder="City"
              value={form.city}
              onChange={handleChange}
            />
            <input
              className={styles.input}
              type="text"
              name="postalCode"
              placeholder="Postal Code"
              value={form.postalCode}
              onChange={handleChange}
            />
          </div>

          <input
            className={styles.input}
            type="text"
            name="streetName"
            placeholder="Street Name"
            value={form.streetName}
            onChange={handleChange}
          />

          <input
            className={styles.input}
            type="text"
            name="streetNum"
            placeholder="Street #"
            value={form.streetNum}
            onChange={handleChange}
          />

          <button type="submit" className={styles.primaryBtn}>
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
}

export default SignUp;
