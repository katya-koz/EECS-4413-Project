import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "../Context/UserContext";
import styles from "./SignIn.module.scss";

function SignIn() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useUser();
  const navigate = useNavigate();

  useEffect(() => { if (login.username != null) {} }, [login]);

  async function handleLogin() {
    const res = await fetch("/api/authentication/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });
    if (!res.ok) {
      const data = await res.json().catch(() => ({}));
      console.error("Login failed:", data.message || res.status);
      return;
    }
    const data = await res.json();
    login(data.token, { username: data.username }, data.expiresAt);
    navigate("/");
  }

  function handleSubmit(e) { e.preventDefault(); handleLogin(); }

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <h2 className={styles.title}>Sign In</h2>
        <form onSubmit={handleSubmit} className={styles.form}>
          <input
            type="username"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className={styles.input}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className={styles.input}
          />

          <button
            type="button"
            onClick={() => navigate("/reset-password/request")}
            className={styles.linkBtn}
            aria-label="Forgot password? Click here to reset it."
          >
            Forgot password?
          </button>

          <button type="submit" className={styles.primaryBtn}>Sign In</button>

          <button
            type="button"
            onClick={() => navigate("/signup")}
            className={styles.ghostBtn}
          >
            Create Account
          </button>
        </form>
      </div>
    </div>
  );
}
export default SignIn;

