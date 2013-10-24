/**
 * 
 */
package com.us.zoupons;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.us.zoupons.MainMenu.MainMenuActivity;

/**
 * Favorite stores,coupons and friends favorite stores
 */
public class Favorites extends MainMenuActivity {

	MyHorizontalScrollView scrollView;
    View leftMenu,rightMenu;
    View app;
    ImageView btnSlide;
    public Typeface mZouponsFont;
    public static String TAG = "Favorites";
    public int mFavoritesScreenWidth;
    public double mFavoritesMenuWidth;
	public LinearLayout mMenuBarFavStores,mMenuBarFavCoupons,mMenuBarFriendsFavStores;
    public TextView mMenuBarFavStoresText,mMenuBarFavCouponsText,mMenuBarFriendsFavStoresText;
    public Button mFavoritesFreezeView;
    
    public ListView mFavoritesListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		favorites(R.layout.favorites);
	}

    @Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
