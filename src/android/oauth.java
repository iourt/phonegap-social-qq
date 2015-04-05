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

public class oauth extends CordovaPlugin {
	private static final String ACTION_SAY_HELLO = "say-hello";	
	private static final String ACTION_QQ_LOGIN  = "qq-login";	
	private static final String ACTION_QQ_SHARED = "qq-shared";	

	public  static final String APPID = "101195786";
	private Tencent m_tHandle = null; 
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
		return true;
	}
	
	private void sayHello() {
		m_cbCtx.success("ni hao");
	}

	private void qqLogout() {
		//TODO
	}

	private void qqLogin() {
		final Activity activity = this.cordova.getActivity();
		Context context = this.cordova.getActivity().getApplicationContext();
		m_tHandle = Tencent.createInstance(APPID, context);

		final IUiListener listener = new BaseUiListener() {
			protected void doComplete(JSONObject values) {
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
			String uid=m_tHandle.getOpenId();
			String token=m_tHandle.getAccessToken();

			JSONObject res = new JSONObject();
			try {
				res.put("uid", uid);
				res.put("token", token);
				m_cbCtx.success(res);
			}
			catch(JSONException e) {
				m_cbCtx.error("error");	
			}
		}

		protected void doComplete(JSONObject values) {
		}

		public void onError(UiError e) {
		}

		public void onCancel() {
			m_cbCtx.error("on cancel");
		}
	}

	private void qqShared() {
		//TODO	
	}
}
