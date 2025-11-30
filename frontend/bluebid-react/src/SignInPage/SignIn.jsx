import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "../Context/UserContext";
import { useLocation } from "react-router-dom";

// http://localhost:3000/login?oauthUserId=119365237&email=null
// user is redirected to that url after a successful oauth response.
// we need to post login again with the oauthuserid to finish logging in.

function SignIn() {
  //const location = useLocation();
  //const query = new URLSearchParams(location.search);

  // const uid = query.get("uid");
  // const uname = query.get("uname");
  // const jwt = query.get("jwt");
  // console.log(uid, uname, jwt);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const { login } = useUser();

  const navigate = useNavigate();

  useEffect(() => {
    // redirect to home page if user is already logged in
    if (login.username != null) {
    }
  }, [login]);

  async function handleLogin() {
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

    login(
      data.token,
      { username: data.username, userid: data.userId },
      data.expiresAt
    );
    // navigate to home page
    navigate("/");
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    handleLogin();
  };

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
          maxWidth: "400px",
          padding: "30px",
          border: "1px solid #ccc",
          borderRadius: "12px",
          boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
        }}
      >
        <h2
          style={{ fontSize: "24px", fontWeight: "bold", marginBottom: "20px" }}
        >
          Sign In
        </h2>

        <button
          onClick={() =>
            (window.location.href =
              "http://localhost:8080/oauth2/authorization/github")
          }
          style={{
            width: "100%",
            padding: "12px",
            marginBottom: "10px",
            borderRadius: "8px",
            border: "1px solid #333",
            background: "black",
            color: "white",
            fontWeight: "bold",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            gap: "10px",
            cursor: "pointer",
          }}
        >
          <i className="bi bi-github"></i>
          Sign in with GitHub
        </button>

        {/* <button
          onClick={() =>
            (window.location.href =
              "http://localhost:8080/api/auth/oauth/google")
          }
          style={{
            width: "100%",
            padding: "12px",
            borderRadius: "8px",
            border: "1px solid #ccc",
            background: "white",
            color: "black",
            fontWeight: "bold",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            gap: "10px",
            cursor: "pointer",
            marginBottom: "20px",
          }}
        >
          <i className="bi bi-google"></i>
          Sign in with Google
        </button> */}

        <div
          style={{
            textAlign: "center",
            margin: "10px 0",
            color: "#888",
            fontSize: "14px",
          }}
        >
          ─── OR ───
        </div>

        <form onSubmit={handleSubmit} style={{ display: "grid", gap: "15px" }}>
          <input
            type="username"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{
              padding: "10px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <button
            type="button"
            onClick={() => navigate("/reset-password")}
            style={{
              justifySelf: "end",
              background: "transparent",
              border: "none",
              color: "#007bff",
              textDecoration: "underline",
              cursor: "pointer",
              padding: 0,
              marginTop: "-6px",
            }}
          >
            Forgot password?
          </button>

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
            Sign In
          </button>

          <button
            onClick={() => navigate("/signup")}
            style={{
              marginTop: "15px",
              padding: "10px",
              width: "100%",
              borderRadius: "8px",
              border: "1px solid #007bff",
              background: "white",
              color: "#007bff",
              fontWeight: "bold",
              cursor: "pointer",
            }}
          >
            Create Account
          </button>
        </form>
      </div>
    </div>
  );
}

export default SignIn;
