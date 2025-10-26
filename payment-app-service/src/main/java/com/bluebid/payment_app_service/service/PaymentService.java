package com.bluebid.payment_app_service.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	
	
	public boolean isValidPaymentInfo(String cardNumber, String expiryMonth, String expiryYear, String cvv)
	{
		try {
		Integer.parseInt(expiryMonth);
		Integer.parseInt(expiryYear);
		Integer.parseInt(cvv);
		Integer.parseInt(cardNumber);
		}catch(NumberFormatException e) {
			throw new IllegalArgumentException("Payment information contains non-numeric characters.");
		}
		
		if (validateCardNumber(cardNumber) && validateExpirationDate(expiryMonth, expiryYear) && validateCvv(cvv))
		{
			return true;
		}
		else
		{
			return false;
		}
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
}
