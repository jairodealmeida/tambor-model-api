package br.com.util;

import android.util.Base64;

public class Cripto {
	
	    public static String encripty(String pw) {
	    	 if (pw!=null && pw.length()>0){
	    	     return Base64.encodeToString( pw.getBytes(), Base64.DEFAULT );
	    	 }else{
	    		 return null;
	    	 }
	    }

	    public static String decripty(String pw) {
	    	 if (pw!=null && pw.length()>0){
	    	        return new String( Base64.decode( pw, Base64.DEFAULT ) );
	    	 }else{
	    		 return null;
	    	 }
	    		 
	    }
}
