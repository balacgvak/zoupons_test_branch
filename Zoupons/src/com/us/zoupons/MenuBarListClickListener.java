package com.us.zoupons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.FlagClasses.ZPayFlag;

public class MenuBarListClickListener implements OnClickListener {

	Context MenuBarContext;
	RelativeLayout mMapView/*,mListView*/;
	LinearLayout mListView;
	TextView menubarlisttext;
	ImageView menubarlistimage;
	Button mZpayList;
	LinearLayout mBrowse,mQRCode,mSearch;
	public ViewGroup getwidthheight;
	public static String TAG="MenuBarListClickListener";
	public static boolean clickFlag=false;
	public static boolean ZpayClickFlag=false;
	public static boolean locationClickFlag=false;
	
	private String menubartext="MANAGE CARDS";
	private int setBackgroundResource;
	public String mClassName;
	
	//Constructor for homepage
	public MenuBarListClickListener(Context context/*,RelativeLayout mapview,LinearLayout listview*/,ViewGroup middleview,TextView menubarlisttext,ImageView menubarlistimage
									,Button zpaylist,LinearLayout menubrowse,LinearLayout menuqrcode,LinearLayout menusearch,int backgroundresource,String classname){
		this.MenuBarContext=context;
		this.getwidthheight=middleview;
		this.menubarlisttext=menubarlisttext;
		this.menubarlistimage=menubarlistimage;
		this.mZpayList=zpaylist;
		this.mBrowse=menubrowse;
		this.mQRCode=menuqrcode;
		this.mSearch=menusearch;
		this.setBackgroundResource=backgroundresource;
		this.mClassName=classname;
		clickFlag=false;
	}
	
	//Constructor for location
	public MenuBarListClickListener(Context context,RelativeLayout mapview,LinearLayout listview,ViewGroup middleview,TextView menubarlisttext,ImageView menubarlistimage
			,int backgroundresource,String classname){
		this.MenuBarContext=context;
		this.mMapView=mapview;
		this.mListView=listview;
		this.getwidthheight=middleview;
		this.menubarlisttext=menubarlisttext;
		this.menubarlistimage=menubarlistimage;
		this.setBackgroundResource=backgroundresource;
		this.mClassName=classname;
		locationClickFlag=false;
	}
	
	//constructor for zpay_step1
	public MenuBarListClickListener(Context context,RelativeLayout mapviewcontainer,LinearLayout listviewcontainer,ViewGroup middleview,Button zpaylist,String classname){
		this.MenuBarContext=context;
		this.mMapView=mapviewcontainer;
		this.mListView=listviewcontainer;
		this.getwidthheight=middleview;
		this.mZpayList=zpaylist;
		this.mClassName=classname;
		ZpayClickFlag=false;
	}
	
