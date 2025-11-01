package com.bluebid.payment_app_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bluebid.payment_app_service.dto.ItemValidationFailureEvent;
import com.bluebid.payment_app_service.dto.ItemValidationSuccessEvent;
import com.bluebid.payment_app_service.dto.PaymentInitiatedEvent;
import com.bluebid.payment_app_service.dto.UserInfoValidationFailureEvent;
import com.bluebid.payment_app_service.dto.UserInfoValidationSuccessEvent;
import com.bluebid.payment_app_service.model.Receipt;
import com.bluebid.payment_app_service.repository.PaymentRepository;

@Service
public class PaymentService {
	//private final Double SALES_TAX_RATE = 0.13;
	private final KafkaTemplate<String, Object> _kafkaTemplate;
	private final PaymentRepository _paymentRepository;

	
	public PaymentService(KafkaTemplate<String, Object> kafkaTemplate, PaymentRepository paymentRepository) {
		_paymentRepository = paymentRepository;
		_kafkaTemplate = kafkaTemplate;
	}
	
	
	
	// kafka listeners
	
	@KafkaListener(topics = "catalogue.payment-item-validation-success-topic", groupId = "payment-group", containerFactory = "itemValidationSuccessListenerFactory")
    public void handleCatalogueSuccess( ItemValidationSuccessEvent event) {
        Optional<Receipt> optionalReceipt = _paymentRepository.findByPaymentId(event.getProducerEventID());

        Receipt receipt = optionalReceipt.get(); // it exists since initializepayment is not called unless a reciept is successfully saved

        // update or overwrite cat. info
        receipt.setItemId(event.getCatalogueID());
        receipt.setItemName(event.getItemName());
        receipt.setItemCost(event.getItemCost());
        receipt.setShippingCost(event.getShippingCost());
        
        receipt.setStatus("Catalogue item reserved successfully.");
        // save
        _paymentRepository.save(receipt);
    }

    @KafkaListener(topics = "catalogue.payment-item-validation-failed-topic", groupId = "payment-group", containerFactory = "itemValidationFailureListenerFactory")
    public void handleCatalogueFailure( ItemValidationFailureEvent event) {
    	 Optional<Receipt> optionalReceipt = _paymentRepository.findByPaymentId(event.getProducerEventID());

         Receipt receipt = optionalReceipt.get(); // it exists since initializepayment is not called unless a reciept is successfully saved

         // update status
         receipt.setFailureReason(event.getMessage());
         receipt.setStatus("CATALOGUE_FAILED");
         
         // save
         _paymentRepository.save(receipt);
    }

    @KafkaListener(topics = "user.payment-user-validation-success-topic", groupId = "payment-group", containerFactory = "userValidationSuccessListenerFactory")
    public void handleUserSuccess(UserInfoValidationSuccessEvent event) {
    	Optional<Receipt> optionalReceipt = _paymentRepository.findByPaymentId(event.getProducerID());

        Receipt receipt = optionalReceipt.get(); // it exists since initializepayment is not called unless a reciept is successfully saved

        // update or overwrite cat. info
        receipt.setBuyerFirstName(event.getFirstName());
        receipt.setBuyerLastName(event.getLastName());
        receipt.setBuyerStreetName(event.getStreetName());
        receipt.setBuyerStreetNum(event.getStreetNum());
        receipt.setBuyerCity(event.getCity());
        receipt.setBuyerPostalCode(event.getPostalCode());
        receipt.setBuyerCountry(event.getCountry());
        receipt.setStatus("User successfully validated.");
        // save
        _paymentRepository.save(receipt);
        
        // send for catalogue. now catalogue will validate the payment request.
        _kafkaTemplate.send("payment.item-validation-topic", new PaymentInitiatedEvent(event.getProducerID(), event.getUserID(), receipt.getSellerId(), receipt.getItemId(), receipt.getTimestamp(), receipt.getIsExpedited(), receipt.getItemCost(), receipt.getShippingCost()));
        
        
    }

