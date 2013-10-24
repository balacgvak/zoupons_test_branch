package com.us.zoupons.WebService;


import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.content.Context;
import android.util.Log;

import com.us.zoupons.R;

public class MyHttpClient extends DefaultHttpClient {

	final Context context;

	public MyHttpClient(Context context) {
		this.context = context;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		try{
			//Log.i("check", "create client connection");  
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			// Register for port 443 our SSLSocketFactory with our keystore
			// to the ConnectionManager
			registry.register(new Scheme("https", newSslSocketFactory(), 443));
			return new SingleClientConnManager(getParams(), registry);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private SSLSocketFactory newSslSocketFactory() {
		try {
			//Log.i("check", "new ssl scoket");
			// Get an instance of the Bouncy Castle KeyStore format
			KeyStore trusted = KeyStore.getInstance("BKS");
			// Get the raw resource, which contains the keystore with
			// your trusted certificates (root and any intermediate certs)
			InputStream in = context.getResources().openRawResource(R.raw.zouponssignedsslcertificate);
			try {
				// Initialize the keystore with the provided trusted certificates
				// Also provide the password of the keystore
				trusted.load(in, "rouzbeh.aminpour".toCharArray());
			}catch(Exception e){
				e.printStackTrace();
				return null;
			} finally {
				in.close();
			}
			// Pass the keystore to the SSLSocketFactory. The factory is responsible
			// for the verification of the server certificate.
			SSLSocketFactory sf = new SSLSocketFactory(trusted);
			// Hostname verification from certificate
			// http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
			//sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			return sf;
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
