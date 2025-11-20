import { useMemo, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const API_BASE = "http://localhost:8080";

async function postJSON(path, body) {
  const res = await fetch(API_BASE + path, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data?.message || `Request failed (${res.status})`);
  return data;
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
    password.length >= 8 && /[A-Za-z]/.test(password) && /\d/.test(password) && password === confirm && token.trim().length > 0;

  async function handleSubmit(e) {
    e.preventDefault();
    if (!valid) return;
    setBusy(true);
    setError("");
    setMessage("");
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
    <div style={{ minHeight:"100vh", display:"flex", justifyContent:"center", alignItems:"center", padding:"20px" }}>
      <div style={{ width:"100%", maxWidth:"400px", padding:"30px", border:"1px solid #ccc", borderRadius:"12px", boxShadow:"0 4px 10px rgba(0,0,0,0.1)", background:"#fff" }}>
        <h2 style={{ fontSize:"24px", fontWeight:"bold", marginBottom:"12px" }}>Set a new password</h2>
        <form onSubmit={handleSubmit} style={{ display:"grid", gap:"12px" }}>
          <label style={{ fontWeight:600 }}>
            Token
            <input
              type="text"
              value={token}
              onChange={(e)=>setToken(e.target.value)}
              placeholder="Paste reset token"
              style={{ padding:"10px", borderRadius:"6px", border:"1px solid #ccc" }}
              required
            />
          </label>
          <label style={{ fontWeight:600 }}>
            New password
            <input
              type="password"
              value={password}
              onChange={(e)=>setPassword(e.target.value)}
              placeholder="••••••••"
              style={{ padding:"10px", borderRadius:"6px", border:"1px solid #ccc" }}
              required
            />
          </label>
          <label style={{ fontWeight:600 }}>
            Confirm password
            <input
              type="password"
              value={confirm}
              onChange={(e)=>setConfirm(e.target.value)}
              placeholder="••••••••"
              style={{ padding:"10px", borderRadius:"6px", border:"1px solid #ccc" }}
              required
            />
          </label>

          <ul style={{ margin:"0 0 4px 18px", color:"#666", fontSize:"13px" }}>
            <li style={{ color: password.length >= 8 ? "#0f5132" : "#666" }}>At least 8 characters</li>
            <li style={{ color: /[A-Za-z]/.test(password) ? "#0f5132" : "#666" }}>Contains a letter</li>
            <li style={{ color: /\d/.test(password) ? "#0f5132" : "#666" }}>Contains a number</li>
            <li style={{ color: confirm && password === confirm ? "#0f5132" : "#666" }}>Passwords match</li>
          </ul>

          <button type="submit" disabled={!valid || busy} style={{ padding:"12px", borderRadius:"8px", background:"#007bff", color:"#fff", fontWeight:"bold", border:"none", cursor:"pointer" }}>
            {busy ? "Saving…" : "Update password"}
          </button>
        </form>

        {message && (
          <div style={{ marginTop:"12px", color:"#0f5132", background:"#d1e7dd", border:"1px solid #badbcc", padding:"10px 12px", borderRadius:"8px" }}>
            {message}
          </div>
        )}
        {error && (
          <div style={{ marginTop:"12px", color:"#842029", background:"#f8d7da", border:"1px solid #f5c2c7", padding:"10px 12px", borderRadius:"8px" }}>
            {error}
          </div>
        )}

        <button type="button" onClick={()=>navigate("/signin")} style={{ marginTop:"12px", background:"transparent", border:"none", color:"#007bff", textDecoration:"underline", cursor:"pointer" }}>
          Back to sign in
        </button>
      </div>
    </div>
  );
}
