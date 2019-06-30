package br.com.util;

import android.util.Log;

public class Logger {
	
	private static Class<?> artefact;
	
	public static Logger getLogger(Class<?> artefact){
		if(artefact!=null){
			Logger.artefact = artefact;
			Log.i("Logger", "logging to " + artefact.getName());
			return new Logger();
		}else{
			throw new NullPointerException("Logger artefact not found");
		}
	}
	public static void info(String msg){
		if(artefact!=null){
			Log.i(artefact.getName(), msg);
		}else{
			Log.i("INFO", msg);	
		}
	}
	public static void error(String msg){
		if(artefact!=null){
			Log.e(artefact.getName(), msg);	
		}else{
			Log.e("ERROR", msg);	
		}
	}
	public static void error(Exception e){
		if(e!=null){
			if(artefact!=null){
				Log.e(artefact.getName(), e.getLocalizedMessage(),e);
			}else{
				Log.e("ERROR", e.getLocalizedMessage(),e);
			}
		}
	}
	
	public static void error(String msg, Exception e){
		Log.e("ERROR", msg);
		if(e!=null){
			if(artefact!=null){
				Log.e(artefact.getName(), e.getLocalizedMessage(),e);
			}else{
				Log.e("ERROR", e.getLocalizedMessage(),e);
			}
		}
	}
	
	
	
	public static void info(Class<?> artefact, String msg){
		if(artefact!=null){
			Log.i(artefact.getName(), msg);
		}else{
			Log.i("INFO", msg);	
		}
	}
	public static void error(Class<?> artefact, String msg){
		if(artefact!=null){
			Log.e(artefact.getName(), msg);	
		}else{
			Log.e("ERROR", msg);	
		}
	}
	public static void error(Class<?> artefact, Exception e){
		if(e!=null){
			if(artefact!=null){
				Log.e(artefact.getName(), e.getLocalizedMessage(),e);
			}else{
				Log.e("ERROR", e.getLocalizedMessage(),e);
			}
		}
	}
	
	public static void error(Class<?> artefact, String msg, Exception e){
		Log.e("ERROR", msg);
		if(e!=null){
			if(artefact!=null){
				Log.e(artefact.getName(), e.getLocalizedMessage(),e);
			}else{
				Log.e("ERROR", e.getLocalizedMessage(),e);
			}
		}
	}
	
}
