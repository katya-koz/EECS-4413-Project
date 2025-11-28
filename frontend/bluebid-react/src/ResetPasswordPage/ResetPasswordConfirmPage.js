import { useMemo, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styles from "./ResetPassword.module.scss";

/** Try several endpoints until one returns !404 */
async function postFirstWorking(candidates) {
  let lastErr;
  for (const { path, body } of candidates) {
    try {
      const res = await fetch(path, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });
      const data = await res.json().catch(() => ({}));

      if (res.status === 404) {
        lastErr = new Error(`Not found: ${path}`);
        continue; // try next candidate
      }
      if (!res.ok) throw new Error(data?.message || `Request failed (${res.status})`);
      return data;
    } catch (e) {
      lastErr = e;
      if (!(String(e?.message || "").includes("Not found"))) throw e;
    }
  }
  throw lastErr || new Error("No matching endpoint found");
}

export default function ResetPasswordConfirmPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const qs = useMemo(() => new URLSearchParams(location.search), [location.search]);

  const [token, setToken] = useState(qs.get("token") || "");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [busy, setBusy] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const valid =
    token.trim().length > 0 &&
    password.length >= 8 &&
    /[A-Za-z]/.test(password) &&
    /\d/.test(password) &&
    password === confirm;

  async function handleSubmit(e) {
    e.preventDefault();
    if (!valid) return;

    setBusy(true);
    setError("");
    setMessage("");

    try {
      await postFirstWorking([
        { path: "/api/authentication/reset-password", body: { token, newPassword: password } },
        { path: "/api/authentication/confirm-reset",  body: { token, newPassword: password } },
        { path: "/api/auth/reset-password",           body: { token, newPassword: password } },
      ]);

      setMessage("Password is Updated! You can now sign in.");
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className={styles.center}>
      <div className={styles.card}>
        <h2 className={styles.title}>Set a New Password</h2>

        <form onSubmit={handleSubmit} className={styles.form}>
          <label className={styles.label}>
            Token
            <input
              className={styles.input}
              type="text"
              value={token}
              onChange={(e)=>setToken(e.target.value)}
              placeholder="Paste Reset Token Here"
              required
            />
          </label>

          <label className={styles.label}>
            New password
            <input
              className={styles.input}
              type="password"
              value={password}
              onChange={(e)=>setPassword(e.target.value)}
              placeholder="••••••••"
              required
            />
          </label>

          <label className={styles.label}>
            Confirm Password
            <input
              className={styles.input}
              type="password"
              value={confirm}
              onChange={(e)=>setConfirm(e.target.value)}
              placeholder="••••••••"
              required
            />
          </label>

          <ul className={styles.hints}>
            <li style={{ color: password.length >= 8 ? "#0f5132" : "#666" }}>At least 8 characters</li>
            <li style={{ color: /[A-Za-z]/.test(password) ? "#0f5132" : "#666" }}>Contains a letter</li>
            <li style={{ color: /\d/.test(password) ? "#0f5132" : "#666" }}>Contains a number</li>
            <li style={{ color: confirm && password === confirm ? "#0f5132" : "#666" }}>Passwords match</li>
          </ul>

          <button type="submit" disabled={!valid || busy} className={styles.primaryBtn}>
            {busy ? "Saving…" : "Update Password"}
          </button>
        </form>

        {message && <div className={styles.success}>{message}</div>}
        {error && <div className={styles.error}>{error}</div>}

        <button
          type="button"
          onClick={()=>navigate("/signin")}
          className={styles.backLink}
        >
          Back to Sign In
        </button>
      </div>
    </div>
  );
}
