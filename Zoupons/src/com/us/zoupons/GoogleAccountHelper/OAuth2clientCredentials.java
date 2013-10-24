package com.us.zoupons.GoogleAccountHelper;

public class OAuth2clientCredentials {
	
	/** Google sign in page url */
	public static final String GOOGLE_SIGN_IN_URL = "https://accounts.google.com/o/oauth2/auth?";
	
	/** Autorization scope parameter */
	public static final String SCOPE = "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https://www.google.com/m8/feeds/";
		
	/** URL to get access token */
	public static final String GET_ACCESSTOKEN = "https://accounts.google.com/o/oauth2/token";
	
	/** URL to get User details */
	public static final String GET_USERINFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
		
	/** Value of the "Client ID" shown under "Client ID for installed applications". */
	public static final String CLIENT_ID_UNSIGNED = "359363876863.apps.googleusercontent.com";
	
	/** Value of the "Client ID" shown under "Client ID for installed applications". */
	public static final String CLIENT_ID = "717036216377-vs2rdmk7h50bks4fb5ub89llioaji55u.apps.googleusercontent.com";

	/** Value of the "Client secret" shown under "Client ID for installed applications". */
	public static final String CLIENT_SECRET = "";

	/** OAuth 2 redirect uri */
	public static final String REDIRECT_URI = "http://localhost";

	/** Latitude API key */
	//public static final String API_KEY = "AIzaSyAA7I099j4zcHZ-lOSmAL46J-vA2-WJvr0";
}
