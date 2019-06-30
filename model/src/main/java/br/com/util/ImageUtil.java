package br.com.util;


import java.io.ByteArrayOutputStream;


import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
/**
 * @class ImageUtil
 * Classe utilit�ria para converter imagens de formatos media (camera) 
 * para formatos suportados pelo banco de dados sqlite
 * @author Jairo
 *
 */
public class ImageUtil {
  
    public static Bitmap parseBitmap(byte[] imageBytes){
    	Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    	return bmp;
    }
    
    public static Bitmap parseBitmap(Context context, int resId){
    	Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId);
    	return bmp;
    }
   
    public static byte[] parseArray(Bitmap bitmap){
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
    	bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
    	byte[] bitMapData = bos.toByteArray();
    	return bitMapData;
    }
    public static Drawable loadImageFromWeb(Context context, String urlImage){
        Drawable d = null;
        if(urlImage!=null){
        	d = new Imagem().loadImageFromWeb(urlImage);
        }//else{
        //	d = context.getResources().getDrawable(R.drawable.sem_foto);
        //}
        return d;
    }
    public static void loadImageByte(Activity activity, ImageView imageView, byte[] imageBytes){
        if(imageBytes!=null){
            if(imageBytes!=null){
            	Bitmap bitmap = ImageUtil.parseBitmap(imageBytes);
            	imageView.setImageBitmap(bitmap);
            }
        }//else{
        //	Drawable d = activity.getResources().getDrawable(R.drawable.sem_foto);
        //	imageView.setImageDrawable(d);
        //}
    }
    public static Bitmap getImageFrom(String fotoPath, boolean ajustado){
     	if(fotoPath!=null){
     		if(ajustado){
	     	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	            bmOptions.inJustDecodeBounds = ajustado;
	            Bitmap picture = BitmapFactory.decodeFile(fotoPath, bmOptions);
	            return picture;
     		}else{
     		     Bitmap picture = BitmapFactory.decodeFile(fotoPath);
     		     return picture;
     		}
    	}else{
    		return null;
    	}
    }
    public static Bitmap getLowImageFrom( String fotoPath){
     	if(fotoPath!=null){
     		
	     	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	     	    
	            //bmOptions.inJustDecodeBounds = ajustado;
	            //bmOptions.inSampleSize = inSampleSize;
	            //bmOptions.inPreferredConfig = inPreferredConfig;
	            //bmOptions.inDither = true;
	     	    
	     	   //bmOptions.inScaled = true;
	     	   bmOptions.inSampleSize = 8;
	     	  //options.inSampleSize = 8; 
	     	    
	           Bitmap picture = BitmapFactory.decodeFile(fotoPath, bmOptions);
	           return picture;
     		
    	}else{
    		return null;
    	}
    }
   
    
}
