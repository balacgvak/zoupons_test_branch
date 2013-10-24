package com.us.zoupons.storeowner.AddEmployee;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.us.zoupons.R;

public class StoreEmployeePermissionListAdapter extends ArrayAdapter<Object>{
	
	View row;
    ArrayList<Object> mModulePermissions;
    int resLayout;
    Context context;
    
    public StoreEmployeePermissionListAdapter(Context context, int textViewResourceId, ArrayList<Object> ModulePermissions) {
        super(context, textViewResourceId,ModulePermissions);
        this.mModulePermissions = ModulePermissions;
        resLayout = textViewResourceId;
        this.context = context;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        row = convertView;
        if(row == null)
        {   // inflate our custom layout. resLayout == R.layout.row_team_layout.xml
            LayoutInflater ll = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = ll.inflate(resLayout, parent, false);
        }

        EmployeePermissionClassVariables mModulePermissionList = (EmployeePermissionClassVariables) mModulePermissions.get(position); // Produce a row for each Team.

        if(mModulePermissionList != null)
        {   // Find our widgets and populate them with the Team data.
            TextView mLocationAddress = (TextView) row.findViewById(R.id.listview_choosedlocationaddress);
            
            if(mLocationAddress != null)
            	mLocationAddress.setText(mModulePermissionList.mPermissionName);
            
        }
        return row;
    }
}
