package br.com.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;


public class Log{
	private static String fileName = "application.log";
	//private static FileWriter f = null;
	private static File outputFile = null;
	public static double MAX_LENGHT_LOG_FILE = 4.5; //MB
	private static boolean debugMode = false; 
	
	public static File getExternalStoragePublicDirectory(Context context) {
		//File externamStorageDir = Environment.getExternalStorageDirectory();
		//File directory = new File(externamStorageDir, context.getPackageName());
		File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
		return directory;
	}
	private static String getApplicationName(Context context){
		final PackageManager pm = context.getApplicationContext().getPackageManager();
		ApplicationInfo ai;
		try {
		    ai = pm.getApplicationInfo( context.getPackageName(), 0);
		} catch (final NameNotFoundException e) {
			android.util.Log.e("ERROR", e.getLocalizedMessage(),e);
		    ai = null;
		}
		final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
		return applicationName;
	}
	public static void removeLogOldFiles(Context context) {
		//int count = 0;
		File directory = getExternalStoragePublicDirectory(context);
		FilenameFilter fileNameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
               if(name.startsWith("application")){
                   return true;
               }else{
            	   return false;
               }
            }
         };
         
		File[] files = directory.listFiles(fileNameFilter);
		if(files!=null && files.length>5){
			Arrays.sort(files, new Comparator<File>() {
			    public int compare(File f1, File f2) {
			        if(f1.lastModified() > f2.lastModified()){
			        	return 1;
			        }else{
			        	return 0;
			        }
			    }
			});
			files[files.length-1].delete();
		}
	}
	
	public static boolean checkSize(File logFile, Context activity){
		if(logFile!=null && logFile.exists()){
			
			double bytes = logFile.length();
			double megabytes = (bytes / 2048);
			if(megabytes > MAX_LENGHT_LOG_FILE){
				appendLogInfo(activity,"Log atingiu tamanho maximo de " + megabytes +" MB");
				File oldFile = new File(outputFile.getAbsolutePath());
				try {
					if(oldFile!=null && oldFile.exists()){
						 oldFile.renameTo(new File(outputFile.getAbsolutePath()+DateUtil.getCurrentTime(DateUtil.DD_MM_YYYY_HH_MM_SS)));
						 
					}
					if(outputFile!=null && !outputFile.exists()){
						 FileWriter f2 = new FileWriter(outputFile,false);
						 f2.write("Log : " + outputFile + "\n");
						 f2.flush();
						 f2.close();	
					}
					return true;					
				} catch (Exception e) {
					android.util.Log.e("ERROR", e.getLocalizedMessage(),e);
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
		return false;
	}
	public static void init(Context activity){
		if(outputFile==null){
			try {
				//File root = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
				 File root =  getExternalStoragePublicDirectory(activity);
				 outputFile = new File(root.getAbsolutePath(), fileName);
				 android.util.Log.i("INFO", "Log : " + outputFile);
				 if(outputFile!=null && !outputFile.exists()){
					 FileWriter f = new FileWriter(outputFile,true);
					 f.write("Log : " + outputFile + "\n");
					 f.flush();
					 f.close();					 
				 }
				 
			}catch(Exception e){
					android.util.Log.e("ERROR", e.getLocalizedMessage(),e);
			}			
		}else{
			if(outputFile.exists()){
				if(checkSize(outputFile, activity)){ //atingiu o limite
					try {
						FileWriter f = new FileWriter(outputFile,true);
						f.write("Log : " + outputFile + "\n");
						f.flush();
						f.close();

						removeLogOldFiles(activity);
					}catch(Exception e){
						android.util.Log.e("ERROR", e.getLocalizedMessage(),e);
					}
				}
			}

		}
	}
	public static void e(String message){
		android.util.Log.e("ERROR",message);
	}
	public static void e(String message, Exception e){
		android.util.Log.e("ERROR",message,e);
	}
	public static void e(Activity activity,String message){
		e(activity, message,null);
	}
	public static void e(Activity activity,Exception e){
		e(activity, e.getLocalizedMessage(),e);
	}	
	public static void e(Activity activity,String message,Exception e){
		if(activity!=null){
			init(activity);
			android.util.Log.e(activity.getClass().getSimpleName(), message,e);
			appendLogError(activity, message, e);
		}else{
			android.util.Log.e("ERROR", message,e);
		}
	}
	public static void e(Context activity,String message,Exception e){
		if(activity!=null){
			init(activity);
			android.util.Log.e(activity.getClass().getSimpleName(), message,e);
			appendLogError(activity, message, e);			
		}else{
			android.util.Log.e("ERROR", message,e);
		}
	}
	private static void appendLogError(Context activity,String message, Exception e){
		if(debugMode && outputFile!=null && outputFile.exists()){
			try {
				FileWriter f = new FileWriter(outputFile,true);
				//BufferedWriter bufferWritter = new BufferedWriter(f);
				PrintWriter writer = new PrintWriter(new BufferedWriter(f));
				writer.println(DateUtil.getCurrentTime(DateUtil.DD_MM_YYYY_HH_MM_SS) + " : " + activity.getClass().getSimpleName() + " : " + message);
	 	        if(e!=null){
	 	        	writer.println(DateUtil.getCurrentTime(DateUtil.DD_MM_YYYY_HH_MM_SS) + " : " + e.getMessage());
	 	        }
	 	       writer.close();
			} catch (Exception e2) {
				android.util.Log.e("ERROR", e2.getLocalizedMessage(),e2);
			}			
		}
	}
	public static void i(String message){
		android.util.Log.i("INFO", message);
	}
	public static void i(Activity activity,String message){
		if(activity!=null){
			init(activity);
			android.util.Log.i(activity.getClass().getSimpleName(), message);
			appendLogInfo(activity,message);			
		}else{
			android.util.Log.i("INFO", message);
		}
	}
	public static void i(Context activity,String message){
		if(activity!=null){
			init(activity);
			android.util.Log.i(activity.getClass().getSimpleName(), message);
			appendLogInfo(activity,message);			
		}else{
			android.util.Log.i("INFO", message);
		}
	}
	private static void appendLogInfo(Context activity,String message){
		if(debugMode && outputFile!=null && outputFile.exists()){
			try {
				FileWriter f = new FileWriter(outputFile,true);
				PrintWriter writer = new PrintWriter(new BufferedWriter(f));
				writer.println(DateUtil.getCurrentTime(DateUtil.DD_MM_YYYY_HH_MM_SS) + " : " + activity.getClass().getSimpleName() + " : " + message);
				writer.close();
			} catch (Exception e) {
				android.util.Log.e("ERROR", e.getLocalizedMessage(),e);
			}			
		}

	}
	public static File getLogCatFile(Activity context){
		init(context);
	    return outputFile;
	}
}
