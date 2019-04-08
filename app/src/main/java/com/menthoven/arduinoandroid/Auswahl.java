package com.menthoven.arduinoandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.MailTo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Handler;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Auswahl extends AppCompatActivity {
    //Wird ausgelöst beim Klick auf den Kaktus Button
    @OnClick(R.id.bt1)
    void test() {
        //Die message die zurück an unsere MainActivity(BluetoothActivity) geht
        String message = "Kaktus";
        //es muss ein Intent erstellt werden den wir zwischen den Activitys senden können
        Intent intent = new Intent();
        //füge die message an den Intent an
        intent.putExtra("MESSAGE", message);
        // Rückgabenachricht an die vorherige Activity
        setResult(2, intent);
        //Diese activity schließen
        finish();//finishing activity
    }

    @OnClick(R.id.bt2)
    void test2() {
        String message = "Blume";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        setResult(2, intent);
        finish();//finishing activity
    }

    @OnClick(R.id.bt3)
    void test3() {
        String message = "Palme";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        setResult(2, intent);
        finish();//finishing activity

    }

    @OnClick(R.id.bt4)
    void test4() {
        String message = "Bonsei";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        setResult(2, intent);
        finish();//finishing activity

    }

    @OnClick(R.id.bt5)
    void test5() {
        String message = "Bambus";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        setResult(2, intent);
        finish();//finishing activity

    }

    @OnClick(R.id.bt6)
    void test6() {
        String message = "Farne";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        setResult(2, intent);
        finish();//finishing activity

    }

    @OnClick(R.id.bt7)
    void test7() {
        String message = "Strauch";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        setResult(2, intent);
        finish();//finishing activity

    }

    @OnClick(R.id.bt8)
    void test8() {
        String message = "Gras";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        setResult(2, intent);
        finish();//finishing activity

    }

    @OnClick(R.id.bt9)
    void test9() {
        String message = "Kräuter";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        setResult(2, intent);
        finish();//finishing activity

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auswahl);
        ButterKnife.bind(this);
    }

}
