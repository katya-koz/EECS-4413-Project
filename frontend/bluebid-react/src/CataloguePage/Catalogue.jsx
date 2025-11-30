import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import CatalogueItemCard from "./CatalogueItemCard";
import CatalogueItemPage from "./CatalogueItemPage";
import "./style/Catalogue.scss";

function Catalogue() {
  const [searchTerm, setSearchTerm] = useState("");
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const { authFetch } = useUser();

  const [timer, setTimer] = useState(null);
  const [modalItemId, setModalItemId] = useState(null);

  const fetchItems = async (query = "", pageNumber = 0) => {
    setLoading(true);
    try {
      const url = new URL("http://localhost:8080/api/catalogue/items");
      url.searchParams.append("page", pageNumber);
      if (query) url.searchParams.append("keyword", query);

      const response = await authFetch(url.toString());
      if (!response.ok) throw new Error("Failed to fetch items");

      const data = await response.json();

      setItems(data.content);
      setHasMore(!data.last);
      setPage(data.number);
    } catch (err) {
      console.error(err);
      setItems([]);
      setHasMore(false);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchItems();
  }, []);

  // debounce search
  useEffect(() => {
    if (timer) clearTimeout(timer);

    setTimer(
      setTimeout(() => {
        fetchItems(searchTerm, 0);
      }, 300)
    );

    return () => clearTimeout(timer);
  }, [searchTerm]);

  const handleNextPage = () => {
    if (!hasMore) return;
    fetchItems(searchTerm, page + 1);
  };

  const handlePrevPage = () => {
    if (page === 0) return;
    fetchItems(searchTerm, page - 1);
  };

  return (
    <div className="cataloguePage">
      <h1>Catalogue</h1>

      <input
        type="text"
        placeholder="Search items..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="searchInput"
      />

      {loading && <p className="loadingText">Loading...</p>}

      {items.length > 0 ? (
        <div className="grid">
          {items.map((item) => (
            <div key={item.id} onClick={() => setModalItemId(item.id)}>
              <CatalogueItemCard item={item} />
            </div>
          ))}
        </div>
      ) : (
        !loading && <p className="noItems">No items found.</p>
      )}

      <div className="pager">
        <i
          className="bi bi-arrow-left"
          onClick={handlePrevPage}
          aria-disabled={page === 0 || loading}
        ></i>
        <i
          className="bi bi-arrow-right"
          onClick={handleNextPage}
          aria-disabled={!hasMore || loading}
        ></i>
      </div>

      {modalItemId && (
        <div className="modalOverlay" onClick={() => setModalItemId(null)}>
          <div className="modalContent" onClick={(e) => e.stopPropagation()}>
            <CatalogueItemPage
              id={modalItemId}
              onUpdate={(updatedItem) => {
                setItems((prevItems) =>
                  prevItems.map((item) =>
                    item.id === updatedItem.id ? updatedItem : item
                  )
                );
              }}
            />
          </div>
        </div>
      )}
    </div>
  );
}

export default Catalogue;
