package br.com.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.net.ParseException;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * @class DateUtil
 * Classe utilizada para manipula��o de datas no banco de dados
 * E nos artefatos que fazem a sincroniza��o dos dados
 * @author Jairo
 *
 */
public class DateUtil 
{
	public static String M_YYY = "M/yyyy";
	public static String MM_YYYY = "MM/yyyy";
	public static String D_M_YYYY = "d/M/yyyy";
	public static String DD_M_YYYY = "dd/M/yyyy";
	public static String D_MM_YYYY = "d/MM/yyyy";
	public static String DD_MM_YYYY = "dd/MM/yyyy";
	public static String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
	public static String D_M_YYYY_HH_MM_SS = "d/M/yyyy HH:mm:ss";
	public static String DD_M_YYYY_HH_MM_SS = "dd/M/yyyy HH:mm:ss";
	public static String D_MM_YYYY_HH_MM_SS = "d/MM/yyyy HH:mm:ss";
	public static String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss"; 
	public static String DD_MM_YYYY_HH_MM_SS_S = "dd/MM/yyyy HH:mm:ss.S";	
	
	/**
	 * Converte em data (java.util.Date) uma String 
	 * @param date - objeto de data em formato String dd/MM/yyyy (DateFormat.MEDIUM)
	 * @return objeto de Data
	 * @throws ParseException se date n�o estiver no formato adequado
	 * @throws Exception 
	 * @throws IllegalArgumentException se date for nulo.
	 * @since 1.0 
	 */
	public static Date parseDate(String date)  {
		if ( date == null ) {
			String msg = "DATA_NAO_NULA";
			throw new IllegalArgumentException(msg);
			//throw new IllegalArgumentException("Data n�o pode ser nula!");
		} else if ( date.trim().length() == 0 ) {
			String msg = "DATA_NAO_BRANCO";
			throw new IllegalArgumentException(msg);
			//throw new IllegalArgumentException("Data n�o pode estar em branco!");
		}
		Date parsed = null;
		try {
			if ( date.indexOf('-') >=0 ) {
				date = date.replace('-', '/');
			}			
			if ( date.indexOf('/') >= 4 ) {
				parsed = DateUtil.formatDate(date);
			} else {
				parsed = DateUtil.formatDate(date);
			}
				
		} catch (Exception e) {
			//String msn = ("Erro no parseDate: data: "+date+", erro: "+e.getMessage()+", cause: "+e.getCause() );
			parsed = new Date();
		}
		return parsed;
	}	
	public static Date formatDate(String date) throws Exception{
		Date parsed = null;
		if ( date.length() == 6 && date.indexOf("/") == 1 ) { // M/yyyy
			parsed = DateUtil.parseDate(date, "M/yyyy");
		} else if ( date.length() == 7 && date.indexOf("/") == 2 ) { // MM/yyyy
			parsed = DateUtil.parseDate(date, "MM/yyyy");
		} else if ( date.length() == 8 ) { // d/M/yyyy
			parsed = DateUtil.parseDate(date, "d/M/yyyy");
		} else if ( date.length() == 9 && date.indexOf("/") == 2 ) { // dd/M/yyyy
			parsed = DateUtil.parseDate(date, "dd/M/yyyy");
		} else if ( date.length() == 9 && date.indexOf("/") == 1 ) { // d/MM/yyyy
			parsed = DateUtil.parseDate(date, "d/MM/yyyy");
		} else if ( date.length() == 10 ) { // dd/MM/yyyy
			parsed = DateUtil.parseDate(date, "dd/MM/yyyy");
		} else if ( date.length() == 16 ) { // dd/MM/yyyy HH:mm
			parsed = DateUtil.parseDate(date, "dd/MM/yyyy HH:mm");
		} else if ( date.length() == 17 && date.indexOf("/") == 1 ) { // d/M/yyyy HH:mm:ss
			parsed = DateUtil.parseDate(date, "d/M/yyyy HH:mm:ss");
		} else if ( date.length() == 18 && date.indexOf("/") == 2 ) { // dd/M/yyyy HH:mm:ss
			parsed = DateUtil.parseDate(date, "dd/M/yyyy HH:mm:ss");
		} else if ( date.length() == 18 && date.indexOf("/") == 1 ) { // d/MM/yyyy  HH:mm:ss
			parsed = DateUtil.parseDate(date, "d/MM/yyyy HH:mm:ss");
		} else if ( date.length() == 19 ) { // dd/MM/yyyy HH:mm:ss
			parsed = DateUtil.parseDate(date, "dd/MM/yyyy HH:mm:ss"); 
		} else if ( date.length() >= 21 ) { // dd/MM/yyyy HH:mm:ss.S
			parsed = DateUtil.parseDate(date, "dd/MM/yyyy HH:mm:ss.S");				
		} else {
			// caso a data seja �nvalida, retorna a data atual
			parsed = new Date();
		}	
		return parsed;
	}
	/**
	 * Converte em data (java.util.Date) uma String usando o Locale default
	 * @param date - objeto de data em formato String dd/MM/yyyy
	 * @return objeto de Data
	 * @throws ParseException se date n�o estiver no formato adequado
	 * @throws java.text.ParseException 
	 * @throws IllegalArgumentException se date ou format for nulo.
	 * @since 1.0 
	 */
	public static Date parseDate(String date, String format) throws ParseException, java.text.ParseException {
		if ( date == null ) {
			String msg = "DATA_NAO_NULA";
			throw new IllegalArgumentException(msg);
			//throw new IllegalArgumentException("Data n�o pode ser nula!");
		}		
		if ( format == null ) {
			String msg = "FORMATO_NAO_NULO";
			throw new IllegalArgumentException(msg);
			//throw new IllegalArgumentException("Formato n�o pode ser nulo!");
		}		
		// Formata a data com o Locale Default (SO Regional Settings) 
		SimpleDateFormat customFormater = new SimpleDateFormat( format ) ;
		return customFormater.parse( date );
	}
	
	/**
	 * Pegar o tempo corrente do sistema
	 * @return Data atual
	 */
	public static String getCurrentTime(String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format); 
		Date date = new Date();
		return dateFormat.format(date);
	}
	public static String parseString(Date date, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format); 
		return dateFormat.format(date);
	}
	public static DatePickerDialog initDatePickDialog(Context context,final EditText et,  final Calendar newDate){
		 Calendar newCalendar = Calendar.getInstance();
		 DatePickerDialog dpd = new DatePickerDialog(context, new OnDateSetListener() {
	 
	            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	               // Calendar newDate = Calendar.getInstance();
	            	//newDate  = Calendar.getInstance();
	                newDate.set(year, monthOfYear, dayOfMonth);
	                et.setText(DateUtil.parseString(newDate.getTime(),DateUtil.DD_M_YYYY_HH_MM_SS));
	            }
	 
	        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		 return dpd;
	}
	
	public static boolean isDate(String date){
		try {
			if(DateUtil.parseDate(date)==null){
				throw new NullPointerException("Uma data válida deve ser informada!");
			}else{
				return true; //is a valid date 
			}
		} catch (Exception e) {
			Log.e(e.getLocalizedMessage());
		}
		return false;
	}
	
	public static String parseString(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.DD_MM_YYYY_HH_MM_SS); 
		return dateFormat.format(date);
	}
	
}
