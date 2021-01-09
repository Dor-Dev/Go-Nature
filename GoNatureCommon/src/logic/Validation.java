package logic;
/**
 * Class with static method to validate all the field in the systems.
 * @author dorswisa
 *
 */
public class Validation {
	/**
	 * Email validation according to xxxxxxx@xxx.xxx.xxx.xxx without any marks that
	 * doesn't necessary.
	 * 
	 * @param email
	 * @return true if valid, else return false
	 */
	public static boolean emailValidation(String email) {
		String emailFormat = "[a-zA-Z0-9[!#$%&'()*+,/\\-_\\.\"]]+@[a-zA-Z0-9[!#$%&'()*+,/\\-_\"]]+\\.[a-zA-Z0-9[!#$%&'()*+,/\\-_\"\\.]]+";
		return email.matches(emailFormat);
	}
	/**
	 * Login validation allow only digits from 0-9 , the length is 6 or 9 for member number or id number.
	 * @param number
	 * @return true if valid, else return false
	 */
	public static boolean loginValidation(String number) {
		String numberFormat = "([0-9]){9,10}";
		return number.matches(numberFormat);
	}

	/**
	 * ID validation according to 9 digits only allow only digits.
	 * 
	 * @param id
	 * @return true if valid, else return false
	 */
	public static boolean idValidation(String id) {
		String idFormat = "([0-9]){9,9}";
		return id.matches(idFormat);
	}

	/**
	 * Name Validation allow only characters.
	 * 
	 * @param name
	 * @return true when match else return false.
	 */
	public static boolean nameValidation(String name) {
		if(name==null)
			return false;
		String nameFormat = "[a-zA-Z ]+";
		return name.matches(nameFormat);
	}
	/**
	 * 
	 * @param str
	 * @return true if null, else false 
	 */
	public static boolean isNull(Object object)
	{
		if (object==null)
			return true;
		return false;
	}

	/**
	 * Phone validation allow only digits , length must be 7-10 digits.
	 * 
	 * @param phone
	 * @return true if valid, else return false
	 */
	public static boolean phoneValidation(String phone) {
		String phoneFormat = "[0-9]{10,10}";
		return phone.matches(phoneFormat);
	}

	/**
	 * CVV validation allow only digits , length than 3 digits.
	 * 
	 * @param cvv
	 * @return true if valid, else return false
	 */
	public static boolean cvvValidation(String cvv) {
		String cvvFormat = "[0-9]{3,4}";
		return cvv.matches(cvvFormat);
	}

	/**
	 * Card number validation allow only digits length equal to 16 digits.
	 * 
	 * @param cNumber
	 * @return true if valid, else return false
	 */
	public static boolean cardNumberValidation(String cNumber) {
		String cardNumberFormat = "[0-9]{16,16}";
		return cNumber.matches(cardNumberFormat);
	}

	/**
	 * Month expiration validation allow only digits, length equals to 2. Months are
	 * between 1-12
	 * @param exp
	 * @return true if valid, else return false
	 */
	public static boolean monthExperationValidation(String exp) {
		if(!onlyDigitsValidation(exp))
			return false;
		int month = Integer.parseInt(exp);
		String expFormat = "[0-9]{2,2}";
		return (exp.matches(expFormat)) && (month >= 1 && month <= 12);
	}
	/**
	 * Year expiration validation allow only digits, length equals to 2. Months are
	 * between 1-12
	 * @param exp
	 * @return true if valid, else return false
	 */
	public static boolean yearExperationValidation(String exp) {
		if(!onlyDigitsValidation(exp))
			return false;
		int year = Integer.parseInt(exp);
		String expFormat = "[0-9]{2,2}";
		return (exp.matches(expFormat)) && (year > 20 && year < 30);
	}
	/**
	 * Event name validation allow digits 0-9 and characters and signs = '-' and '.' only.
	 * @param eventName
	 * @return true if valid, else return false
	 */
	public static boolean eventNameValidation(String eventName) {
		if(eventName==null)
			return false;
		String eventNameFormat = "[a-zA-Z0-9\\-\\. ]+";
		return eventName.matches(eventNameFormat);
	}
	/**
	 * Only digits from 0-9 is allowed.
	 * @param text
	 * @return true if valid, else return false
	 */
	public static boolean onlyDigitsValidation(String text) {
		String onlyDigits = "^[0-9]+";
		return text.matches(onlyDigits);
	}
	/**
	 * Only digits from 0-9 is allowed.
	 * @param text
	 * @return true if valid, else return false
	 */
	public static boolean onlyLettersValidation(String text) {
		String onlyDigits = "^[a-zA-Z]+";
		return text.matches(onlyDigits);
	}
	
	/**
	 * Discount validation allow only digits 0-9 and '.' .
	 * @param discount
	 * @return true if valid, else return false
	 */
	public static boolean discoutValidation(String discount) {
		String discountFormat = "[0-9.]+";
		return discount.matches(discountFormat);
	}
	

}
