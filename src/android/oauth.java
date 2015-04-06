package com.tangram.oauth;

import java.util.ArrayList;
import java.text.SimpleDateFormat;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.Tencent;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;

public class oauth extends CordovaPlugin {
	private static final String ACTION_SAY_HELLO    = "say-hello";	
	private static final String ACTION_QQ_LOGIN     = "qq-login";	
	private static final String ACTION_SHARED_QZONE = "shared-qzone";	
	private static final String ACTION_SHARED_FRIEND= "shared-friend";	

	public  static final String APPID = "101195786";
	private Tencent  m_tHandle = null; 
	private UserInfo m_userInfo= null; 
	private CallbackContext m_cbCtx = null;

	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		m_cbCtx = callbackContext;
		if(action.equals("say-hello")) {
			this.sayHello();
		}
		else if(action.equals("qq-login")) {
			this.qqLogin();
		}
		else if(action.equals("qq-logout")) {
			this.qqLogout();
		}
		else if(action.equals("shared-friend")) {
			this.sharedToFriend(args);
		}
		else if(action.equals("shared-qzone")) {
			this.sharedQzone(args);
		}
		return true;
	}
	
	private void sharedQzone(JSONArray args) {
		try {
			JSONObject jsonObj = args.getJSONObject(0);
			String title =  jsonObj.getString("title");
			String summary = jsonObj.getString("summary");
			String targetURL = "";
			String imageURL = "";

			if(jsonObj.has("target_url")) {
				targetURL = jsonObj.getString("target_url");
			}
			if(jsonObj.has("image_url")) {
				imageURL = jsonObj.getString("image_url");
			}

			Context context = this.cordova.getActivity().getApplicationContext();
			m_tHandle = Tencent.createInstance(APPID, context);
			
			final Activity activity = this.cordova.getActivity();
			Bundle bundle = new Bundle();
			bundle.clear();
			bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
			bundle.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
			bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
			bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
			if(targetURL != "") {
				bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetURL);
			}
			if(imageURL != "") {
				ArrayList<String> imageUrls = new ArrayList<String>();
				imageUrls.add(imageURL);
				bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
			}
			final IUiListener listener3 = new BaseUiListener() {
				protected void doComplete(JSONObject values) {
					m_cbCtx.success(values);
				}
			};
			m_tHandle.shareToQzone(activity, bundle, listener3); 
		}
		catch(JSONException e) {
			m_cbCtx.success("fail");
		}
	}

	private void sharedToFriend(JSONArray args) {
		try {
			JSONObject jsonObj = args.getJSONObject(0);
			String title =  jsonObj.getString("title");
			String summary = jsonObj.getString("summary");
			String AppName = "";
			String imageURL = "";

			if(jsonObj.has("image_url")) {
				imageURL = jsonObj.getString("image_url");
			}
			if(jsonObj.has("appname")) {
				AppName = jsonObj.getString("appname");
			}

			final Activity activity = this.cordova.getActivity();
			Bundle bundle = new Bundle();
			bundle.clear();
			bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
			bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
			bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
			if(AppName != "") {
				bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, AppName);
			}
			if(imageURL != "") {
				bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageURL);
			}
			final IUiListener listener4 = new BaseUiListener() {
				protected void doComplete(JSONObject values) {
					m_cbCtx.success(values);
				}
			};
			m_tHandle.shareToQQ(activity, bundle, listener4);
		}
		catch(JSONException e) {
			m_cbCtx.success("fail");
		}
	}

	private void sayHello() {
		m_cbCtx.success("ni hao");
	}

	private void qqLogout() {
		Context context = this.cordova.getActivity().getApplicationContext();
		m_tHandle.logout(context);
		m_cbCtx.success();
	}
	
	final private void getUserInfo() {
		final Activity activity = this.cordova.getActivity();
		final IUiListener listener2 = new BaseUiListener() {
			protected void doComplete(JSONObject values) {
				m_cbCtx.success(values);
			}
		};
		m_userInfo = new UserInfo(activity, m_tHandle.getQQToken());
		m_userInfo.getUserInfo(listener2);
	}

	private void qqLogin() {
		final Activity activity = this.cordova.getActivity();
		Context context = this.cordova.getActivity().getApplicationContext();
		m_tHandle = Tencent.createInstance(APPID, context);

		final IUiListener listener = new BaseUiListener() {
			protected void doComplete(JSONObject values) {
				String uid=m_tHandle.getOpenId();
				String token=m_tHandle.getAccessToken();
				
				getUserInfo();				
			}
		};

		this.cordova.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				m_tHandle.login(activity, "all", listener);
			}
		});
	}

	private class BaseUiListener implements IUiListener {
		public void onComplete(Object response) {
			doComplete((JSONObject)response);
			//String uid=m_tHandle.getOpenId();
			//String token=m_tHandle.getAccessToken();

			//JSONObject res = new JSONObject();
			//try {
			//	res.put("uid", uid);
			//	res.put("token", token);
			//	m_cbCtx.success(res);
			//}
			//catch(JSONException e) {
			//	m_cbCtx.error("error");	
			//}
		}

		protected void doComplete(JSONObject values) {
		}

		public void onError(UiError e) {
			m_cbCtx.error("message:"+ e.errorMessage + "code:" + e.errorCode + "detail:" + e.errorDetail);
		}

		public void onCancel() {
			m_cbCtx.error("on cancel");
		}
	}

	private void qqShared() {
		//TODO	
	}
}
