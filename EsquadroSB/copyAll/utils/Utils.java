package {{pack}};


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

public class Utils {

	/**
	 * Logger instance.
	 */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());


	/**
	 * @param clazz
	 * @param message
	 * @return message
	 */
	public static String getMessageLog(Class<?> clazz, String message) {

		StringBuilder messageLog = new StringBuilder(message.length() + 20);

		messageLog.append(clazz.getName());
		messageLog.append(" - ");
		messageLog.append(message);

		return messageLog.toString();
	}


	/**
	 * Method that generates the MD5 of the given string.
	 * 
	 * @param s
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String MD5(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(), 0, s.length());
		return new BigInteger(1, m.digest()).toString(16);
	}

	/**
	 * Method responsible for making the email validation when not meet the rule name@domain.type
	 *
	 * @param email
	 * @return boolean
	 */
	public static boolean isValidEmail(String email) {
		String regex = "(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*";
		boolean result = true;

		if (StringUtils.isBlank(email)) {
			result = false;
		} else {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(email);
			result = matcher.find();
		}
		return result;
	}


	/**
	 * method reponsible to validate password
	 *
	 * @param password
	 * @return boolean
	 */
	public static boolean isValidPassword(String password) {
		if (password.length() >= 8) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Removes the punctuation of a string.
	 * 
	 * @param s
	 * @return
	 */
	public static String removePunctuation(String s) {
		if (s != null) {
			return s.replaceAll("\\p{Punct}", "");
		}
		return s;
	}


	/**
	 * Validate phone number
	 *
	 * @param strPhone
	 * @return boolean
	 */
	public static boolean isValidTelefone(String strPhone) {
		if (strPhone.length() == 10) {
			strPhone = "(" + strPhone.substring(0, 2) + ") "
					+ strPhone.substring(2, 6) + "-"
					+ strPhone.substring(6, 10);
		} else if (strPhone.length() == 11) {
			strPhone = "(" + strPhone.substring(0, 2) + ") "
					+ strPhone.substring(2, 7) + "-"
					+ strPhone.substring(7, 11);
		} else if (strPhone.length() == 13 && strPhone.startsWith("+")) {
			strPhone = "(" + strPhone.substring(3, 5) + ") "
					+ strPhone.substring(5, 9) + "-"
					+ strPhone.substring(9, 12);
		} else if (strPhone.length() == 14 && strPhone.startsWith("+")) {
			strPhone = "(" + strPhone.substring(3, 5) + ") "
					+ strPhone.substring(5, 10) + "-"
					+ strPhone.substring(10, 13);
		} else {
			return false;
		}

		String format = ".((10)|([1-9][1-9]).)\\s9?[6-9][0-9]{3}-[0-9]{4}"; // (**)
		// ****-****
		String format1 = ".((10)|([1-9][1-9]).)\\s[2-5][0-9]{3}-[0-9]{4}"; // (**)
		// *****-****

		return strPhone.matches(format) || strPhone.matches(format1);
	}


	
	/**
	 * CALCULA IDADE EM ANOS MESES E DIAS A PARTIR DA DATA DE NASCIMENTO
	 * @param dataNasc
	 * @return HashMap<String,String> in Keys(YEAR,MONTH,DAY_OF_MONTH)
	 */
	public static HashMap<String, Integer> calcularIdade(Date dataNasc) {
		HashMap<String, Integer> hm = new HashMap<>(); 
		if(dataNasc != null){

			Integer anos = 0;
			Integer meses = 0;
			Integer dias = 0;
			Calendar dataAtual;
			Calendar dataNascimento;
			dataAtual = Calendar.getInstance();        
			dataNascimento = Calendar.getInstance();
			dataNascimento.setTime(dataNasc);
			anos = anos + (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));
			meses = meses + (dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH));
			dias = dias + (dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH));
			if (dataAtual.get(Calendar.MONTH) == dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = 12 + meses;
					anos = anos - 1;
				}
			} else if (dataAtual.get(Calendar.MONTH) < dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) >= dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					meses = 12 + meses;
					anos = anos - 1;
				} else if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = 12 + meses;
					anos = anos - 1;
				}
			} else if (dataAtual.get(Calendar.MONTH) > dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = meses - 1;
				}
			}

			hm.put("YEAR"  		  , anos);
			hm.put("MONTH"		  , meses);
			hm.put("DAY_OF_MONTH" , dias);
		}
		return hm;
	}

	/**
	 * CALCULA IDADE EM ANOS MESES E DIAS A PARTIR DA DATA DE NASCIMENTO ATE DATEREF
	 * @author Adamis.Rocha
	 * @since  1.0
	 * @param dataNasc
	 * @param dateRef
	 * @return HashMap<String,String> in Keys(YEAR,MONTH,DAY_OF_MONTH)
	 */
	public static HashMap<String, Integer> calcularIdade(Date dataNasc,Date dateRef) {

		HashMap<String, Integer> hm = new HashMap<>(); 
		if(dataNasc != null && dateRef != null){

			Integer anos = 0;
			Integer meses = 0;
			Integer dias = 0;
			Calendar dataAtual;
			Calendar dataNascimento;
			dataAtual = Calendar.getInstance();   
			dataAtual.setTime(dateRef);
			dataNascimento = Calendar.getInstance();
			dataNascimento.setTime(dataNasc);
			anos = anos + (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));
			meses = meses + (dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH));
			dias = dias + (dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH));
			if (dataAtual.get(Calendar.MONTH) == dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = 12 + meses;
					anos = anos - 1;
				}
			} else if (dataAtual.get(Calendar.MONTH) < dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) >= dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					meses = 12 + meses;
					anos = anos - 1;
				} else if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = 12 + meses;
					anos = anos - 1;
				}
			} else if (dataAtual.get(Calendar.MONTH) > dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = meses - 1;
				}
			}

			hm.put("YEAR"  		  , anos);
			hm.put("MONTH"		  , meses);
			hm.put("DAY_OF_MONTH" , dias);
		}

		return hm;
	}


	/**
	 * (Não Testado! Favor Testar Quando usar!)
	 * CALCULA IDADE EM DIAS A PARTIR DA DATA DE NASCIMENTO ATE DATEREF
	 * @author Adamis.Rocha
	 * @since  1.0
	 * @param dataNasc
	 * @param dateRef
	 * @return HashMap<String,String> in Keys(DAY)
	 */
	public static HashMap<String, Integer> calcularIdadeDias(Date dataNasc,Date dateRef) {
		
		HashMap<String, Integer> hm = new HashMap<>(); 
		if(dataNasc != null && dateRef != null){
			Integer diferencaDias = Integer.parseInt(((dateRef.getTime() - dataNasc.getTime()) / (1000*60*60*24))+"");			
			hm.put("DAY" , diferencaDias);
		}

		return hm;
	}
	
	/**
	 * (Não Testado! Favor Testar Quando usar!)
	 * CALCULA IDADE EM MESES A PARTIR DA DATA DE NASCIMENTO ATE DATEREF
	 * @author Adamis.Rocha
	 * @since  1.0
	 * @param dataNasc
	 * @param dateRef
	 * @return HashMap<String,String> in Keys(MONTH)
	 */
	public static HashMap<String, Integer> calcularIdadeMeses(Date dataNasc,Date dateRef) {
		
		HashMap<String, Integer> hm = new HashMap<>(); 
		
		if(dataNasc != null && dateRef != null){

			Integer anos = 0;
			Integer meses = 0;
			Integer dias = 0;
			
			Calendar dataAtual;
			Calendar dataNascimento;
			
			dataAtual = Calendar.getInstance();   
			dataAtual.setTime(dateRef);
			
			dataNascimento = Calendar.getInstance();
			dataNascimento.setTime(dataNasc);
			
			anos = anos + (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));
			meses = meses + (dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH));
			dias = dias + (dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH));
			
			if (dataAtual.get(Calendar.MONTH) == dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = 12 + meses;
					anos = anos - 1;
				}
			} else if (dataAtual.get(Calendar.MONTH) < dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) >= dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					meses = 12 + meses;
					anos = anos - 1;
				} else if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = 12 + meses;
					anos = anos - 1;
				}
			} else if (dataAtual.get(Calendar.MONTH) > dataNascimento.get(Calendar.MONTH)) {
				if (dataAtual.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
					dias = 30 + dias;
					meses = meses - 1;
				}
			}
			
			int mesesTemp = 0;
			
			if(anos>0){
				mesesTemp += anos*12;
			}

			if(meses>0){
				mesesTemp += meses; 
			}			
			
			if(dias>0){
				mesesTemp += 1;
			}
			
			hm.put("MONTH" , mesesTemp);
		}

		return hm;
	}
}
