import { useState, useEffect } from "react";
import { useUser } from "../Context/UserContext";
import CatalogueItemCard from "./CatalogueItemCard";
import CatalogueItemModal from "./CatalogueItemModal";

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
    <div style={{ maxWidth: "900px", margin: "20px auto", padding: "10px" }}>
      <h1>Catalogue</h1>

      <input
        type="text"
        placeholder="Search items..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        style={{
          width: "100%",
          padding: "10px",
          marginBottom: "20px",
          borderRadius: "6px",
          border: "1px solid #ccc",
        }}
      />

      {loading && <p>Loading...</p>}

      {items.length > 0 ? (
        <div
          className="grid"
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))",
            gap: "16px",
            marginTop: "20px",
          }}
        >
          {items.map((item) => (
            <div key={item.id} onClick={() => setModalItemId(item.id)}>
              <CatalogueItemCard item={item} />
            </div>
          ))}
        </div>
      ) : (
        !loading && <p>No items found.</p>
      )}

      <div style={{ marginTop: "20px", textAlign: "center" }}>
        <button
          onClick={handlePrevPage}
          disabled={page === 0 || loading}
          style={{ marginRight: "10px" }}
        >
          Previous
        </button>
        <button onClick={handleNextPage} disabled={!hasMore || loading}>
          Next
        </button>
      </div>

      {modalItemId && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            backgroundColor: "rgba(0,0,0,0.5)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            zIndex: 999,
          }}
          onClick={() => setModalItemId(null)}
        >
          <div onClick={(e) => e.stopPropagation()}>
            <CatalogueItemModal
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
