package com.us.zoupons;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import com.us.zoupons.animation.TransitionAnimation;
import com.us.zoupons.flagclasses.ZPayFlag;

/**
 * 
 * Helper class to show list and google map when we tap respective button in footer
 *
 */

public class MenuBarListClickListener implements OnClickListener {

	private Context mMenuBarContext;
	private RelativeLayout mMapView;
	private LinearLayout mListView;
	private TextView menubarlisttext;
	private ImageView menubarlistimage;
	private Button mZpayList;
	private LinearLayout mBrowse,mQRCode,mSearch;
	public ViewGroup mMiddleViewContainer;
	public String mTAG="MenuBarListClickListener";
	public static boolean sClickFlag=false;
	public static boolean sZpayClickFlag=false;
	public static boolean sLocationClickFlag=false;
	private String mMenubartext="MANAGE CARDS";
	private String mClassName;
	
	//Constructor for homepage
	public MenuBarListClickListener(Context context/*,RelativeLayout mapview,LinearLayout listview*/,ViewGroup middleview,TextView menubarlisttext,ImageView menubarlistimage
									,Button zpaylist,LinearLayout menubrowse,LinearLayout menuqrcode,LinearLayout menusearch,int backgroundresource,String classname,LinearLayout mListview, RelativeLayout mMapViewContainer){
		this.mMenuBarContext=context;
		this.mMiddleViewContainer=middleview;
		this.menubarlisttext=menubarlisttext;
		this.menubarlistimage=menubarlistimage;
		this.mZpayList=zpaylist;
		this.mBrowse=menubrowse;
		this.mQRCode=menuqrcode;
		this.mSearch=menusearch;
		this.mClassName=classname;
		this.mListView = mListview;
		this.mMapView = mMapViewContainer;
		sClickFlag=false;
	}
	
	//Constructor for location
	public MenuBarListClickListener(Context context,RelativeLayout mapview,LinearLayout listview,ViewGroup middleview,TextView menubarlisttext,ImageView menubarlistimage
			,int backgroundresource,String classname){
		this.mMenuBarContext=context;
		this.mMapView=mapview;
		this.mListView=listview;
		this.mMiddleViewContainer=middleview;
		this.menubarlisttext=menubarlisttext;
		this.menubarlistimage=menubarlistimage;
		this.mClassName=classname;
		sLocationClickFlag=false;
	}
	
	//constructor for CardPurchase
	public MenuBarListClickListener(Context context,RelativeLayout mapviewcontainer,LinearLayout listviewcontainer,ViewGroup middleview,Button zpaylist,String classname){
		this.mMenuBarContext=context;
		this.mMapView=mapviewcontainer;
		this.mListView=listviewcontainer;
		this.mMiddleViewContainer=middleview;
		this.mZpayList=zpaylist;
		this.mClassName=classname;
		sZpayClickFlag=false;
	}
	
