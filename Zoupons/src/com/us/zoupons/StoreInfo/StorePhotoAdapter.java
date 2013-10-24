package com.us.zoupons.StoreInfo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.us.zoupons.R;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.ImageLoader;

public class StorePhotoAdapter extends PagerAdapter{

	private Context context;
	static ImageView mStoreImageView;
	private WebView store_image_webview;
	static ProgressBar mProgressView;
	private int width,height;
	private ImageLoader imageLoader;
	static String TAG = "StorePhotoAdapter";
	static ViewPager mViewPager;
	public static int mSelectedPosition;
	ImageView mPhotoSwitcherLeftArrow, mPhotoSwitcherRightArrow; 

	public StorePhotoAdapter(Context context,int width,int height, ViewPager viewpager) {
		this.context = context;
		this.width = width ; 
		this.height = height ;
		imageLoader = new ImageLoader(context);
		mViewPager = viewpager;
	}

	public StorePhotoAdapter(Context context,int width,int height, ViewPager viewpager, ImageView mleftarrow, ImageView mrightarrow) {
		this.context = context;
		this.width = width ; 
		this.height = height ;
		imageLoader = new ImageLoader(context);
		mViewPager = viewpager;
		this.mPhotoSwitcherLeftArrow = mleftarrow;
		this.mPhotoSwitcherRightArrow = mrightarrow;
	}

