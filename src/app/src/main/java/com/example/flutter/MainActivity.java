package com.example.flutter;

import com.xc.payment.FlutterTransaction;
import com.xc.payment.LoadConfig;
import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.EventChannel;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


public class MainActivity extends FlutterActivity {
  public static MainActivity instance;
  private static final String CHANNEL = "com.example.pushka_donation/payment";
  private static final String CHANNEL_STREAM = "com.example.pushka_donation/card_info";
  private Handler uiThreadHandler = new Handler(Looper.getMainLooper());

  private EventChannel.EventSink eventSink;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    instance = this;
  }

  public void sendDataToFlutter(String data) {
    if (eventSink != null) {
      uiThreadHandler.post(() -> eventSink.success(data));
    }
  }

  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
    super.configureFlutterEngine(flutterEngine);
    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
        .setMethodCallHandler(
            (call, result) -> {
              // This method is invoked on the main thread.
              // This method is invoked on the main thread.
              if (call.method.equals("startTransaction")) {
                FlutterTransaction trans = new FlutterTransaction();
                String data = trans.startTransaction();

                if (!data.startsWith("err")) {
                  result.success(data);
                } else {
                  result.error("Error", data, null);
                }
              } else if(call.method.equals("loadConfig")){
                try{
                  LoadConfig.load();
                  result.success("");
                }catch(Exception e){
                  result.error("Error", e.toString(), null);
                }
                
              } else {
                result.notImplemented();
              }
            });

    new EventChannel(flutterEngine.getDartExecutor(), CHANNEL_STREAM).setStreamHandler(
        new EventChannel.StreamHandler() {
          @Override
          public void onListen(Object args, final EventChannel.EventSink events) {
            System.out.println("Listening");
            MainActivity.this.eventSink = events;
          }

          @Override
          public void onCancel(Object args) {
            System.out.println("Cancelling");
            MainActivity.this.eventSink = null;
          }
        });
  }
}