	@Override
	public void onClick(View v) {
		if(mClassName.equals("ShopperHomePage")){
			v.setBackgroundResource(R.drawable.gradient_menubar_new);
			this.mBrowse.setBackgroundResource(R.drawable.header_2);
			this.mQRCode.setBackgroundResource(R.drawable.header_2);
			this.mSearch.setBackgroundResource(R.drawable.header_2);

			//if(!this.menubarlisttext.getText().equals(menubartext)){
				if(!MenuBarListClickListener.sClickFlag){
					
					if(ZPayFlag.getFlag()==0){
						this.menubarlistimage.setImageResource(R.drawable.map_view);
						this.menubarlisttext.setText("Map View");
						
						TransitionAnimation mStartAnimation = new TransitionAnimation(0,90,this.mMiddleViewContainer.getWidth()/2,this.mMiddleViewContainer.getHeight()/2,300f,true);
						mStartAnimation.setDuration(400);
						mStartAnimation.setFillAfter(true);
						mStartAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								mMapView.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
								mListView.bringToFront();
								
								TransitionAnimation mStartEndAnimation = new TransitionAnimation(-90, 0,mMiddleViewContainer.getWidth()/2,mMiddleViewContainer.getHeight()/2,300f,false);
								mStartEndAnimation.setDuration(400);
								mStartEndAnimation.setFillAfter(true);
								mListView.startAnimation(mStartEndAnimation);
							}
						});
						mMapView.startAnimation(mStartAnimation);
					}else{
						final Drawable drawableTop = mMenuBarContext.getResources().getDrawable(R.drawable.list_brown);
						this.mZpayList.setText("List View");
						this.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
						
						TransitionAnimation mListAnimation = new TransitionAnimation(0,-90,this.mMiddleViewContainer.getWidth()/2,this.mMiddleViewContainer.getHeight()/2,300f,true);
						mListAnimation.setDuration(400);
						mListAnimation.setFillAfter(true);
						mListAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								mMiddleViewContainer.post(new Runnable() {
									@Override
									public void run() {
										mListView.setVisibility(View.GONE);
										mMapView.setVisibility(View.VISIBLE);
										mMapView.bringToFront();
										
										TransitionAnimation mListEndAnimation = new TransitionAnimation(90,0,mMiddleViewContainer.getWidth()/2,mMiddleViewContainer.getHeight()/2,300f,false);
										mListEndAnimation.setDuration(400);
										mListEndAnimation.setFillAfter(true);
										mMapView.startAnimation(mListEndAnimation);
									}
								});
							}
						});
						mListView.startAnimation(mListAnimation);
					}
				}else{
					if(ZPayFlag.getFlag()==0){
						this.menubarlistimage.setImageResource(R.drawable.list);
						this.menubarlisttext.setText("List View");
						TransitionAnimation mListAnimation = new TransitionAnimation(0,-90,this.mMiddleViewContainer.getWidth()/2,this.mMiddleViewContainer.getHeight()/2,300f,true);
						mListAnimation.setDuration(400);
						mListAnimation.setFillAfter(true);
						mListAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								

								mMiddleViewContainer.post(new Runnable() {

									@Override
									public void run() {

										mListView.setVisibility(View.GONE);
										mMapView.setVisibility(View.VISIBLE);
										mMapView.bringToFront();
										
										TransitionAnimation mListEndAnimation = new TransitionAnimation(90,0,mMiddleViewContainer.getWidth()/2,mMiddleViewContainer.getHeight()/2,300f,false);
										mListEndAnimation.setDuration(400);
										mListEndAnimation.setFillAfter(true);
										mMapView.startAnimation(mListEndAnimation);
									}
								});
							}
						});
						mListView.startAnimation(mListAnimation);
					}else{
						final Drawable drawableTop = mMenuBarContext.getResources().getDrawable(R.drawable.map_view_brown);
						this.mZpayList.setText("Map View");
						this.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
						
						TransitionAnimation mStartAnimation = new TransitionAnimation(0,90,this.mMiddleViewContainer.getWidth()/2,this.mMiddleViewContainer.getHeight()/2,300f,true);
						mStartAnimation.setDuration(400);
						mStartAnimation.setFillAfter(true);
						mStartAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								
								mMapView.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
								mListView.bringToFront();
								TransitionAnimation mStartEndAnimation = new TransitionAnimation(-90, 0,mMiddleViewContainer.getWidth()/2,mMiddleViewContainer.getHeight()/2,300f,false);
								mStartEndAnimation.setDuration(400);
								mStartEndAnimation.setFillAfter(true);
								mListView.startAnimation(mStartEndAnimation);
							}
						});
						mMapView.startAnimation(mStartAnimation);
					}
				}
				MenuBarListClickListener.sClickFlag=!MenuBarListClickListener.sClickFlag;
				/*}else{
				Toast.makeText(this.MenuBarContext,this.menubarlisttext.getText().toString(), Toast.LENGTH_SHORT).show();
			}*/
		}else if(mClassName.equals("Location")){
			TranslateLocationMapView();
		}else if(mClassName.equals("CardPurchase")){
			if(!MenuBarListClickListener.sZpayClickFlag){
				final Drawable drawableTop = mMenuBarContext.getResources().getDrawable(R.drawable.list_brown);
				this.mZpayList.setText("List View");
				this.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);

				TransitionAnimation mListAnimation = new TransitionAnimation(0,-90,this.mMiddleViewContainer.getWidth()/2,this.mMiddleViewContainer.getHeight()/2,300f,true);
				mListAnimation.setDuration(400);
				mListAnimation.setFillAfter(true);
				mListAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						

						mMiddleViewContainer.post(new Runnable() {

							@Override
							public void run() {
								mListView.setVisibility(View.GONE);
								mMapView.setVisibility(View.VISIBLE);
								mMapView.bringToFront();

								TransitionAnimation mListEndAnimation = new TransitionAnimation(90,0,mMiddleViewContainer.getWidth()/2,mMiddleViewContainer.getHeight()/2,300f,false);
								mListEndAnimation.setDuration(400);
								mListEndAnimation.setFillAfter(true);
								mMapView.startAnimation(mListEndAnimation);
							}
						});
					}
				});
				mListView.startAnimation(mListAnimation);
			}else{
				final Drawable drawableTop = mMenuBarContext.getResources().getDrawable(R.drawable.map_view_brown);
				this.mZpayList.setText("Map View");
				this.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);

				TransitionAnimation mStartAnimation = new TransitionAnimation(0,90,this.mMiddleViewContainer.getWidth()/2,this.mMiddleViewContainer.getHeight()/2,300f,true);
				mStartAnimation.setDuration(400);
				mStartAnimation.setFillAfter(true);
				mStartAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						
						mMapView.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						mListView.bringToFront();
						TransitionAnimation mStartEndAnimation = new TransitionAnimation(-90, 0,mMiddleViewContainer.getWidth()/2,mMiddleViewContainer.getHeight()/2,300f,false);
						mStartEndAnimation.setDuration(400);
						mStartEndAnimation.setFillAfter(true);
						mListView.startAnimation(mStartEndAnimation);
					}
				});
				mMapView.startAnimation(mStartAnimation);
			}
			MenuBarListClickListener.sZpayClickFlag=!MenuBarListClickListener.sZpayClickFlag;
		}
	}
	
	public void TranslateLocationMapView(){
		
		if(!this.menubarlisttext.getText().equals(mMenubartext)){
			if(!MenuBarListClickListener.sLocationClickFlag){
				
				this.menubarlistimage.setImageResource(R.drawable.list);
				this.menubarlisttext.setText("List View");
				
				TransitionAnimation mListAnimation = new TransitionAnimation(0,-90,this.mMiddleViewContainer.getWidth()/2,this.mMiddleViewContainer.getHeight()/2,300f,true);
				mListAnimation.setDuration(400);
				mListAnimation.setFillAfter(true);
				mListAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						

						mMiddleViewContainer.post(new Runnable() {

							@Override
							public void run() {
								mListView.setVisibility(View.GONE);
								mMapView.setVisibility(View.VISIBLE);
								mMapView.bringToFront();
								TransitionAnimation mListEndAnimation = new TransitionAnimation(90,0,mMiddleViewContainer.getWidth()/2,mMiddleViewContainer.getHeight()/2,300f,false);
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
				
				TransitionAnimation mStartAnimation = new TransitionAnimation(0,90,this.mMiddleViewContainer.getWidth()/2,this.mMiddleViewContainer.getHeight()/2,300f,true);
				mStartAnimation.setDuration(400);
				mStartAnimation.setFillAfter(true);
				mStartAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						
						mMapView.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						mListView.bringToFront();
						TransitionAnimation mStartEndAnimation = new TransitionAnimation(-90, 0,mMiddleViewContainer.getWidth()/2,mMiddleViewContainer.getHeight()/2,300f,false);
						mStartEndAnimation.setDuration(400);
						mStartEndAnimation.setFillAfter(true);
						mListView.startAnimation(mStartEndAnimation);
					}
				});
				mMapView.startAnimation(mStartAnimation);
			}
			MenuBarListClickListener.sLocationClickFlag=!MenuBarListClickListener.sLocationClickFlag;
		}else{
			Toast.makeText(this.mMenuBarContext,this.menubarlisttext.getText().toString(), Toast.LENGTH_SHORT).show();
		}
	}
}