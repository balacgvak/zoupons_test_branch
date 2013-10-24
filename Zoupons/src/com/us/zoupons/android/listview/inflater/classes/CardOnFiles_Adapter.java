package com.us.zoupons.android.listview.inflater.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.ManageCards;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.CardId_ClassVariable;
import com.us.zoupons.ClassVariables.CardOnFiles_ClassVariables;
import com.us.zoupons.ClassVariables.EditCardDetails_ClassVariables;
import com.us.zoupons.ClassVariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.AddEmployee.Add_EmployeeTask;
import com.us.zoupons.storeowner.AddEmployee.StoreOwner_AddEmployee;
import com.us.zoupons.storeowner.billing.ActivateCardTask;

public class CardOnFiles_Adapter extends BaseAdapter implements OnCheckedChangeListener{

	private LayoutInflater CardOnFilesInflater;
	Context ctx;
	String TAG="CardOnFiles_Adapter",mEventFlag;
	public static int checked_position=-1;
	private int previous_checked_position = 0;

	public CardOnFiles_Adapter(Context context,String eventFlag) {
		CardOnFilesInflater=LayoutInflater.from(context);
		this.mEventFlag = eventFlag; 
		this.ctx=context;
		checked_position = -1;
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
		ViewHolder holder = null;
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
				/*if(obj.Status.equalsIgnoreCase("active")){
					 mChooseCardCheckBox(true);
				}*/
			}
			if(obj.status.equalsIgnoreCase("active")){
				 previous_checked_position = position;
				 holder.mChooseCardCheckBox.setOnCheckedChangeListener(null);
				 holder.mChooseCardCheckBox.setChecked(true);
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
					//Toast.makeText(ctx, "Edit CardName : "+obj.cardName, Toast.LENGTH_SHORT).show();
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
					EditCardDetails_ClassVariables.StreatAddress=obj.StreatAddress;
					EditCardDetails_ClassVariables.StreatNumber=obj.StreatNumber;
					EditCardDetails_ClassVariables.zipCode=obj.zipCode;
					if(ctx.getClass().getSimpleName().equalsIgnoreCase("ManageCards")){
						ManageCards.mAuthenticateFlag="editcard";
						ManageCards.mManageCards_Login_PasswordValue.setText("");
						ManageCards.mManageCards_CardListContainer.setVisibility(View.GONE);
						ManageCards.mManageCards_LoginCredentialsContainer.setVisibility(View.VISIBLE);
					}else{
						//From Store owner billing to change the view in to edit view.
					}
				}
			});

			holder.BtnRemove.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					//Toast.makeText(ctx, "Remove CardName : "+obj.cardName, Toast.LENGTH_SHORT).show();
					CardId_ClassVariable.cardid=obj.cardId;
					ManageCards.mAuthenticateFlag="removecard";
					ManageCards.mManageCards_Login_PasswordValue.setText("");
					ManageCards.mManageCards_CardListContainer.setVisibility(View.GONE);
					ManageCards.mManageCards_LoginCredentialsContainer.setVisibility(View.VISIBLE);
					
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
			AlertDialog.Builder mActivateCardAlert = new AlertDialog.Builder(ctx);
			mActivateCardAlert.setTitle("Information");
			mActivateCardAlert.setMessage("Do you want to activate this credit card for zoupon payment subscription");
			mActivateCardAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					final CardOnFiles_ClassVariables obj = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(Integer.valueOf(buttonView.getTag().toString()));
					/*if(new NetworkCheck().ConnectivityCheck(ctx)){
						ActivateCardTask mActivateCard = new ActivateCardTask(ctx,obj.cardId,CardOnFiles_Adapter.this,Integer.valueOf(buttonView.getTag().toString()));
						mActivateCard.execute();	
					}else{
						Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();	
					}*/
					checked_position = Integer.valueOf(buttonView.getTag().toString());
					previous_checked_position = checked_position;
					notifyDataSetChanged();
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
			checked_position = Integer.valueOf(buttonView.getTag().toString());
			notifyDataSetChanged();
		}
	}
}
