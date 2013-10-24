package com.us.zoupons.android.listview.inflater.classes;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.cards.StoreGiftCardDetails;
import com.us.zoupons.zpay.Step2_ManageCards;

public class Favorites_Adapter extends BaseAdapter{

	private LayoutInflater FavoritesInflater;
	private Context FavoritesContext;
	private ArrayList<POJOStoreInfo> mFavouriteStoreList;
	public ImageLoader imageLoader; 
	public String mClassName;
	public String mPageFlag;
	
	//Constructor for Favorites page
	public Favorites_Adapter(Context context, ArrayList<POJOStoreInfo> FavouriteStoreDetails,String classname,String pageflag){
		FavoritesInflater = LayoutInflater.from(context);
		this.FavoritesContext = context;
		this.mFavouriteStoreList = FavouriteStoreDetails;
		imageLoader=new ImageLoader(context);
		this.mClassName=classname;
		this.mPageFlag=pageflag;
	}

	@Override
	public int getCount() {
		return mFavouriteStoreList.size();
	}

	@Override
	public Object getItem(int position) {
		return mFavouriteStoreList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Log.i("favourites", "getview");
		if(convertView==null){
			convertView=FavoritesInflater.inflate(R.layout.favoriteslistview, null);
			holder=new ViewHolder();
			holder.mFavouritesStoreName=(TextView) convertView.findViewById(R.id.favourite_storeNameId);
			holder.mFavouritesStoreCategory=(TextView) convertView.findViewById(R.id.favourite_storecategoryId);
			holder.mFavouritesStoreType=(TextView) convertView.findViewById(R.id.favourite_storeTypeId);
			holder.mFavouriteStoreDistance=(TextView) convertView.findViewById(R.id.favourite_storeDistanceId);
			holder.mFavouriteStoreImage = (ImageView) convertView.findViewById(R.id.favourite_storeImageId);
			holder.mFavouriteStoreLikeCount = (TextView) convertView.findViewById(R.id.favourite_storeLikesId);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		POJOStoreInfo mFavouriteStoreDetails = (POJOStoreInfo) mFavouriteStoreList.get(position);
		holder.mFavouritesStoreName.setText(mFavouriteStoreDetails.store_name);
		// setting store like count
		holder.mFavouriteStoreLikeCount.setText(mFavouriteStoreDetails.like_count);	
		holder.mFavouritesStoreCategory.setText(mFavouriteStoreDetails.address_line1);
		if(mFavouriteStoreDetails.store_distance.equalsIgnoreCase("-1")){
			holder.mFavouritesStoreType.setText("Online");
			holder.mFavouriteStoreDistance.setText("");
			holder.mFavouriteStoreLikeCount.setVisibility(View.INVISIBLE);
		}else{
			if(mFavouriteStoreDetails.has_point_of_sale.equalsIgnoreCase("Yes")){
				holder.mFavouritesStoreType.setText("Retail"+"(Preferred)");
				holder.mFavouriteStoreLikeCount.setVisibility(View.VISIBLE);
			}else{
				holder.mFavouritesStoreType.setText("Retail");
				holder.mFavouriteStoreLikeCount.setVisibility(View.INVISIBLE);
			}
			
			holder.mFavouriteStoreDistance.setText(String.format("%.2f", Double.valueOf(mFavouriteStoreDetails.store_distance))+"m");
		}
		
		imageLoader.DisplayImage(mFavouriteStoreDetails.store_image_path+"&w="+90+"&h="+70+"&zc=0",holder.mFavouriteStoreImage);
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				POJOStoreInfo mStoreInformation = (POJOStoreInfo) getItem(position);
				RightMenuStoreId_ClassVariables.mStoreID = mStoreInformation.store_id;
				RightMenuStoreId_ClassVariables.mStoreName = mStoreInformation.store_name;
				RightMenuStoreId_ClassVariables.mLocationId = mStoreInformation.location_id;
				RightMenuStoreId_ClassVariables.mSelectedStore_lat = mStoreInformation.mLatitude;
				RightMenuStoreId_ClassVariables.mSelectedStore_long = mStoreInformation.mLongitude;
				
				
				if(mStoreInformation.store_distance.equalsIgnoreCase("-1")){
					RightMenuStoreId_ClassVariables.mStoreLocation = "online";
				}else{
					RightMenuStoreId_ClassVariables.mStoreLocation = mStoreInformation.address_line1 + "\n" + mStoreInformation.city+", "+mStoreInformation.state+" "+mStoreInformation.zip_code;	
				}
				
				if(mClassName.equals("Favorites")){
					Log.i("list", "click");
					int menuWidth = MainMenuActivity.leftMenu.getMeasuredWidth();
					// Ensure menu is visible
					MainMenuActivity.rightMenu.setVisibility(View.VISIBLE);
					int right = menuWidth+menuWidth;
					MainMenuActivity.scrollView.smoothScrollTo(right, 0);
					MenuOutClass.WHOLE_MENUOUT=true;
					MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.VISIBLE);
					MainMenuActivity.storeposition = position;
					
					
					//ReAssign RightMenu Item status
					RightMenuStoreId_ClassVariables.rightmenu_location_flag = mStoreInformation.has_location;
					RightMenuStoreId_ClassVariables.rightmenu_coupons_flag = mStoreInformation.has_coupons ;
					RightMenuStoreId_ClassVariables.rightmenu_aboutus_flag = mStoreInformation.has_about_us;
					RightMenuStoreId_ClassVariables.rightmenu_reviewandrating_flag = mStoreInformation.has_review_and_rating;
					RightMenuStoreId_ClassVariables.rightmenu_photos_flag = mStoreInformation.has_photos;
					RightMenuStoreId_ClassVariables.rightmenu_contactstore_flag = mStoreInformation.has_contact_store;
					RightMenuStoreId_ClassVariables.rightmenu_giftcards_flag = mStoreInformation.has_giftcard;
					RightMenuStoreId_ClassVariables.rightmenu_videos_flag = mStoreInformation.has_videos;
					RightMenuStoreId_ClassVariables.rightmenu_invoicecenter_flag = mStoreInformation.has_invoicecenter;
					RightMenuStoreId_ClassVariables.rightmenu_communication_flag = mStoreInformation.has_communication;
					RightMenuStoreId_ClassVariables.rightmenu_pointofsale_flag = mStoreInformation.has_point_of_sale;
					RightMenuStoreId_ClassVariables.rightmenu_zcards_flag = mStoreInformation.has_zcards;
					RightMenuStoreId_ClassVariables.rightmenu_favourite_status = mStoreInformation.favorite_store;
					RightMenuStoreId_ClassVariables.rightmenu_share_flag = mStoreInformation.has_share;
					RightMenuStoreId_ClassVariables.rightmenu_batchsales_flag = mStoreInformation.has_batchsales;
					RightMenuStoreId_ClassVariables.rightmenu_refund_flag = mStoreInformation.has_refund;
					RightMenuStoreId_ClassVariables.rightmenu_mobilepayment_flag = mStoreInformation.has_mobilepayment;
					RightMenuStoreId_ClassVariables.rightmenu_rewardcards_flag = mStoreInformation.has_rewardcards;
					
					MainMenuActivity.SetRightMenuStatus(FavoritesContext);
					MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
					if(MainMenuActivity.mIsfriendfavourite){
						if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
							MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else{
							MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}
					}
				}else{
					//Load from zpay step1 page
					if(mPageFlag.equalsIgnoreCase("giftcard")){
		    			if(mStoreInformation.has_giftcard.equalsIgnoreCase("yes")){
		    				//Move to Giftcards and zcards page
		    				Intent intent_giftcard_zcard = new Intent(FavoritesContext,StoreGiftCardDetails.class);
		    				intent_giftcard_zcard.putExtra("CardType", "Regular");
		    				intent_giftcard_zcard.putExtra("bothcards", "both");
		    				FavoritesContext.startActivity(intent_giftcard_zcard);
		    			}else{
		    				// alert shown
		    				alertBox_service(FavoritesContext,"Information","This Store does't have giftcard.");
		    			}
		    		}else{
		    			if(mStoreInformation.has_point_of_sale.equalsIgnoreCase("yes")){
		    				//Move to step2
		    				Intent intent_step2 = new Intent(FavoritesContext,Step2_ManageCards.class);
		    				intent_step2.putExtra("datasourcename", "zpay");
		    				FavoritesContext.startActivity(intent_step2);
		    			}else{
		    				// alert shown
		    				alertBox_service(FavoritesContext,"Information","This Store does't have payment option.");
		    			}
		    		}
				}
			}
		});
		return convertView;
	}

	static class ViewHolder{
		private TextView mFavouritesStoreName,mFavouritesStoreCategory,mFavouritesStoreType,mFavouriteStoreDistance,mFavouriteStoreLikeCount;
		private ImageView mFavouriteStoreImage;
	}
	
	private void alertBox_service(Context context,String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(context);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}
