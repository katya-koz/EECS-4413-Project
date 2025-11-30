import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "../Context/UserContext";
import { useLocation } from "react-router-dom";

function OAuthSuccess() {
  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const navigate = useNavigate();
  const { login, authFetch } = useUser();

  const uid = query.get("uid");
  const uname = query.get("uname");
  const email = query.get("email");
  const hasShippingInfo = query.get("hasshipinfo") === "true"; // boolean
  const hasName = query.get("hasname") === "true"; // boolean
  const jwt = query.get("jwt");
  const expiresAt = query.get("expires");

  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({
    username: uname || "",
    firstName: "",
    lastName: "",
    country: "",
    city: "",
    postalCode: "",
    streetName: "",
    streetNum: "",
    email: email,
  });

  useEffect(() => {
    setLoading(false);
    login(jwt, { username: uname, userid: uid }, expiresAt);
    if (jwt && uname && uid && expiresAt && hasName && hasShippingInfo) {
      navigate("/");
    }
    return;
  }, [uname, jwt, uid, expiresAt, hasName, hasShippingInfo]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    login(jwt, { username: form.username || uname, userid: uid }, expiresAt); // log in with what we hve so we can use the jwt right away

    try {
      const res = await authFetch(`http://localhost:8080/api/account/profile`, {
        method: "PUT",
        body: JSON.stringify(form),
      });

      if (!res.ok) {
        throw new Error("Failed to save additional info");
      }

      login(jwt, { username: form.username || uname, userid: uid }, expiresAt);

      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Failed to save additional info. Please try again.");
    }
  };

  if (loading) return <p>We're getting things ready for you...</p>;
  else
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
            maxWidth: "500px",
            padding: "30px",
            border: "1px solid #ccc",
            borderRadius: "12px",
            boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
          }}
        >
          <h2
            style={{
              fontSize: "24px",
              fontWeight: "bold",
              marginBottom: "20px",
            }}
          >
            Complete Your Profile
          </h2>
          <form
            onSubmit={handleSubmit}
            style={{ display: "grid", gap: "15px" }}
          >
            {(!uname || uname.trim() === "") && (
              <input
                type="text"
                name="username"
                placeholder="Username / Display Name"
                value={form.username}
                onChange={handleChange}
                style={{
                  padding: "10px",
                  borderRadius: "6px",
                  border: "1px solid #ccc",
                }}
              />
            )}

            {!hasName && (
              <div style={{ display: "flex", gap: "10px" }}>
                <input
                  type="text"
                  name="firstName"
                  placeholder="First Name"
                  value={form.firstName}
                  onChange={handleChange}
                  style={{
                    flex: 1,
                    padding: "10px",
                    borderRadius: "6px",
                    border: "1px solid #ccc",
                  }}
                />
                <input
                  type="text"
                  name="lastName"
                  placeholder="Last Name"
                  value={form.lastName}
                  onChange={handleChange}
                  style={{
                    flex: 1,
                    padding: "10px",
                    borderRadius: "6px",
                    border: "1px solid #ccc",
                  }}
                />
              </div>
            )}

            {!hasShippingInfo && (
              <>
                <h3 style={{ marginTop: "10px" }}>Shipping Information</h3>
                <input
                  type="text"
                  name="country"
                  placeholder="Country"
                  value={form.country}
                  onChange={handleChange}
                  style={{
                    padding: "10px",
                    borderRadius: "6px",
                    border: "1px solid #ccc",
                  }}
                />
                <div style={{ display: "flex", gap: "10px" }}>
                  <input
                    type="text"
                    name="city"
                    placeholder="City"
                    value={form.city}
                    onChange={handleChange}
                    style={{
                      flex: 1,
                      padding: "10px",
                      borderRadius: "6px",
                      border: "1px solid #ccc",
                    }}
                  />
                  <input
                    type="text"
                    name="postalCode"
                    placeholder="Postal Code"
                    value={form.postalCode}
                    onChange={handleChange}
                    style={{
                      flex: 1,
                      padding: "10px",
                      borderRadius: "6px",
                      border: "1px solid #ccc",
                    }}
                  />
                </div>
                <input
                  type="text"
                  name="streetName"
                  placeholder="Street Name"
                  value={form.streetName}
                  onChange={handleChange}
                  style={{
                    padding: "10px",
                    borderRadius: "6px",
                    border: "1px solid #ccc",
                  }}
                />
                <input
                  type="text"
                  name="streetNum"
                  placeholder="Street #"
                  value={form.streetNum}
                  onChange={handleChange}
                  style={{
                    padding: "10px",
                    borderRadius: "6px",
                    border: "1px solid #ccc",
                  }}
                />
              </>
            )}

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
              Save & Continue
            </button>
          </form>
        </div>
      </div>
    );
}

export default OAuthSuccess;