    @KafkaListener(topics = "user.payment-validation-failed-topic", groupId = "payment-group" , containerFactory = "userValidationFailureListenerFactory")
    public void handleUserFailure(UserInfoValidationFailureEvent event) {
   	 Optional<Receipt> optionalReceipt = _paymentRepository.findByPaymentId(event.getProducerEventID());

     Receipt receipt = optionalReceipt.get(); // it exists since initializepayment is not called unless a reciept is successfully saved

     // update status
     receipt.setFailureReason(event.getMessage());
     receipt.setStatus("USER_FAILED");
     
     // save
     _paymentRepository.save(receipt);
    }
//    
	// logical methods
    public String isValidPaymentInfo(String cardNumber, String expiryMonth, String expiryYear, String cvv, String userID, String sellerID, String itemID, LocalDateTime timestamp, Boolean isExpedited, Double itemPrice, Double shippingCost) {
        boolean paymentValid = (validateCardNumber(cardNumber) && validateExpirationDate(expiryMonth, expiryYear) && validateCvv(cvv));
                
        if(paymentValid){
            // returns receipt id
            return this.initiatePayment(userID, sellerID, itemID, timestamp, isExpedited, itemPrice, shippingCost);
        } else {
            return null;
        }
    }
	
	private String initiatePayment(String userID, String sellerID, String itemID, LocalDateTime timestamp, Boolean isExpedited, Double itemPrice, Double shippingCost) {
	    PaymentInitiatedEvent event = new PaymentInitiatedEvent(userID, sellerID, itemID, timestamp, isExpedited, itemPrice, shippingCost); 
	    
	    Receipt receipt = new Receipt(
	        event.getId(),  
	        userID,
	        sellerID,
	        itemID,
	        itemPrice,
	        isExpedited,
	        shippingCost,  
	        "PENDING",
	        timestamp
	    );
	    
	    // save partial receipt
	    _paymentRepository.save(receipt);
	    
	    // publish event for user first
	    _kafkaTemplate.send("payment.user-validation-topic", event);
	    
	    return receipt.getId(); // return the receipt id
	}

	private boolean validateCardNumber(String cardNumber)
	{
		boolean isCardValid;
		
		//reverse the string
		String cardNumberReversed = new StringBuilder(cardNumber).reverse().toString();
		

		//implement luhn's alogirthm
		int luhns = luhnsAlgorithm(cardNumberReversed);
		
		if (luhns % 10 == 0)
		{
			isCardValid = true;
		}
		else
		{
			isCardValid = false;
			
		}
		
		
		return isCardValid;
	}
	
	private boolean validateExpirationDate(String expiryMonth, String expiryYear)
	{
		
		int expiryMonthInt = Integer.parseInt(expiryMonth);
		int expiryYearInt = Integer.parseInt(expiryYear);
		
		int currentMonth = LocalDate.now().getMonthValue();
		int currentYear = LocalDate.now().getYear();
		
		if (expiryMonthInt < 1 || expiryMonthInt > 12 || expiryYearInt < currentYear) //checking whether they have entered a valid month
		{
			return false;
		}
		
		
		if ((expiryMonthInt < currentMonth) && (expiryYearInt == currentYear))
		{
			return false;
		}
		
		return true;
	}
	
	private boolean validateCvv(String cvv)
	{
		boolean validCV;
		if (cvv == null)
		{
			return false;
		}
		cvv = cvv.trim();
		validCV = cvv.matches("\\d{3,4}");
	
		//checking the number is at least 3 and at most 4 digits
		return validCV;
	}
	
	//integer for doubling the value
	private int doubleVal(char value)
	{
		int result = 0;
		
		result = Character.getNumericValue(value);
		result *= 2;
		
		return result;
	}
	
	private int luhnsAlgorithm(String s)
	{
		int total = 0;
		int temp = 0;
		
		for (int i = 0; i < s.length(); i++)
		{
			if ((i%2) == 1)//check whether the index is odd
			{
				char c = s.charAt(i);
				temp = doubleVal(c); //double the value
				if (temp > 9)
				{
					int digit = 0;
					while (temp > 0) //getting the value of the two digits added
					{
						digit += temp % 10;
						temp = temp / 10;
					}
					
					temp = digit;
					
				}
				
				total += temp;

			}else {
				//even, just add
				total += Character.getNumericValue(s.charAt(i));
			}
			
			
		}
		return total;
	}



	public Receipt getReceiptByPaymentId(String paymentId) {
		return _paymentRepository.findByPaymentId(paymentId).orElse(null);
	}
	
	public Receipt getReceiptById(String receiptId) {
		return _paymentRepository.findById(receiptId).orElse(null);
	}
}
