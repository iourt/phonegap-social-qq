package com.tangram.oauth;

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

public class oauth extends CordovaPlugin {
	private static final String ACTION_SAY_HELLO = "say-hello";	
	private static final String ACTION_QQ_LOGIN  = "qq-login";	
	private static final String ACTION_QQ_SHARED = "qq-shared";	

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
		else if(action.equals("qq-shared")) {
			this.qqShared();
		}
		else if(action.equals("get-userinfo")) {
			//this.getUserInfo(args);
		}
		return true;
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
		//Context context = this.cordova.getActivity().getApplicationContext();
		//m_tHandle = Tencent.createInstance(APPID, context);	
		//JSONObject jsonObj = args.getJSONObject(0);
		//String uid = jsonObj.getString("uid"); 
		//String token = jsonObj.getString("token"); 
		//m_tHandle.setOpenId(uid);
		//m_tHandle.setAccessToken(token);

		final IUiListener listener2 = new BaseUiListener() {
			protected void doComplete(JSONObject values) {
				m_cbCtx.success(values);
				//m_cbCtx.success("hello owrld!");
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
				
				//m_cbCtx.success("hello");
				getUserInfo();				
			//	new Thread() {
			//		public void run() {
			//			JSONObject res = new JSONObject();
			//			res = m_tHandle.request(Constants.GRAPH_SIMPLE_USER_INFO, null, Constants.HTTP_GET);
			//			m_cbCtx.success(res);
			//		}
			//	}.start();
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
