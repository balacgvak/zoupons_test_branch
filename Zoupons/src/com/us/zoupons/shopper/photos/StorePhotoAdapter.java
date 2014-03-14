package com.us.zoupons.shopper.photos;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.us.zoupons.R;
import com.us.zoupons.classvariables.POJOStorePhoto;
import com.us.zoupons.collections.WebServiceStaticArrays;

/**
 * 
 * Custom pager adapter to display store photos in slider
 *
 */

@SuppressWarnings("deprecation")
public class StorePhotoAdapter extends PagerAdapter{

	private ImageView mStoreImageView;
	private WebView mStoreImageWebview;
	private ProgressBar mProgressView;
	private int width,height;
	private Context mContext;

	public StorePhotoAdapter(Context context,int width,int height) {
		this.width = width ; 
		this.height = height ;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return WebServiceStaticArrays.mStorePhoto.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
        Log.i("view pager", "instatiate item "+ position);
		LayoutInflater inflater = (LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.store_image_switcher, null);
		((ViewPager) container).addView(view, 0);
		mStoreImageView = (ImageView) view.findViewById(R.id.store_image);
		mStoreImageView.setVisibility(View.GONE);
		mProgressView =  (ProgressBar) view.findViewById(R.id.mProgressBar);
		//mProgressView.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
		mStoreImageWebview = (WebView) view.findViewById(R.id.store_image_webview);
		mStoreImageWebview.setHorizontalScrollBarEnabled(false);
		mStoreImageWebview.setVerticalScrollBarEnabled(false);
		mStoreImageWebview.setVisibility(View.VISIBLE);
		try{
			// To get store photo url
			POJOStorePhoto mStorePhotoDetails = (POJOStorePhoto) WebServiceStaticArrays.mStorePhoto.get(position);
			int original_Image_width = Integer.valueOf(mStorePhotoDetails.mOriginalImageWidth);
			int original_Image_height = Integer.valueOf(mStorePhotoDetails.mOriginalImageHeight);


			Log.i("original image width and height", original_Image_width + " " + original_Image_height);
			Log.i("viewpager width and height", width + " " + height);
			Log.i("image url", mStorePhotoDetails.full_size);
			//setWebviewSettings();

			RelativeLayout.LayoutParams mWebviewLayoutParams;
			// Compare image original width/height and webview width/height 
			if(original_Image_width >= width && original_Image_height >= height){
				mWebviewLayoutParams= new RelativeLayout.LayoutParams(width, height);
				mWebviewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				mStoreImageWebview.setLayoutParams(mWebviewLayoutParams);
				setWebviewSettings();
				mStoreImageWebview.loadUrl(mStorePhotoDetails.full_size+"&w="+width+"&h="+height+"&zc=0");
			}else if(original_Image_width >= width && original_Image_height <= height){
				mWebviewLayoutParams= new RelativeLayout.LayoutParams(width, original_Image_height);
				mWebviewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				mStoreImageWebview.setLayoutParams(mWebviewLayoutParams);
				setWebviewSettings();
				mStoreImageWebview.loadUrl(mStorePhotoDetails.full_size+"&w="+width+"&h="+original_Image_height+"&zc=0");
			}else if(original_Image_width <= width && original_Image_height >= height){
				mWebviewLayoutParams= new RelativeLayout.LayoutParams(original_Image_width, height);
				mWebviewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				mStoreImageWebview.setLayoutParams(mWebviewLayoutParams);
				setWebviewSettings();
				mStoreImageWebview.loadUrl(mStorePhotoDetails.full_size+"&w="+original_Image_width+"&h="+height+"&zc=0");
			}else{
				mWebviewLayoutParams= new RelativeLayout.LayoutParams(original_Image_width, original_Image_height);
				mWebviewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				mStoreImageWebview.setLayoutParams(mWebviewLayoutParams);
				setWebviewSettings();
				mStoreImageWebview.loadUrl(mStorePhotoDetails.full_size+"&w="+original_Image_width+"&h="+original_Image_height+"&zc=0");
			}		
			mStoreImageWebview.getSettings().setBuiltInZoomControls(false);
			mStoreImageWebview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			mStoreImageWebview.setWebViewClient(new CustomWebviewLoadListener(mProgressView));	
		}catch(Exception e){
			e.printStackTrace();
		}

		return view;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((FrameLayout)object);
	}		

	void startMyTask(AsyncTask<Integer, Void, String> asyncTask/*,int position*/) {
		if(Build.VERSION.SDK_INT >= 11)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR/*, position*/);
		else
			asyncTask.execute(/*position*/);
	}

	/**
	 * 
	 * Custom webview client to manage loading of photo
	 *
	 */

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
			this.mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			view.setVisibility(View.GONE);

		}
	}

	private void setWebviewSettings(){
		switch (mContext.getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			// ...
			//mStoreImageWebview.getSettings().setLoadWithOverviewMode(true);
			//mStoreImageWebview.getSettings().setUseWideViewPort(true);
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			// ...
			/*mStoreImageWebview.getSettings().setLoadWithOverviewMode(true);
			mStoreImageWebview.getSettings().setUseWideViewPort(true);*/
			break;
		case DisplayMetrics.DENSITY_HIGH:
			// ...
			/*	mStoreImageWebview.getSettings().setLoadWithOverviewMode(true);
			mStoreImageWebview.getSettings().setUseWideViewPort(true);*/
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			// ...
			mStoreImageWebview.getSettings().setLoadWithOverviewMode(true);
			mStoreImageWebview.getSettings().setUseWideViewPort(true);
			break;
		default:

		}
	}


}
