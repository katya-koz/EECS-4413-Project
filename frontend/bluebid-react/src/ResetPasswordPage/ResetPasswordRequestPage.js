import { useState } from "react";
import { useNavigate } from "react-router-dom";

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
      const data = await postJSON("/api/auth/request-password-reset", { email });
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
    <div style={{ minHeight:"100vh", display:"flex", justifyContent:"center", alignItems:"center", padding:"20px" }}>
      <div style={{ width:"100%", maxWidth:"400px", padding:"30px", border:"1px solid #ccc", borderRadius:"12px", boxShadow:"0 4px 10px 	rgba(0,0,0,0.1)", background:"#fff" }}>
        <h2 style={{ fontSize:"24px", fontWeight:"bold", marginBottom:"12px" }}>Reset Your Password</h2>
        <p style={{ marginTop:"-6px", marginBottom:"16px", color:"#555" }}>
          Enter your account email. We’ll return a reset token.
        </p>

        <form onSubmit={handleSubmit} style={{ display:"grid", gap:"12px" }}>
          <label style={{ fontWeight:600 }}>
            Email
            <input
              type="email"
              placeholder="name@example.com"
              value={email}
              onChange={(e)=>setEmail(e.target.value)}
              required
              style={{ padding:"10px", borderRadius:"6px", border:"1px solid #ccc" }}
            />
          </label>
          <button type="submit" disabled={busy} style={{ padding:"12px", borderRadius:"8px", background:"#007bff", color:"#fff", fontWeight:"bold", border:"none", cursor:"pointer" }}>
            {busy ? "Requesting…" : "Request Reset Token"}
          </button>
        </form>

        {error && (
          <div style={{ marginTop:"12px", color:"#842029", background:"#f8d7da", border:"1px solid #f5c2c7", padding:"10px 12px", borderRadius:"8px" }}>
            {error}
          </div>
        )}

        {token && (
          <div style={{ marginTop:"12px" }}>
            <div style={{ fontSize:14, color:"#0f5132", background:"#d1e7dd", border:"1px solid #badbcc", padding:"10px 12px", borderRadius:"8px" }}>
              Token received:
              <code style={{ display:"block", marginTop:6, wordBreak:"break-all" }}>{token}</code>
            </div>
            <div style={{ display:"flex", gap:8, marginTop:10 }}>
              <button
                type="button"
                onClick={() => navigator.clipboard?.writeText(token)}
                style={{ padding:"10px 12px", borderRadius:"8px", border:"1px solid #ccc", background:"#fff", cursor:"pointer" }}
              >
                Copy token
              </button>
              <button
                type="button"
                onClick={goToConfirm}
                style={{ padding:"10px 12px", borderRadius:"8px", background:"#111827", color:"#fff", border:"none", cursor:"pointer" }}
              >
                Go to confirm page
              </button>
            </div>
          </div>
        )}

        <button type="button" onClick={()=>navigate("/signin")} style={{ marginTop:"12px", background:"transparent", border:"none", color:"#007bff", 	textDecoration:"underline", cursor:"pointer" }}>
          Back to Sign In
        </button>
      </div>
    </div>
  );
}
