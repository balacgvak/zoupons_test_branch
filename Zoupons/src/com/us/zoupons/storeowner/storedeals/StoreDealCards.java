package com.us.zoupons.storeowner.storedeals;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreDealCards extends Activity {

	public static String TAG="StoreOwner_StoreDeals";

	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;

	private Header header;
	private View mRightMenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu storeowner_leftmenu;

	private Button mStoreOwner_deals_FreezeView;
	private ListView mStoreOwner_deals_ListView;
	private LinearLayout mStoreOwner_deals_Back,mStoreOwner_menusplitter;
	Button mStoreOwner_deals_Save;
	private ImageView mStoreOwner_deals_CardImage;
	private TextView mStoreOwner_deals_FaceValue;
	private EditText mStoreOwner_deals_ChargeValue,mStoreOwner_deals_WeekSaleValue;
	private RelativeLayout mStoreOwner_deals_DetailsView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();

		app = inflater.inflate(R.layout.storeowner_deals, null);

		ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeownerdeals_middleview);
		ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeownerdeals_footer);

		mStoreOwner_deals_FreezeView = (Button) app.findViewById(R.id.storeownerdeals_freezeview);
		mStoreOwner_deals_ListView = (ListView) mMiddleView.findViewById(R.id.storeownerdeals_ListView);
		mStoreOwner_deals_DetailsView = (RelativeLayout) mMiddleView.findViewById(R.id.storeownerdeals_detailsView);
		mStoreOwner_deals_CardImage =(ImageView) mStoreOwner_deals_DetailsView.findViewById(R.id.storeownerdeals_details_cardImageId);
		mStoreOwner_deals_FaceValue = (TextView) mStoreOwner_deals_DetailsView.findViewById(R.id.storeownerdeals_details_cardvalueId);
		mStoreOwner_deals_ChargeValue = (EditText) mStoreOwner_deals_DetailsView.findViewById(R.id.storeownerdeals_details_chargevalueId);
		mStoreOwner_deals_WeekSaleValue = (EditText) mStoreOwner_deals_DetailsView.findViewById(R.id.storeownerdeals_details_weeksalevalueId);
		mStoreOwner_deals_Back = (LinearLayout) mFooterView.findViewById(R.id.storeownerdeals_footer_back);
		mStoreOwner_deals_Save = (Button) mStoreOwner_deals_DetailsView.findViewById(R.id.storeonwerdeals_details_save); 
		mStoreOwner_menusplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter1);
		storeowner_leftmenu = new StoreOwner_LeftMenu(StoreDealCards.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_deals_FreezeView, TAG);
		mLeftMenu = storeowner_leftmenu.intializeInflater();

		storeowner_leftmenu.clickListener(mLeftMenu);

		/* Header Tab Bar which contains logout,notification and home buttons*/
		header = (Header) app.findViewById(R.id.storeownerdeals_header);
		header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_deals_FreezeView, TAG);
		//header.mTabBarNotificationContainer.setVisibility(View.GONE);
		final View[] children = new View[] { mLeftMenu, app };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		mStoreOwner_deals_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_deals_FreezeView, TAG));

		// async task to get store deals
		StoreDealsAsyncTask mStoreDealCards =  new StoreDealsAsyncTask(StoreDealCards.this, "Progress", mStoreOwner_deals_ListView);
		mStoreDealCards.execute();


		mStoreOwner_deals_Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mStoreOwner_deals_DetailsView.getVisibility() == View.VISIBLE){ // If details visible move to deals list view
					mStoreOwner_deals_ListView.setVisibility(View.VISIBLE);
					mStoreOwner_deals_DetailsView.setVisibility(View.GONE);
					mStoreOwner_deals_Back.setVisibility(View.INVISIBLE);
					mStoreOwner_menusplitter.setVisibility(View.INVISIBLE);
				}else{ // move to home page
				}
			}
		});

		mStoreOwner_deals_Save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(StoreDealCards.this, "saved", Toast.LENGTH_SHORT).show();
			}
		});

		mStoreOwner_deals_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				mStoreOwner_deals_ListView.setVisibility(View.GONE);
				mStoreOwner_deals_DetailsView.setVisibility(View.VISIBLE);
				mStoreOwner_deals_Back.setVisibility(View.VISIBLE);
				mStoreOwner_menusplitter.setVisibility(View.VISIBLE);
			}
		});

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


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwner_deals_ListView.setVisibility(View.VISIBLE);
	}

}

