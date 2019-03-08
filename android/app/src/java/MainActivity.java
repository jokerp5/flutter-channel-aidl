package com.cookilog.flutterapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

import com.example.myservice.aidl.IMyAidlInterface;

public class MainActivity extends FlutterActivity {

  private IMyAidlInterface myAidlInterface;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new MethodChannel(getFlutterView(), "com.example.myservice/plugin").setMethodCallHandler(new MethodChannel.MethodCallHandler() {
        @Override
        public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
            Intent intent = new Intent("com.example.myservice.MyService");
            intent.setPackage("com.example.myservice");
            bindService(intent, connection, BIND_AUTO_CREATE);
            String message = "";
            try {
                message = myAidlInterface.echo();
            } catch (Exception e) {

            }
            System.out.println("flutter: "+message);
            result.success(message);
        }
    });

    GeneratedPluginRegistrant.registerWith(this);
  }

  private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      myAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
  };
}
