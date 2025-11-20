import {
  BrowserRouter as Router,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import Layout from "./Components/Layout";
import Catalogue from "./CataloguePage/Catalogue";
import CatalogueView from "./CataloguePage/CatalogueItemModal";
import SignIn from "./SignInPage/SignIn";
import SignUp from "./SignUpPage/SignUp";
import Home from "./HomePage/Home";
import PayNow from "./PayNowPage/PayNow";
import Receipt from "./PayNowPage/Reciept";
import ResetPasswordRequestPage from "./ResetPasswordPage/ResetPasswordRequestPage";
import ResetPasswordConfirmPage from "./ResetPasswordPage/ResetPasswordConfirmPage";
import AuctionNotification from "./Components/Notification/AuctionNotification";
import { UserProvider } from "./Context/UserContext";

function AppRoutes() {
  const location = useLocation();
  const state = location.state;

  return (
    <>
      <Routes location={state?.backgroundLocation || location}>
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
	<Route path="/reset-password/request" element={<ResetPasswordRequestPage />} />
	<Route path="/reset-password/confirm" element={<ResetPasswordConfirmPage />} />
        <Route
          path="/"
          element={
            <Layout>
              <Home />
            </Layout>
          }
        />
        <Route
          path="/catalogue/items"
          element={
            <Layout>
              <Catalogue />
            </Layout>
          }
        />
        <Route path="/catalogue/items/:id" element={<CatalogueView />} />
        <Route
          path="/pay/:id"
          element={
            <Layout>
              <PayNow />
            </Layout>
          }
        />
        <Route
          path="/receipt/:id"
          element={
            <Layout>
              <Receipt />
            </Layout>
          }
        />
      </Routes>

      {state?.backgroundLocation && (
        <Routes>
          <Route path="/catalogue/items/:id" element={<CatalogueView />} />
        </Routes>
      )}
    </>
  );
}

function App() {
  return (
    <Router>
      <UserProvider>
        <AppRoutes />
	<AuctionNotification />
      </UserProvider>
    </Router>
  );
}

export default App;
