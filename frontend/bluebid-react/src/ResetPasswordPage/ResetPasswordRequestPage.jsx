import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ResetPasswordRequest.scss";

const API_BASE = "http://localhost:8080";

async function postJSON(path, body) {
  const res = await fetch(API_BASE + path, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok)
    throw new Error(data?.message || `Request failed (${res.status})`);
  return data;
}

export default function ResetPasswordRequestPage() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [busy, setBusy] = useState(false);
  const [token, setToken] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setBusy(true);
    setError("");
    setToken("");
    try {
      const data = await postJSON("/api/account/forgot-password", {
        email,
        username,
      });
      setToken(data.token || "");
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  function goToConfirm() {
    if (!token) return;
    navigate(`/reset-password/confirm?token=${encodeURIComponent(token)}`);
  }

  return (
    <div className="reset-container">
      <div className="reset-card">
        <h2 className="reset-title">Reset Your Password</h2>
        <p className="reset-subtitle">
          Enter your account email. We’ll return a reset token.
        </p>

        <form onSubmit={handleSubmit} className="reset-form">
          <label className="reset-label">
            Email
            <input
              type="email"
              placeholder="name@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="reset-input"
            />
          </label>
          <label className="reset-label">
            Username
            <input
              type="username"
              placeholder="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              className="reset-input"
            />
          </label>
          <button type="submit" disabled={busy} className="reset-button">
            {busy ? "Requesting…" : "Request Reset Token"}
          </button>
        </form>

        {error && <div className="reset-message error">{error}</div>}

        {token && (
          <div className="reset-token-section">
            <div className="reset-message success">
              Token received:
              <code className="reset-token">{token}</code>
            </div>
            <div className="reset-token-actions">
              <button
                type="button"
                onClick={() => navigator.clipboard?.writeText(token)}
                className="reset-copy-button"
              >
                Copy token
              </button>
              <button
                type="button"
                onClick={goToConfirm}
                className="reset-go-button"
              >
                Go to confirm page
              </button>
            </div>
          </div>
        )}

        <button
          type="button"
          onClick={() => navigate("/login")}
          className="reset-back"
        >
          Back to Sign In
        </button>
      </div>
    </div>
  );
}
