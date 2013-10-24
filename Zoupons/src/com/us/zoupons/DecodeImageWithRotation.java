package com.us.zoupons;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class DecodeImageWithRotation {

		
	public Bitmap decodeImage(String imagepath,Bitmap bmp,int scaledWidth,int scaledHeight){
		Bitmap bitmap = bmp;
		int orientation;
		try{
			ExifInterface exif = new ExifInterface(imagepath);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			//exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);
			Matrix m = new Matrix();
			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
				//m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90); 
				bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), m, true);
				return bitmap;
			} 
			return bitmap;
		}catch(Exception e){
			e.printStackTrace();
			return bmp;
		}
	}
	
}
