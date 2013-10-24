package com.us.zoupons.storeowner.refunds;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
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

import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.invoice.CheckZouponsCustomerTask;

public class RefundDetails extends Activity {

	public static String TAG="StoreOwnerRefunds";
	public static MyHorizontalScrollView scrollView;
	View app;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	Header header;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mLeftMenu;
	int mMenuFlag;
	private Button mStoreOwnerRefundsFreezeView,mStoreOwnerRefundTelephoneSubmitButton,mStoreOwnerIssueRefundButton;
	private LinearLayout mStoreOwnerRefundTelephoneLayout,mStoreOwnerRefundCustomerDetailsContainer,mStoreOwnerRefundDetailsContainer,mBackLayout;
	private EditText mStoreOwnerRefundTelephoneValue,mStoreOwnerRefundCustomerFirstName,mStoreOwnerRefundCustomerLastName,mStoreOwnerRefundAmount,mStoreOwnerRefundEmployeePin;
	private ImageView mStoreOwnerRefundCustomerImage;
	private ListView mStoreOwnerRefundList,mStoreOwnerRefundDetailsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		mConnectionAvailabilityChecking = new NetworkCheck();
		app = inflater.inflate(R.layout.storeowner_refunds, null);
		ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeowner_refund_middleview);
		ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeowner_refund_footer);
		mStoreOwnerRefundsFreezeView = (Button) app.findViewById(R.id.storeowner_refund_freezeview);
		storeowner_leftmenu = new StoreOwner_LeftMenu(RefundDetails.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerRefundsFreezeView, TAG);
		mLeftMenu = storeowner_leftmenu.intializeInflater();
		storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
		/* Header Tab Bar which contains logout,notification and home buttons*/
		header = (Header) app.findViewById(R.id.storeowner_refund_header);
		header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerRefundsFreezeView, TAG);
		final View[] children = new View[] { mLeftMenu, app};
		/* Scroll to app (view[1]) when layout finished.*/
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		mStoreOwnerRefundsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerRefundsFreezeView, TAG));
		// Initialisation of Refunds initial telephone entry views
		mStoreOwnerRefundTelephoneLayout = (LinearLayout) mMiddleView.findViewById(R.id.initial_telephone_layout);
		mStoreOwnerRefundTelephoneValue = (EditText) mStoreOwnerRefundTelephoneLayout.findViewById(R.id.storeowner_refund_phoneNumberId);
		mStoreOwnerRefundTelephoneSubmitButton = (Button) mStoreOwnerRefundTelephoneLayout.findViewById(R.id.storeowner_refund_phoneNumber_submit);
		// Initialisation of refunds center customer details and refund list views
		mStoreOwnerRefundCustomerDetailsContainer =  (LinearLayout) mMiddleView.findViewById(R.id.storeowner_customer_refund_info);
		mStoreOwnerRefundCustomerFirstName = (EditText) mStoreOwnerRefundCustomerDetailsContainer.findViewById(R.id.storeowner_customer_refund_FirstName);
		mStoreOwnerRefundCustomerLastName = (EditText) mStoreOwnerRefundCustomerDetailsContainer.findViewById(R.id.storeowner_customer_refund_LastName);
		mStoreOwnerRefundCustomerImage = (ImageView) mStoreOwnerRefundCustomerDetailsContainer.findViewById(R.id.storeowner_customer_refund_ImageId);
		mStoreOwnerRefundList = (ListView) mStoreOwnerRefundCustomerDetailsContainer.findViewById(R.id.storeowner_customer_refund_list);
		// Initialisation of refunds details and issue refunds views
		mStoreOwnerRefundDetailsContainer =  (LinearLayout) mMiddleView.findViewById(R.id.storeowner_customer_refund_details_container);
		mStoreOwnerRefundAmount = (EditText) mStoreOwnerRefundDetailsContainer.findViewById(R.id.storeowner_refund_amount);
		mStoreOwnerRefundEmployeePin = (EditText) mStoreOwnerRefundDetailsContainer.findViewById(R.id.storeowner_refund_employeePin);
		mStoreOwnerIssueRefundButton = (Button) mStoreOwnerRefundDetailsContainer.findViewById(R.id.storeowner_refund_issueButton);
		mStoreOwnerRefundDetailsList = (ListView) mStoreOwnerRefundDetailsContainer.findViewById(R.id.storeowner_customer_refund_details_list);
		// Initialisation of footer views
		mBackLayout = (LinearLayout) mFooterView.findViewById(R.id.storeowner_refund_footer_back);
		mBackLayout.setVisibility(View.GONE);
		// To validate Mobile Number field
		mStoreOwnerRefundTelephoneValue.addTextChangedListener(new MobileNumberTextWatcher());
		mStoreOwnerRefundTelephoneValue.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});

		mStoreOwnerRefundTelephoneSubmitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckZouponsCustomerTask customerTask = new CheckZouponsCustomerTask(RefundDetails.this, mStoreOwnerRefundTelephoneLayout,mStoreOwnerRefundCustomerDetailsContainer,mStoreOwnerRefundCustomerImage,mStoreOwnerRefundList,mBackLayout);
				customerTask.execute("");
			}
		});

		mStoreOwnerRefundList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mStoreOwnerRefundCustomerDetailsContainer.setVisibility(View.GONE);
				mStoreOwnerRefundAmount.getText().clear();
				mStoreOwnerRefundEmployeePin.getText().clear();
				mStoreOwnerRefundDetailsContainer.setVisibility(View.VISIBLE);
				mStoreOwnerRefundDetailsList.setAdapter(new CustomRefundDetailsAdapter(RefundDetails.this, "refunds_details"));
			}
		});

		mBackLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mStoreOwnerRefundCustomerDetailsContainer.getVisibility() == View.VISIBLE){
					mStoreOwnerRefundCustomerDetailsContainer.setVisibility(View.GONE);
					mStoreOwnerRefundTelephoneLayout.setVisibility(View.VISIBLE);
					mBackLayout.setVisibility(View.GONE);
				}else if(mStoreOwnerRefundDetailsContainer.getVisibility() == View.VISIBLE){
					mStoreOwnerRefundDetailsContainer.setVisibility(View.GONE);
					mStoreOwnerRefundCustomerDetailsContainer.setVisibility(View.VISIBLE);
				}
			}
		});

		mStoreOwnerIssueRefundButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IssueRefundTask mRefundTask = new IssueRefundTask(RefundDetails.this, mStoreOwnerRefundAmount.getText().toString(), mStoreOwnerRefundDetailsList);
				mRefundTask.execute();
			}
		});

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

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