	@Override
	public void onClick(View v) {
		if(mClassName.equals("SlidingView")){
			v.setBackgroundResource(R.drawable.gradient_menubar_new);
			this.mBrowse.setBackgroundResource(R.drawable.header_2);
			this.mQRCode.setBackgroundResource(R.drawable.header_2);
			this.mSearch.setBackgroundResource(R.drawable.header_2);

			//if(!this.menubarlisttext.getText().equals(menubartext)){
				if(!MenuBarListClickListener.clickFlag){
					
					if(ZPayFlag.getFlag()==0){
						this.menubarlistimage.setImageResource(R.drawable.map_view);
						this.menubarlisttext.setText("Map View");
						
						TransitionAnimation mStartAnimation = new TransitionAnimation(0,90,this.getwidthheight.getWidth()/2,this.getwidthheight.getHeight()/2,300f,true);
						mStartAnimation.setDuration(400);
						mStartAnimation.setFillAfter(true);
						mStartAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								Log.i("animation listener", "on start");
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								Log.i("animation listener", "on repeat");
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								Log.i("animation listener", "on end");
								SlidingView.mMapViewContainer.setVisibility(View.GONE);
								SlidingView.mListView.setVisibility(View.VISIBLE);
								SlidingView.mListView.bringToFront();
								
								TransitionAnimation mStartEndAnimation = new TransitionAnimation(-90, 0,getwidthheight.getWidth()/2,getwidthheight.getHeight()/2,300f,false);
								mStartEndAnimation.setDuration(400);
								mStartEndAnimation.setFillAfter(true);
								SlidingView.mListView.startAnimation(mStartEndAnimation);
							}
						});
						SlidingView.mMapViewContainer.startAnimation(mStartAnimation);
					}else{
						final Drawable drawableTop = MenuBarContext.getResources().getDrawable(R.drawable.list_brown);
						this.mZpayList.setText("List View");
						this.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
						
						TransitionAnimation mListAnimation = new TransitionAnimation(0,-90,this.getwidthheight.getWidth()/2,this.getwidthheight.getHeight()/2,300f,true);
						mListAnimation.setDuration(400);
						mListAnimation.setFillAfter(true);
						mListAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								Log.i("animation listener", "on start");
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								Log.i("animation listener", "on repeat");
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								Log.i("animation listener", "on end");

								getwidthheight.post(new Runnable() {

									@Override
									public void run() {
										SlidingView.mListView.setVisibility(View.GONE);
										SlidingView.mMapViewContainer.setVisibility(View.VISIBLE);
										SlidingView.mMapViewContainer.bringToFront();
										
										TransitionAnimation mListEndAnimation = new TransitionAnimation(90,0,getwidthheight.getWidth()/2,getwidthheight.getHeight()/2,300f,false);
										mListEndAnimation.setDuration(400);
										mListEndAnimation.setFillAfter(true);
										SlidingView.mMapViewContainer.startAnimation(mListEndAnimation);
									}
								});
							}
						});
						SlidingView.mListView.startAnimation(mListAnimation);
					}
				}else{
					if(ZPayFlag.getFlag()==0){
						this.menubarlistimage.setImageResource(R.drawable.list);
						this.menubarlisttext.setText("List View");
						
						TransitionAnimation mListAnimation = new TransitionAnimation(0,-90,this.getwidthheight.getWidth()/2,this.getwidthheight.getHeight()/2,300f,true);
						mListAnimation.setDuration(400);
						mListAnimation.setFillAfter(true);
						mListAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								Log.i("animation listener", "on start");
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								Log.i("animation listener", "on repeat");
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								Log.i("animation listener", "on end");

								getwidthheight.post(new Runnable() {

									@Override
									public void run() {

										SlidingView.mListView.setVisibility(View.GONE);
										SlidingView.mMapViewContainer.setVisibility(View.VISIBLE);
										SlidingView.mMapViewContainer.bringToFront();
										
										TransitionAnimation mListEndAnimation = new TransitionAnimation(90,0,getwidthheight.getWidth()/2,getwidthheight.getHeight()/2,300f,false);
										mListEndAnimation.setDuration(400);
										mListEndAnimation.setFillAfter(true);
										SlidingView.mMapViewContainer.startAnimation(mListEndAnimation);
									}
								});
							}
						});
						SlidingView.mListView.startAnimation(mListAnimation);
					}else{
						final Drawable drawableTop = MenuBarContext.getResources().getDrawable(R.drawable.map_view_brown);
						this.mZpayList.setText("Map View");
						this.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
						
						TransitionAnimation mStartAnimation = new TransitionAnimation(0,90,this.getwidthheight.getWidth()/2,this.getwidthheight.getHeight()/2,300f,true);
						mStartAnimation.setDuration(400);
						mStartAnimation.setFillAfter(true);
						mStartAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								Log.i("animation listener", "on start");
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								Log.i("animation listener", "on repeat");
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								Log.i("animation listener", "on end");
								SlidingView.mMapViewContainer.setVisibility(View.GONE);
								SlidingView.mListView.setVisibility(View.VISIBLE);
								SlidingView.mListView.bringToFront();
								TransitionAnimation mStartEndAnimation = new TransitionAnimation(-90, 0,getwidthheight.getWidth()/2,getwidthheight.getHeight()/2,300f,false);
								mStartEndAnimation.setDuration(400);
								mStartEndAnimation.setFillAfter(true);
								SlidingView.mListView.startAnimation(mStartEndAnimation);
							}
						});
						SlidingView.mMapViewContainer.startAnimation(mStartAnimation);
					}
				}
				MenuBarListClickListener.clickFlag=!MenuBarListClickListener.clickFlag;
				/*}else{
				Toast.makeText(this.MenuBarContext,this.menubarlisttext.getText().toString(), Toast.LENGTH_SHORT).show();
			}*/
		}else if(mClassName.equals("Location")){
			TranslateLocationMapView();
		}else if(mClassName.equals("zpay_step1")){
			if(!MenuBarListClickListener.ZpayClickFlag){
				final Drawable drawableTop = MenuBarContext.getResources().getDrawable(R.drawable.list_brown);
				this.mZpayList.setText("List View");
				this.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);

				TransitionAnimation mListAnimation = new TransitionAnimation(0,-90,this.getwidthheight.getWidth()/2,this.getwidthheight.getHeight()/2,300f,true);
				mListAnimation.setDuration(400);
				mListAnimation.setFillAfter(true);
				mListAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						Log.i("animation listener", "on start");
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						Log.i("animation listener", "on repeat");
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Log.i("animation listener", "on end");

						getwidthheight.post(new Runnable() {

							@Override
							public void run() {
								mListView.setVisibility(View.GONE);
								mMapView.setVisibility(View.VISIBLE);
								mMapView.bringToFront();

								TransitionAnimation mListEndAnimation = new TransitionAnimation(90,0,getwidthheight.getWidth()/2,getwidthheight.getHeight()/2,300f,false);
								mListEndAnimation.setDuration(400);
								mListEndAnimation.setFillAfter(true);
								mMapView.startAnimation(mListEndAnimation);
							}
						});
					}
				});
				mListView.startAnimation(mListAnimation);
			}else{
				final Drawable drawableTop = MenuBarContext.getResources().getDrawable(R.drawable.map_view_brown);
				this.mZpayList.setText("Map View");
				this.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);

				TransitionAnimation mStartAnimation = new TransitionAnimation(0,90,this.getwidthheight.getWidth()/2,this.getwidthheight.getHeight()/2,300f,true);
				mStartAnimation.setDuration(400);
				mStartAnimation.setFillAfter(true);
				mStartAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						Log.i("animation listener", "on start");
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						Log.i("animation listener", "on repeat");
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Log.i("animation listener", "on end");
						mMapView.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						mListView.bringToFront();
						TransitionAnimation mStartEndAnimation = new TransitionAnimation(-90, 0,getwidthheight.getWidth()/2,getwidthheight.getHeight()/2,300f,false);
						mStartEndAnimation.setDuration(400);
						mStartEndAnimation.setFillAfter(true);
						mListView.startAnimation(mStartEndAnimation);
					}
				});
				mMapView.startAnimation(mStartAnimation);
			}
			MenuBarListClickListener.ZpayClickFlag=!MenuBarListClickListener.ZpayClickFlag;
		}
	}
	
	public void TranslateLocationMapView(){
		
		if(!this.menubarlisttext.getText().equals(menubartext)){
			if(!MenuBarListClickListener.locationClickFlag){
				
				this.menubarlistimage.setImageResource(R.drawable.list);
				this.menubarlisttext.setText("List View");
				
				TransitionAnimation mListAnimation = new TransitionAnimation(0,-90,this.getwidthheight.getWidth()/2,this.getwidthheight.getHeight()/2,300f,true);
				mListAnimation.setDuration(400);
				mListAnimation.setFillAfter(true);
				mListAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						Log.i("animation listener", "on start");
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						Log.i("animation listener", "on repeat");
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Log.i("animation listener", "on end");

						getwidthheight.post(new Runnable() {

							@Override
							public void run() {
								mListView.setVisibility(View.GONE);
								mMapView.setVisibility(View.VISIBLE);
								mMapView.bringToFront();
								TransitionAnimation mListEndAnimation = new TransitionAnimation(90,0,getwidthheight.getWidth()/2,getwidthheight.getHeight()/2,300f,false);
								mListEndAnimation.setDuration(400);
								mListEndAnimation.setFillAfter(true);
								mMapView.startAnimation(mListEndAnimation);
							}
						});
					}
				});
				mListView.startAnimation(mListAnimation);					
			}else{
				this.menubarlistimage.setImageResource(R.drawable.map_view);
				this.menubarlisttext.setText("Map View");
				
				TransitionAnimation mStartAnimation = new TransitionAnimation(0,90,this.getwidthheight.getWidth()/2,this.getwidthheight.getHeight()/2,300f,true);
				mStartAnimation.setDuration(400);
				mStartAnimation.setFillAfter(true);
				mStartAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						Log.i("animation listener", "on start");
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						Log.i("animation listener", "on repeat");
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Log.i("animation listener", "on end");
						mMapView.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						mListView.bringToFront();
						TransitionAnimation mStartEndAnimation = new TransitionAnimation(-90, 0,getwidthheight.getWidth()/2,getwidthheight.getHeight()/2,300f,false);
						mStartEndAnimation.setDuration(400);
						mStartEndAnimation.setFillAfter(true);
						mListView.startAnimation(mStartEndAnimation);
					}
				});
				mMapView.startAnimation(mStartAnimation);
			}
			MenuBarListClickListener.locationClickFlag=!MenuBarListClickListener.locationClickFlag;
		}else{
			Toast.makeText(this.MenuBarContext,this.menubarlisttext.getText().toString(), Toast.LENGTH_SHORT).show();
		}
	}
}