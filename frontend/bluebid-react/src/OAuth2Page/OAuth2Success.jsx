import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useUser } from "../Context/UserContext";
import "./OAuth2Success.scss";

function OAuthSuccess() {
  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const navigate = useNavigate();
  const { login, authFetch } = useUser();

  const uid = query.get("uid");
  const uname = query.get("uname");
  const email = query.get("email");
  const hasShippingInfo = query.get("hasshipinfo") === "true";
  const hasName = query.get("hasname") === "true";
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
  }, [uname, jwt, uid, expiresAt, hasName, hasShippingInfo]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    login(jwt, { username: form.username || uname, userid: uid }, expiresAt);

    try {
      const res = await authFetch(`http://localhost:8080/api/account/profile`, {
        method: "PUT",
        body: JSON.stringify(form),
      });

      if (!res.ok) throw new Error("Failed to save additional info");

      login(jwt, { username: form.username || uname, userid: uid }, expiresAt);
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Failed to save additional info. Please try again.");
    }
  };

  if (loading) return <p>We're getting things ready for you...</p>;

  return (
    <div className="oauth-container">
      <div className="oauth-card">
        <h2 className="oauth-title">Complete Your Profile</h2>
        <form onSubmit={handleSubmit} className="oauth-form">
          {(!uname || uname.trim() === "") && (
            <input
              type="text"
              name="username"
              placeholder="Username / Display Name"
              value={form.username}
              onChange={handleChange}
              className="oauth-input"
            />
          )}

          {!hasName && (
            <div className="oauth-flex">
              <input
                type="text"
                name="firstName"
                placeholder="First Name"
                value={form.firstName}
                onChange={handleChange}
                className="oauth-input"
              />
              <input
                type="text"
                name="lastName"
                placeholder="Last Name"
                value={form.lastName}
                onChange={handleChange}
                className="oauth-input"
              />
            </div>
          )}

          {!hasShippingInfo && (
            <>
              <h3 className="shipping-title">Shipping Information</h3>
              <input
                type="text"
                name="country"
                placeholder="Country"
                value={form.country}
                onChange={handleChange}
                className="oauth-input"
              />
              <div className="oauth-flex">
                <input
                  type="text"
                  name="city"
                  placeholder="City"
                  value={form.city}
                  onChange={handleChange}
                  className="oauth-input"
                />
                <input
                  type="text"
                  name="postalCode"
                  placeholder="Postal Code"
                  value={form.postalCode}
                  onChange={handleChange}
                  className="oauth-input"
                />
              </div>
              <input
                type="text"
                name="streetName"
                placeholder="Street Name"
                value={form.streetName}
                onChange={handleChange}
                className="oauth-input"
              />
              <input
                type="text"
                name="streetNum"
                placeholder="Street #"
                value={form.streetNum}
                onChange={handleChange}
                className="oauth-input"
              />
            </>
          )}

          <button type="submit" className="oauth-button">
            Save & Continue
          </button>
        </form>
      </div>
    </div>
  );
}

export default OAuthSuccess;
