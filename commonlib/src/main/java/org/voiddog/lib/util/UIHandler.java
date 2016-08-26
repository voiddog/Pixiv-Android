package org.voiddog.lib.util;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

public class UIHandler {

	public static void sendEmptyMessage(int what, Callback callback) {
		Handler handler = new Handler(Looper.getMainLooper(), callback);
		handler.sendEmptyMessage(what);
	}

	public static void sendMessage(int what, Object obj, Callback callback) {
		Handler handler = new Handler(Looper.getMainLooper(), callback);
		Message message = handler.obtainMessage(what, obj);
		message.sendToTarget();
	}

	public static void sendEmptyMessageDelayed(int what, long delay,
											   Callback callback) {
		Handler handler = new Handler(Looper.getMainLooper(), callback);
		handler.sendEmptyMessageDelayed(what, delay);
	}
}
