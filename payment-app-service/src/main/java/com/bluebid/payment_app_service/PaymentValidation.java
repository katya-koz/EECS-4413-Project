package com.bluebid.payment_app_service;
import java.time.LocalDate;

public class PaymentValidation

{
	
	String cardNumber;
	int expiryMonth;
	int expiryYear;
	String cvv;
	
	public PaymentValidation(String creditCardNumber, int expirationMonth, int expirationYear, String cvvNumber)
	{
		cardNumber = creditCardNumber;
		expiryMonth = expirationMonth;
		expiryYear = expirationYear;
		cvv = cvvNumber;
		
	}
	
	public boolean validateCard()
	{
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
//			System.out.println("card # is not valid");
			isCardValid = false;
			
		}
		
		
		return isCardValid;
	}
	
	private boolean validateExpirationDate(int month, int year)
	{
		
		
		int currentMonth = LocalDate.now().getMonthValue();
		int currentYear = LocalDate.now().getYear();
		
		if (month < 1 || month > 12 || year < currentYear) //checking whether they have entered a valid month
		{
//			System.out.println("month # is not valid");
			return false;
		}
		
		
		if ((month < currentMonth) && (year == currentYear))
		{
//			System.out.println("date is not valid");
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
		
		if (validCV == false)
		{
//			System.out.println("invalid CV");
		}
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
//				System.out.println("number is " + c);
				temp = doubleVal(c); //double the value
//				System.out.println("doubled # is " + temp);
//				System.out.println("temp before check is " + temp + "\n");
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
				
//				System.out.println("temp is  " + temp + "\n");
				total += temp;
//				System.out.println("running sum: " + total + "\n");
				
				//temp = 0;
			}
			
			
		}
//		System.out.println("total is " + total);
		return total;
	}
	
	

}
