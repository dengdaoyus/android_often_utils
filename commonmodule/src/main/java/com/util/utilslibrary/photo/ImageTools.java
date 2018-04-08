package com.util.utilslibrary.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public final class ImageTools {

	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}


	public static boolean checkSDCardAvailable(){
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	

	public static File savePhotoToSDCard(Bitmap photoBitmap, String path){
			File photoFile = new File(path);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
						fileOutputStream.flush();
						return photoFile;
//						fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally{
				try {
					if (fileOutputStream != null)
						fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return null;
	}
	
	private static int readPictureDegree(String path){
		int degree=0;
		try {
			ExifInterface exifInterface=new ExifInterface(path);
			int orientation=exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch(orientation){
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree=90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree=180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree=270;
				break;
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return degree;
		
	}
	public static Bitmap rotateBitmap(Bitmap bitmap, String path){
		Bitmap new_bitmap=null;
		if(bitmap!=null){
			Matrix matrix=new Matrix();
			matrix.postRotate(readPictureDegree(path));
			Bitmap small_bitmap=getSmallBitmap(path);
			 new_bitmap= Bitmap.createBitmap(small_bitmap, 0, 0, small_bitmap.getWidth(), small_bitmap.getHeight(),matrix,true);
			if (null != bitmap && bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			if (null != small_bitmap && small_bitmap.isRecycled()) {
				small_bitmap.recycle();
				small_bitmap = null;
			}

		}
		return new_bitmap;
		
	}

	public static Bitmap rotateBitmap(String path, int screenWidth, int screenHeight) {
		Bitmap bitmap = null;
		int orientation=readPictureDegree(path);
		final int maxWidth = screenWidth;
		final int maxHeight = screenHeight;
		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int sourceWidth, sourceHeight;
			if (orientation == 90 || orientation == 270) {
				sourceWidth = options.outHeight;
				sourceHeight = options.outWidth;
			} else {
				sourceWidth = options.outWidth;
				sourceHeight = options.outHeight;
			}
			boolean compress = false;
			if (sourceWidth > maxWidth || sourceHeight > maxHeight) {
				float widthRatio = (float) sourceWidth / (float) maxWidth;
				float heightRatio = (float) sourceHeight / (float) maxHeight;

				options.inJustDecodeBounds = false;
				if (new File(path).length() > 512000) {
					float maxRatio = Math.max(widthRatio, heightRatio);
					options.inSampleSize = (int) maxRatio;
					compress = true;
				}
				bitmap = BitmapFactory.decodeFile(path, options);
			} else {
				bitmap = BitmapFactory.decodeFile(path);
			}
			if (orientation > 0) {
				Matrix matrix = new Matrix();
				//matrix.postScale(sourceWidth, sourceHeight);
				matrix.postRotate(orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}
			sourceWidth = bitmap.getWidth();
			sourceHeight = bitmap.getHeight();
			if ((sourceWidth > maxWidth || sourceHeight > maxHeight) && compress) {
				float widthRatio = (float) sourceWidth / (float) maxWidth;
				float heightRatio = (float) sourceHeight / (float) maxHeight;
				float maxRatio = Math.max(widthRatio, heightRatio);
				sourceWidth = (int) ((float) sourceWidth / maxRatio);
				sourceHeight = (int) ((float) sourceHeight / maxRatio);
				Bitmap bm = Bitmap.createScaledBitmap(bitmap, sourceWidth, sourceHeight, true);
				bitmap.recycle();
				return bm;
			}
		} catch (Exception e) {
		}
		return bitmap;
	}

	public static Bitmap getSmallBitmap(String path){

		BitmapFactory.Options options=new BitmapFactory.Options();

		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, 320, 480);

		options.inJustDecodeBounds = false;

		Bitmap bitmap= BitmapFactory.decodeFile(path, options);

		return bitmap;

	}
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth){

		int height=options.outHeight;
		int width=options.outWidth;
		int inSampleSize=1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;

	}
	
}
