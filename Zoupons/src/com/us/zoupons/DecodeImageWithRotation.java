package com.us.zoupons;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

/*
 *  Class used to check if image is rotated, if so revoke to original position
 */

public class DecodeImageWithRotation {
	
	// To Check whether bitmap is rotated , if so change to original position
	public Bitmap decodeImage(String imagepath,Bitmap bmp,int scaledWidth,int scaledHeight){
		Bitmap bitmap = bmp;
		int orientation;
		try{
			ExifInterface exif = new ExifInterface(imagepath);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1); // returns current orientation of image
			Matrix matrix = new Matrix();
			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) { // 180 degree rotation
				matrix.postRotate(180);
				bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) { // 90 degree rotation
				matrix.postRotate(90); 
				bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),bmp.getHeight(), matrix, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) { // 270 degree rotation
				matrix.postRotate(270);
				bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),bmp.getHeight(), matrix, true);
				return bitmap;
			} 
			return bitmap;
		}catch(Exception e){
			e.printStackTrace();
			return bmp;
		}
	}
	
}
