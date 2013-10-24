package com.us.zoupons.notification;

import java.io.Serializable;

public class NotificationDetails implements Serializable{
	public String id="";
	public String notify_to="";
	public String notification_type="";
	public String notification_msg="";
	public String store_id="";
	public String status="";
	public String created="";
	public static int notificationcount=0;
	public String timeZone="";
	public String full_name="",store_name="",notification_type_id="",notification_shortmessage="",notification_longmessage="",notification_locationId="";
}


