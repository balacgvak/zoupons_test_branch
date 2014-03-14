package com.us.zoupons.listview_inflater_classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.CardId_ClassVariable;
import com.us.zoupons.classvariables.CardOnFiles_ClassVariables;
import com.us.zoupons.classvariables.EditCardDetails_ClassVariables;
import com.us.zoupons.classvariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.wallet.ManageWallets;
import com.us.zoupons.storeowner.billing.ActivateCardTask;

/**
 * 
 * Adapter to list customer and store billing credit cards
 *
 */

public class CardOnFiles_Adapter extends BaseAdapter implements OnCheckedChangeListener{

	private LayoutInflater CardOnFilesInflater;
	private Context mContext;
	private String mEventFlag;
	public static int checked_position=-1;
	public static int previous_checked_position = 0;
	private ViewHolder holder = null;
	
	public CardOnFiles_Adapter(Context context,String eventFlag) {
		CardOnFilesInflater=LayoutInflater.from(context);
		this.mEventFlag = eventFlag; 
		this.mContext=context;
		checked_position = -1;
		previous_checked_position = 0;
	}

	@Override
	public int getCount() {
		return WebServiceStaticArrays.mCardOnFiles.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// To get credit card details for each list item
		final CardOnFiles_ClassVariables obj = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(position);
		if(convertView==null){
			convertView = CardOnFilesInflater.inflate(R.layout.listview_managecards, null);
			holder=new ViewHolder();
			holder.CardImage=(ImageView) convertView.findViewById(R.id.managecards_cardImage);
			holder.CardName=(TextView) convertView.findViewById(R.id.managecards_cardName);
			holder.BtnEdit=(Button) convertView.findViewById(R.id.btnEdit);
			holder.BtnRemove=(Button) convertView.findViewById(R.id.btnRemove);
			holder.mChooseCardCheckBox=(CheckBox) convertView.findViewById(R.id.storeowner_billing_checkboxId);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		if(mEventFlag.equalsIgnoreCase("customer_creditcards")){
			holder.mChooseCardCheckBox.setVisibility(View.GONE);
		}else{
			holder.CardName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			holder.mChooseCardCheckBox.setVisibility(View.VISIBLE);
			holder.mChooseCardCheckBox.setTag(position);
			if(checked_position != -1){
				if(position == checked_position){
					 holder.mChooseCardCheckBox.setOnCheckedChangeListener(null);
					 holder.mChooseCardCheckBox.setChecked(true);
				}else{
					holder.mChooseCardCheckBox.setOnCheckedChangeListener(null);
					holder.mChooseCardCheckBox.setChecked(false);
				}	
			}else{
				if(obj.status.equalsIgnoreCase("active")){
					 previous_checked_position = position;
					 holder.mChooseCardCheckBox.setOnCheckedChangeListener(null);
					 holder.mChooseCardCheckBox.setChecked(true);
				}
			}
			holder.mChooseCardCheckBox.setOnCheckedChangeListener(this);
		}
		if(obj.cardType.equals("Visa")){
			holder.CardImage.setImageResource(R.drawable.visacard);
		}else if(obj.cardType.equals("Mastercard")){
			holder.CardImage.setImageResource(R.drawable.mastercard);
		}else{
			holder.CardImage.setImageResource(R.drawable.discovercard);
		}
		holder.CardName.setText(obj.cardMask);
		try{
			holder.BtnEdit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					//Toast.makeText(mContext, "Edit CardName : "+obj.cardName, Toast.LENGTH_SHORT).show();
					ManageCardAddPin_ClassVariables.mEditCardFlag="true";
					ManageCardAddPin_ClassVariables.mAddPinFlag="false";
					CardId_ClassVariable.cardid=obj.cardId;
					EditCardDetails_ClassVariables.cardName=obj.cardName;
					EditCardDetails_ClassVariables.CardHolderName=obj.CardHolderName;
					EditCardDetails_ClassVariables.cardNumber=obj.cardMask;
					EditCardDetails_ClassVariables.cardType=obj.cardType;
					EditCardDetails_ClassVariables.cvv=obj.cvv;
					EditCardDetails_ClassVariables.expiryYear=obj.expiryYear;
					EditCardDetails_ClassVariables.expiryMonth=obj.expiryMonth;
					EditCardDetails_ClassVariables.StreetAddress=obj.StreatAddress;
					EditCardDetails_ClassVariables.StreatNumber=obj.StreatNumber;
					EditCardDetails_ClassVariables.zipCode=obj.zipCode;
					if(mContext.getClass().getSimpleName().equalsIgnoreCase("ManageWallets")){
						ManageWallets.mAuthenticateFlag="editcard";
						ManageWallets.mManageCards_Login_PasswordValue.setText("");
						ManageWallets.mManageCards_CardListContainer.setVisibility(View.GONE);
						ManageWallets.mManageCards_LoginCredentialsContainer.setVisibility(View.VISIBLE);
					}else{
						//From Store owner billing to change the view in to edit view.
					}
				}
			});

			holder.BtnRemove.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					//Toast.makeText(mContext, "Remove CardName : "+obj.cardName, Toast.LENGTH_SHORT).show();
					if(obj.status.equalsIgnoreCase("active") && !mEventFlag.equalsIgnoreCase("customer_creditcards")){
						AlertDialog.Builder mActivateCardAlert = new AlertDialog.Builder(mContext);
						mActivateCardAlert.setTitle("Information");
						mActivateCardAlert.setMessage("Active credit cards cannot be removed");
						mActivateCardAlert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();	
							}
						});
						mActivateCardAlert.show();
					}else{
						CardId_ClassVariable.cardid=obj.cardId;
						ManageWallets.mAuthenticateFlag="removecard";
						ManageWallets.mManageCards_Login_PasswordValue.setText("");
						ManageWallets.mManageCards_CardListContainer.setVisibility(View.GONE);
						ManageWallets.mManageCards_LoginCredentialsContainer.setVisibility(View.VISIBLE);	
					}
					
					
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
	}

	static class ViewHolder{
		private ImageView CardImage;
		private TextView CardName;
		private Button BtnEdit,BtnRemove;
		private CheckBox mChooseCardCheckBox;
	}

	@Override
	public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
         
		if(isChecked){
			AlertDialog.Builder mActivateCardAlert = new AlertDialog.Builder(mContext);
			mActivateCardAlert.setTitle("Information");
			mActivateCardAlert.setMessage("Do you want to activate this credit card for zoupon payment subscription");
			mActivateCardAlert.setCancelable(false);
			mActivateCardAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					final CardOnFiles_ClassVariables obj = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(Integer.valueOf(buttonView.getTag().toString()));
					if(new NetworkCheck().ConnectivityCheck(mContext)){
						// To activate credit card for billing
						ActivateCardTask mActivateCard = new ActivateCardTask(mContext,obj.cardId,CardOnFiles_Adapter.this,Integer.valueOf(buttonView.getTag().toString()));
						mActivateCard.execute();	
					}else{
						Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();	
					}
				}
			});
			mActivateCardAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					checked_position = previous_checked_position;
					notifyDataSetChanged();
				}
			});
			mActivateCardAlert.show();
		}else{
			// To assign checked position and refreshing adapter
			checked_position = Integer.valueOf(buttonView.getTag().toString());
			notifyDataSetChanged();
		}
	}
}
