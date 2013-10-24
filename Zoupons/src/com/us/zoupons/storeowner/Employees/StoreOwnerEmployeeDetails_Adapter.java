package com.us.zoupons.storeowner.Employees;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.storeowner.AddEmployee.EmployeePermissionClassVariables;

public class StoreOwnerEmployeeDetails_Adapter extends ArrayAdapter<Object>{
	
	View row;
    ArrayList<Object> mLocations;
    int resLayout;
    Context context;
        
    public StoreOwnerEmployeeDetails_Adapter(Context context, int textViewResourceId, ArrayList<Object> locations) {
        super(context, textViewResourceId,locations);
        this.mLocations = locations;
        resLayout = textViewResourceId;
        this.context = context;
       
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
    	try{
    	row = convertView;
    	if(row == null)
    	{   // inflate our custom layout. resLayout == R.layout.row_team_layout.xml
    		LayoutInflater ll = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		row = ll.inflate(resLayout, parent, false);
    	}
    	if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_AddEmployee")){ //loading Permission For add employee 
    		POJOStoreInfo mStoreLocationdetails = (POJOStoreInfo) mLocations.get(position); // Produce a row for each Team.
    		if(mStoreLocationdetails != null)
    		{   // Find our widgets and populate them with the Team data.
    			TextView mLocationAddress = (TextView) row.findViewById(R.id.listview_choosedlocationaddress);
    			if(mLocationAddress != null)
    				//mLocationAddress.setText(mStoreLocationdetails.address_line1+", "+mStoreLocationdetails.city+", "+mStoreLocationdetails.state+" "+mStoreLocationdetails.zip_code);
    				mLocationAddress.setText(mStoreLocationdetails.address_line1 + "\n" + mStoreLocationdetails.city+", "+mStoreLocationdetails.state+" "+mStoreLocationdetails.zip_code);
    		}
    	}else{ //loading Permission For edit employee permissions
    		EmployeePermissionClassVariables mStoreLocationdetails = (EmployeePermissionClassVariables) mLocations.get(position); // Produce a row for each Team.
    		if(mStoreLocationdetails != null)
    		{   // Find our widgets and populate them with the Team data.
    			TextView mLocationAddress = (TextView) row.findViewById(R.id.listview_choosedlocationaddress);
    			if(mLocationAddress != null){
    				//mLocationAddress.setText(mStoreLocationdetails.mLocationPermissionaddress_line1+", "+mStoreLocationdetails.mLocationPermissioncity+", "+mStoreLocationdetails.mLocationPermissionstate+" "+mStoreLocationdetails.mLocationPermissionzip_code);
    				mLocationAddress.setText(mStoreLocationdetails.mLocationPermissionaddress_line1 + "\n" + mStoreLocationdetails.mLocationPermissioncity+", "+mStoreLocationdetails.mLocationPermissionstate+" "+mStoreLocationdetails.mLocationPermissionzip_code);
    			}
    		}
    	}
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return row;
    }
 
}
