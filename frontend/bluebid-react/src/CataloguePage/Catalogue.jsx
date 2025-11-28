import { useState, useEffect, useRef } from "react";
import { useUser } from "../Context/UserContext";
import CatalogueItemCard from "./CatalogueItemCard";
import CatalogueItemModal from "./CatalogueItemModal";
import styles from "./Catalogue.module.scss";

function Catalogue() {
  const [searchTerm, setSearchTerm] = useState("");
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const { authFetch } = useUser();

  const debounceRef = useRef(null);
  const [modalItemId, setModalItemId] = useState(null);

  const fetchItems = async (query = "", pageNumber = 0) => {
    setLoading(true);
    try {
      // relative URL -> CRA proxy -> gateway:8080
      const qp = new URLSearchParams();
      qp.append("page", String(pageNumber));
      if (query) qp.append("keyword", query);

      const response = await authFetch(`/api/catalogue/items?${qp.toString()}`);
      if (!response.ok) throw new Error("Failed to fetch items");

      const data = await response.json();
      setItems(data.content);
      setHasMore(!data.last);
      setPage(data.number);
    } catch (err) {
      console.error(err);
      setItems([]);
      setHasMore(false);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchItems();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // debounce search
  useEffect(() => {
    if (debounceRef.current) clearTimeout(debounceRef.current);
    debounceRef.current = setTimeout(() => {
      fetchItems(searchTerm, 0);
    }, 300);

    return () => clearTimeout(debounceRef.current);
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
    <div className={styles.container}>
      <h1>Catalogue</h1>

      <input
        type="text"
        placeholder="Search items..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className={styles.searchInput}
      />

      {loading && <p>Loading...</p>}

      {items.length > 0 ? (
        <div className={styles.grid}>
          {items.map((item) => (
            <div key={item.id} onClick={() => setModalItemId(item.id)}>
              <CatalogueItemCard item={item} />
            </div>
          ))}
        </div>
      ) : (
        !loading && <p>No items found.</p>
      )}

      <div className={styles.pager}>
        <button
          onClick={handlePrevPage}
          disabled={page === 0 || loading}
          className="spacer"
        >
          Previous
        </button>
        <span className={styles.spacer} />
        <button onClick={handleNextPage} disabled={!hasMore || loading}>
          Next
        </button>
      </div>

      {modalItemId && (
        <div className={styles.overlay} onClick={() => setModalItemId(null)}>
          <div className={styles.modalWrap} onClick={(e) => e.stopPropagation()}>
            <CatalogueItemModal
              id={modalItemId}
              onUpdate={(updatedItem) => {
                setItems((prevItems) =>
                  prevItems.map((it) =>
                    it.id === updatedItem.id ? updatedItem : it
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
