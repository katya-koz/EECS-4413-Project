
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
  private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

  public void sendAuctionEndNotification(String auctionId, String winnerUserId) {
    log.info("Auction {} ended. Winner: {}", auctionId, winnerUserId);
  }
}
