// this will be the notification on auction end (like a toast)

function AuctionNotification({ message, onClick }) {
  if (!message) return null;

  return <div onClick={onClick}>{message}</div>;
}

export default AuctionNotification;
