import { useState } from "react";
import { useNavigate } from "react-router-dom";
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
        // try next candidate
        lastErr = new Error(`Not found: ${path}`);
        continue;
      }
      if (!res.ok) {
        throw new Error(data?.message || `Request failed (${res.status})`);
      }
      return data;
    } catch (e) {
      lastErr = e;
      // continue if 404; otherwise surface the error
      if (!(String(e?.message || "").includes("Not found"))) throw e;
    }
  }
  throw lastErr || new Error("No matching endpoint found");
}

export default function ResetPasswordRequestPage() {
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
      // all paths are relative so they go through CRA proxy -> gateway:8080
      const data = await postFirstWorking([
        { path: "/api/authentication/request-password-reset", body: { email } },
        { path: "/api/authentication/forgot-password",        body: { email } },
        { path: "/api/auth/request-password-reset",           body: { email } }
      ]);

      const t = data.token || data.resetToken || data.value || "";
      if (!t) throw new Error("Server did not return a token");
      setToken(t);
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
    <div className={styles.center}>
      <div className={styles.card}>
        <h2 className={styles.title}>Reset Your Password</h2>
        <p className={styles.subtitle}>
          Enter your account email. We’ll return a reset token.
        </p>

        <form onSubmit={handleSubmit} className={styles.form}>
          <label className={styles.label}>
            Email
            <input
              className={styles.input}
              type="email"
              placeholder="name@example.com"
              value={email}
              onChange={(e)=>setEmail(e.target.value)}
              required
            />
          </label>

          <button type="submit" disabled={busy} className={styles.primaryBtn}>
            {busy ? "Requesting…" : "Request Reset Token"}
          </button>
        </form>

        {error && <div className={styles.error}>{error}</div>}

        {token && (
          <div className={styles.tokenBox}>
            <div className={styles.success}>
              Token received:
              <code className={styles.tokenCode}>{token}</code>
            </div>
            <div className={styles.actionsRow}>
              <button
                type="button"
                onClick={() => navigator.clipboard?.writeText(token)}
                className={styles.ghostBtn}
              >
                Copy token
              </button>
              <button
                type="button"
                onClick={goToConfirm}
                className={`${styles.ghostBtn} ${styles.darkBtn}`}
              >
                Go to confirm page
              </button>
            </div>
          </div>
        )}

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
