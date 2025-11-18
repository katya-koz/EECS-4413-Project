import { useEffect, useMemo, useState } from "react";
import { useLocation, Link } from "react-router-dom";
import "./ResetPasswordPage.css";

const API_BASE = "http://localhost:8080";

async function postJSON(path, body) {
  const res = await fetch(API_BASE + path, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) {
    const message = data?.message || `Request failed (${res.status})`;
    throw new Error(message);
  }
  return data;
}

export default function ResetPasswordPage() {
  const location = useLocation();
  const qs = useMemo(() => new URLSearchParams(location.search), [location.search]);
  const token = qs.get("token");

  return (
    <div className="rp-container resetpw">
      <div className="rp-card">
        {!token ? <RequestResetForm /> : <ConfirmResetForm token={token} />}
        <div className="rp-footer">
          <Link to="/signin">Back to sign in</Link>
        </div>
      </div>
    </div>
  );
}

function RequestResetForm() {
  const [email, setEmail] = useState("");
  const [busy, setBusy] = useState(false);
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    setBusy(true);
    setError(null);
    setMessage(null);
    try {
      await postJSON("/api/auth/request-password-reset", { email });
      setMessage("If that email exists, a reset link has been sent.");
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <>
      <h1 className="rp-title">Reset your password</h1>
      <p className="rp-subtitle">
        Enter your account email. We’ll send a link to reset your password.
      </p>
      <form onSubmit={handleSubmit} className="rp-form">
        <label className="rp-label">
          Email
          <input
            className="rp-input"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="you@example.com"
            required
          />
        </label>

        <button className="rp-button" type="submit" disabled={busy}>
          {busy ? "Sending…" : "Send reset link"}
        </button>

        {message && <div className="rp-success" role="status">{message}</div>}
        {error && <div className="rp-error" role="alert">{error}</div>}
      </form>
    </>
  );
}

function ConfirmResetForm({ token }) {
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [show, setShow] = useState(false);
  const [busy, setBusy] = useState(false);
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    setMessage(null);
    setError(null);
  }, [password, confirm]);

  const valid =
    password.length >= 8 &&
    /\d/.test(password) &&
    /[A-Za-z]/.test(password) &&
    password === confirm;

  async function handleSubmit(e) {
    e.preventDefault();
    if (!valid) return;
    setBusy(true);
    setError(null);
    setMessage(null);
    try {
      await postJSON("/api/auth/reset-password", { token, newPassword: password });
      setMessage("Password updated. You can now sign in.");
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <>
      <h1 className="rp-title">Choose a new password</h1>
      <form onSubmit={handleSubmit} className="rp-form">
        <label className="rp-label">
          New password
          <div className="rp-password-row">
            <input
              className="rp-input"
              type={show ? "text" : "password"}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              required
            />
            <button
              type="button"
              className="rp-ghost"
              onClick={() => setShow((s) => !s)}
            >
              {show ? "Hide" : "Show"}
            </button>
          </div>
        </label>

        <label className="rp-label">
          Confirm password
          <input
            className="rp-input"
            type={show ? "text" : "password"}
            value={confirm}
            onChange={(e) => setConfirm(e.target.value)}
            placeholder="••••••••"
            required
          />
        </label>

        <PasswordHints password={password} confirm={confirm} />

        <button className="rp-button" type="submit" disabled={!valid || busy}>
          {busy ? "Saving…" : "Update password"}
        </button>

        {message && <div className="rp-success" role="status">{message}</div>}
        {error && <div className="rp-error" role="alert">{error}</div>}
      </form>
    </>
  );
}

function PasswordHints({ password, confirm }) {
  const rules = [
    { ok: password.length >= 8, text: "At least 8 characters" },
    { ok: /[A-Za-z]/.test(password), text: "Contains a letter" },
    { ok: /\d/.test(password), text: "Contains a number" },
    { ok: password === confirm && confirm.length > 0, text: "Passwords match" },
  ];
  return (
    <ul className="rp-hints">
      {rules.map((r) => (
        <li key={r.text} className={r.ok ? "ok" : ""}>
          {r.text}
        </li>
      ))}
    </ul>
  );
}
