import { useMemo, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./ResetPasswordConfirm.scss";

const API_BASE = "http://localhost:8080";

export default function ResetPasswordConfirmPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const qs = useMemo(
    () => new URLSearchParams(location.search),
    [location.search]
  );
  const [token, setToken] = useState(qs.get("token") || "");
  const [newPassword, setNewPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [busy, setBusy] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  // const valid =
  //   newPassword.length >= 8 &&
  //   /[A-Za-z]/.test(newPassword) &&
  //   /\d/.test(newPassword) &&
  //   newPassword === confirm &&
  //   token.trim().length > 0;

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setMessage("");
    setBusy(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/account/reset-password/${token}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ newPassword }),
        }
      );
      const data = await response.json();
      setMessage(data.message);
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="reset-container">
      <div className="reset-card">
        <h2 className="reset-title">Set a New Password</h2>
        <form onSubmit={handleSubmit} className="reset-form">
          <label className="reset-label">
            Token
            <input
              type="text"
              value={token}
              onChange={(e) => setToken(e.target.value)}
              placeholder="Paste Reset Token Here"
              className="reset-input"
              required
            />
          </label>
          <label className="reset-label">
            New password
            <input
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="••••••••"
              className="reset-input"
              required
            />
          </label>
          <label className="reset-label">
            Confirm Password
            <input
              type="password"
              value={confirm}
              onChange={(e) => setConfirm(e.target.value)}
              placeholder="••••••••"
              className="reset-input"
              required
            />
          </label>

          {/* <ul className="reset-password-criteria">
            <li className={newPassword.length >= 8 ? "valid" : ""}>
              At least 8 characters
            </li>
            <li className={/[A-Za-z]/.test(newPassword) ? "valid" : ""}>
              Contains a letter
            </li>
            <li className={/\d/.test(newPassword) ? "valid" : ""}>
              Contains a number
            </li>
            <li className={confirm && newPassword === confirm ? "valid" : ""}>
              Passwords match
            </li>
          </ul> */}

          <button
            type="submit"
            className="reset-button"
            // disabled={!valid || busy}
          >
            {busy ? "Saving…" : "Update Password"}
          </button>
        </form>

        {message && <div className="reset-message success">{message}</div>}
        {error && <div className="reset-message error">{error}</div>}

        <button
          type="button"
          className="reset-back"
          onClick={() => navigate("/login")}
        >
          Back to Log In
        </button>
      </div>
    </div>
  );
}