	@Override
	public int getCount() {
		return WebServiceStaticArrays.mFullSizeStorePhotoUrl.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		Log.i(TAG,"isViewFromObject");
		return (view == object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		mSelectedPosition=position;
		LayoutInflater inflater = (LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.store_image_switcher, null);
		((ViewPager) container).addView(view, 0);
		mStoreImageView = (ImageView) view.findViewById(R.id.store_image);
		mStoreImageView.setVisibility(View.GONE);
		mProgressView =  (ProgressBar) view.findViewById(R.id.mProgressBar);
		//mProgressView.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
		store_image_webview = (WebView) view.findViewById(R.id.store_image_webview);
		store_image_webview.setHorizontalScrollBarEnabled(false);
		store_image_webview.setVerticalScrollBarEnabled(false);
		Log.i("webview width and height", width + " "+ height);
		store_image_webview.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams mWebviewLayoutParams= new RelativeLayout.LayoutParams(width, height);
		mWebviewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		store_image_webview.setLayoutParams(mWebviewLayoutParams);
		store_image_webview.loadUrl(WebServiceStaticArrays.mFullSizeStorePhotoUrl.get(position)+"&w="+width+"&h="+height+"&zc=0");
		store_image_webview.getSettings().setBuiltInZoomControls(false);
		
		store_image_webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		store_image_webview.setWebViewClient(new CustomWebviewLoadListener(mProgressView));
		return view;
	}

	public static void refresh(){
		try{
			if(mViewPager!=null){
				//mViewPager.getAdapter().notifyDataSetChanged();
				if(mSelectedPosition>=0&&mStoreImageView.getTag().equals(mSelectedPosition)){
					mProgressView.setVisibility(View.GONE);
					mStoreImageView.setImageBitmap(WebServiceStaticArrays.mFullSizeStorePhoto.get(mSelectedPosition));
				}
				Log.i(TAG,"Refreshed");
			}else{
				Log.i(TAG,"No Adapter Intialized");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public int getItemPosition(Object object) {
		Log.i(TAG,"getItemPosition");
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		Log.i(TAG,"destroyItem");
		container.removeView((FrameLayout)object);
	}		

	void startMyTask(AsyncTask<Integer, Void, String> asyncTask/*,int position*/) {
		if(Build.VERSION.SDK_INT >= 11)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR/*, position*/);
		else
			asyncTask.execute(/*position*/);
	}

	private Bitmap getBitmapFromURL(String src) {
		try {
			Log.i("url", src);
			Bitmap mBitmap;	
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(src);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 60000); // 1 minute
			request.setParams(params);
			HttpResponse response = httpClient.execute(request);
			byte[] image = EntityUtils.toByteArray(response.getEntity());
			mBitmap =   BitmapFactory.decodeByteArray(image, 0,image.length);
			image = null;
			return mBitmap;
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/*private Bitmap getBitmapFromURLNew(String src) {
		try {
			Log.i("url", src);
			Bitmap mBitmap;
			URL url = new URL(src);
			mBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			return mBitmap;
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}*/
	
	public class CustomWebviewLoadListener extends WebViewClient{
		
		private ProgressBar mProgressBar;
		
		public CustomWebviewLoadListener(ProgressBar progress) {
			// TODO Auto-generated constructor stub
			this.mProgressBar = progress;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			this.mProgressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			Log.i("webview client", "On Page finished");
			this.mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
			// TODO Auto-generated method stub
			Log.i("webview client", "On Received Error");
			view.setVisibility(View.GONE);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
	
	/*public class LoadStorePhotoTask extends AsyncTask<Integer, Void, String>{
		
		private Bitmap mStoreImage;
		private ImageView mImageView;
		private ProgressBar mProgressView;
		ImageView mLeftArrow,mRightArrow;
		int position;
		
		public LoadStorePhotoTask(ImageView imageview, ProgressBar ProgressView,ImageView leftarrow, ImageView rightarrow, int position) {
			this.mImageView = imageview;
			mProgressView = ProgressView;
			this.mLeftArrow = leftarrow;
			this.mRightArrow = rightarrow;
			this.position = position;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(position==0){
				mLeftArrow.setAlpha(100);
				mRightArrow.setAlpha(255);
			}else if(position==(WebServiceStaticArrays.mFullSizeStorePhotoUrl.size()-1)){
				mLeftArrow.setAlpha(255);
				mRightArrow.setAlpha(100);
			}
		}

		@Override
		protected String doInBackground(Integer... params) {
			try{
				//position = params[0];
				if(WebServiceStaticArrays.mFullSizeStorePhoto.size()>0){
					if(WebServiceStaticArrays.mFullSizeStorePhoto.get(position)!=null){
						mStoreImage = WebServiceStaticArrays.mFullSizeStorePhoto.get(position);
					}else{
						mStoreImage = getBitmapFromURL(WebServiceStaticArrays.mFullSizeStorePhotoUrl.get(position)+"&w="+width+"&h="+height+"&zc=0");
						//mStoreImage = getBitmapFromURLNew(WebServiceStaticArrays.mFullSizeStorePhotoUrl.get(position)+"&w="+width+"&h="+height+"&zc=0");
					}
				}else{
					Log.i(TAG,"mFullSizeStorePhotoUrl Position Value : "+WebServiceStaticArrays.mFullSizeStorePhotoUrl.get(position));
					mStoreImage = getBitmapFromURL(WebServiceStaticArrays.mFullSizeStorePhotoUrl.get(position)+"&w="+width+"&h="+height+"&zc=0");
					//mStoreImage = getBitmapFromURLNew(WebServiceStaticArrays.mFullSizeStorePhotoUrl.get(position)+"&w="+width+"&h="+height+"&zc=0");
				}
				return "success";
			}catch(Exception e){
				e.printStackTrace();
				return "failure";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equalsIgnoreCase("success")){
				mProgressView.setVisibility(View.GONE);
				if(mStoreImage != null){
					mImageView.setImageBitmap(mStoreImage);
				}
				if(position==0){
					mLeftArrow.setAlpha(100);
					mRightArrow.setAlpha(255);
				}else if(position==(WebServiceStaticArrays.mFullSizeStorePhotoUrl.size()-1)){
					mLeftArrow.setAlpha(255);
					mRightArrow.setAlpha(100);
				}
				mProgressView.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
			}else{
				if(position==0){
					mLeftArrow.setAlpha(100);
					mRightArrow.setAlpha(255);
				}else if(position==(WebServiceStaticArrays.mFullSizeStorePhotoUrl.size()-1)){
					mLeftArrow.setAlpha(255);
					mRightArrow.setAlpha(100);
				}
				mProgressView.setVisibility(View.VISIBLE);
			}
		}
	}*/
}
