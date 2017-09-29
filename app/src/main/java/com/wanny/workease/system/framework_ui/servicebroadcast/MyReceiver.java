package com.wanny.workease.system.framework_ui.servicebroadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;


import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.LogUtil;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_ui.customer_UI.activity.HomeManagerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			LogUtil.log(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				LogUtil.log(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				PreferenceUtil.getInstance(context).saveString("channId",regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				LogUtil.log(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				setCustomerNitic(context);
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//				setCustomerNitic(context);
				LogUtil.log(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				LogUtil.log(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
				processCustomMessage(context , bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				LogUtil.log(TAG, "[MyReceiver] 用户点击打开了通知");
				//打开自定义的Activity
				Intent i = new Intent(context, HomeManagerActivity.class);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);
//				operateNotication(context ,bundle);
			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				LogUtil.log(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				LogUtil.log(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				LogUtil.log(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

//
//	private void setCustomerNitic(Context context){
//		CustomPushNotificationBuilder builder = new
//                CustomPushNotificationBuilder(context,
//				R.layout.customer_notitfication_layout,
//				R.id.icon,
//				R.id.title,
//				R.id.text);
//		// 指定定制的 Notification Layout
//		builder.statusBarDrawable = R.mipmap.ic_launcher;
//		// 指定最顶层状态栏小图标
//		builder.layoutIconDrawable = R.mipmap.icon_jpush_state;
//		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
//				| Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
//		builder.notificationDefaults = Notification.DEFAULT_SOUND
//				| Notification.DEFAULT_VIBRATE
//				| Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
//		// 指定下拉状态栏时显示的通知图标
//		// 指定下拉状态栏时显示的通知图标
//		JPushInterface.setPushNotificationBuilder(2, builder);
//	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					LogUtil.log(TAG, "This message has no Extra data");
					continue;
				}
				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();
					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					LogUtil.log(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	//send msg to MainActivity
	private void operateNotication(Context context, Bundle bundle) {
		if (bundle != null) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			if (extras != null) {
				if (!TextUtils.isEmpty(extras)) {
					try {
						JSONObject extraJson = new JSONObject(extras);
						String type = "";
						String transNo = "";
						String PicPath = "";
						String giftsid = "";
						String GiftName = "";
						if (extraJson.length() > 0) {
							if (extraJson.has("type")) {
								type = extraJson.getString("type");
							}else{
								type = "";
							}
							if (extraJson.has("transNo")) {
								transNo = extraJson.getString("transNo");
							}
							if (extraJson.has("PicPath")) {
								PicPath = extraJson.getString("PicPath");
							}
							if (extraJson.has("giftsid")) {
								giftsid = extraJson.getString("giftsid");
							}
							if (extraJson.has("GiftName")) {
								GiftName = extraJson.getString("GiftName");
							}
//							if(type.equals("gift")){
//								Intent intent = new Intent(context,Camera2GiftActivity.class);
//								intent.putExtra("tranOn",transNo);
//								intent.putExtra("filePath", ApiStores.PATH_HEADER + PicPath);
//								intent.putExtra("mode", Camera2GiftActivity.MODE_RECEIVE);
//								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								context.startActivity(intent);
//							}else if(type.equals("verify")){
//								Intent intent = new Intent(context, SendGiftActivity.class);
//								GiftInfoEntity giftInfoEntity = new GiftInfoEntity();
//								giftInfoEntity.setId(giftsid);
//								giftInfoEntity.setThumbnail(ApiStores.PATH_HEADER + PicPath);
//								giftInfoEntity.setTitle(GiftName);
//								intent.putExtra("gift",giftInfoEntity);
//								intent.putExtra("mode",SendGiftActivity.MODE_UPLOAD);
//								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								context.startActivity(intent);
//							}
//							if (type == 1 || type == 5 || type == 6) {
//								if (YiPingHomeActivity.isForeground) {
//									Intent msgIntent = new Intent(YiPingHomeActivity.MESSAGE_RECEIVED_ACTION);
//									msgIntent.putExtra(YiPingHomeActivity.KEY_MESSAGE, message);
//									msgIntent.putExtra(YiPingHomeActivity.KEY_EXTRAS, type);
//									context.sendBroadcast(msgIntent);
//								}else{
//									Intent intent = new Intent(context, YiPingHomeActivity.class);
//									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//									intent.putExtra(YiPingHomeActivity.KEY_EXTRAS,type);
//									context.startActivity(intent);
//								}
//							} else if (type == 3) {
//								Intent intent = new Intent(context,CallBackActivity.class);
//								intent.putExtra("projectId",projectId);
//								intent.putExtra("objectId",objectid);
//								intent.putExtra("type",CallBackActivity.MODE_SHENHE);
//								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								context.startActivity(intent);
//							} else if (type == 4) {
//								Intent intent = new Intent(context,CallPriceDetailActivity.class);
//								intent.putExtra("projectId",projectId);
//								intent.putExtra("objectId",objectid);
//								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								context.startActivity(intent);
//							} else if(type == 2) {
//								Intent intent = new Intent(context, ProjectDetailActivity.class);
//								intent.putExtra("projectId",projectId);
//								intent.putExtra("mode",0);
//								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								context.startActivity(intent);
//							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

    //操作信息
	private void processCustomMessage(Context context , Bundle bundle){
//		if (YiPingHomeActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(YiPingHomeActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(YiPingHomeActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(YiPingHomeActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//				}
//			}
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//		}
	}
}
