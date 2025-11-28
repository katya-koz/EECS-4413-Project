import {
  BrowserRouter as Router,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import Layout from "./Components/Layout";
import Catalogue from "./CataloguePage/Catalogue";
import CatalogueView from "./CataloguePage/CatalogueItemPage";
import SignIn from "./SignInPage/SignIn";
import SignUp from "./SignUpPage/SignUp";
import Home from "./HomePage/Home";
import PayNow from "./PayNowPage/PayNow";
import Receipt from "./PayNowPage/Reciept";
import ResetPasswordRequestPage from "./ResetPasswordPage/ResetPasswordRequestPage";
import ResetPasswordConfirmPage from "./ResetPasswordPage/ResetPasswordConfirmPage";
import { UserProvider } from "./Context/UserContext";
import NewAuction from "./NewAuctionPage/NewAuction";
import { ToastProvider } from "./Context/ToastContext";
import "bootstrap-icons/font/bootstrap-icons.css";

function AppRoutes() {
  const location = useLocation();
  const state = location.state;

  return (
    <>
      <Routes location={state?.backgroundLocation || location}>
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/reset-password" element={<ResetPasswordRequestPage />} />
        <Route
          path="/reset-password/confirm"
          element={<ResetPasswordConfirmPage />}
        />
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
        <Route
          path="/catalogue/items/:id"
          element={
            <Layout>
              <CatalogueView />
            </Layout>
          }
        />
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

        <Route
          path="/auction/new-auction"
          element={
            <Layout>
              <NewAuction />
            </Layout>
          }
        />
		
	
      </Routes>

      {/* {state?.backgroundLocation && (
        <Routes>
          <Route path="/catalogue/items/:id" element={<CatalogueView />} />
        </Routes>
      )} */}
    </>
  );
}

function App() {
  return (
    <Router>
      <UserProvider>
        <ToastProvider>
          <AppRoutes />
        </ToastProvider>
      </UserProvider>
    </Router>
  );
}

export default App;
