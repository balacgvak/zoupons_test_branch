package com.us.zoupons.classvariables;

public class POJOFBfriendListData {
	
	public String friend_id="";
	public String friend_username="";
	public String name="";
	public String photo_url="";
	public String zouponsfriend="";
	public String totalcount="";
	public String timezone="";
	public String google_friends_updated_time="";
	public String friend_provider="";
	public String friend_email="";
	
	public String getFriend_username() {
		return friend_username;
	}
	public void setFriend_username(String friend_username) {
		this.friend_username = friend_username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto_url() {
		return photo_url;
	}
	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	public String getZouponsfriend() {
		return zouponsfriend;
	}
	public void setZouponsfriend(String zouponsfriend) {
		this.zouponsfriend = zouponsfriend;
	}
	public String getFriend_email() {
		return friend_email;
	}
	public void setFriend_email(String friend_email) {
		this.friend_email = friend_email;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != POJOFBfriendListData.class) {
	        return false;
	    }
		POJOFBfriendListData other = (POJOFBfriendListData) obj;
	    if (!other.friend_email.equalsIgnoreCase(this.friend_email)) {
	        return false;
	    }
	    return true;
	}
	
	@Override
	public int hashCode() {
		 int result = 7;
		 result += friend_email.hashCode() * 31;
		 return result;
	}
	
}
