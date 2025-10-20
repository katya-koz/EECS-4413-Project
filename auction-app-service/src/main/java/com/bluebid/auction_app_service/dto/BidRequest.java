import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BidRequest {
  private String userId;
  @DecimalMin(value = "0.0", inclusive = false)
  private int bidVal;

  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public int getBidValue() { return bidVal; }
  public void setBidValue(int bidVal) { this.bidVal = bidVal; }
}
