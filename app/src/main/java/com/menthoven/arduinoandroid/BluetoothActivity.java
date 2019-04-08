package com.menthoven.arduinoandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Handler;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Switch;
import android.os.Message;
import android.app.AlertDialog;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnCheckedChanged;

public class BluetoothActivity extends AppCompatActivity {

    BluetoothService bluetoothService;
    BluetoothDevice device;
    Button sendButton;
    MenuItem reconnectButton;
    Snackbar snackTurnOn;

    //Statischer String da es immer nur eine Konfigurations Nachricht geben kann
    //Außerdem kann man von überall in der App auf diese Nachricht zugreifen
    static String message;

    //Ein Handler der Reagiert wenn er über die Bluetooth Verbindung eine Nachricht empfängt
    private static class myHandler extends Handler {
        private final WeakReference<BluetoothActivity> mActivity;

        public myHandler(BluetoothActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BluetoothActivity activity = mActivity.get();
            switch (msg.what) {
                case Constants.MESSAGE_READ:

                    String readMessage = (String) msg.obj;
                    //Weise die empfangende Nachricht unserer statischen Variable zu.
                    //In diesem Fall ist das möglich da von dem Esp nur diese Nachricht kommt, ansonsten ist die Kommunikation einseitig von App->Esp
                    message = readMessage;
                    break;
            }
        }
    }

    public void setConfig() {
        //Teile message in zwei Substrings auf
        //Der 1. gibt den Slot an und den Sollwert
        //Der 2. ob dieser Slot dis/enabled werden soll
        String config = message.substring(0, 5);
        String cngA = message.substring(5, 6);
        Switch s = (Switch) findViewById(R.id.switch1);
        TextView t = (TextView) findViewById(R.id.textView10);

        //Setze die Einstellungen zu dis/enable
        if (cngA == "d") {
            s.setChecked(false);
        }
        if (cngA == "e") {
            s.setChecked(true);
        }
        switch (cngA) {
            case "d":
                s.setChecked(false);
                break;
            case "e":
                s.setChecked(true);
                break;
        }

        //Setze die Einstellungen zum Sollwert
        switch (config) {
            case "s0=10":
                //Setze Text
                t.setText("Kaktus");
                //Setze Icon
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i9, 0);
                t.setGravity(Gravity.CENTER);

                break;
            case "s0=50":
                t.setText("Blume");
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i6, 0);
                t.setGravity(Gravity.CENTER);

                break;
            case "s0=35":

                t.setText("Palme");
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i2, 0);
                t.setGravity(Gravity.CENTER);

                break;
            case "s0=65":
                t.setText("Bonsei");
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i4, 0);
                t.setGravity(Gravity.CENTER);

                break;
            case "s0=70":


                t.setText("Bambus");
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i5, 0);
                t.setGravity(Gravity.CENTER);

                break;
            case "s0=60":
                t.setText("Farne");
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i8, 0);
                t.setGravity(Gravity.CENTER);

                break;
            case "s0=30":


                t.setText("Strauch");
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i1, 0);
                t.setGravity(Gravity.CENTER);

                break;
            case "s0=75":


                t.setText("Gras");
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i7, 0);
                t.setGravity(Gravity.CENTER);

                break;
            case "s0=55":


                t.setText("Kräuter");
                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i3, 0);
                t.setGravity(Gravity.CENTER);

                break;

            default:
                break;
        }
    }

    //Schicke eine Nachricht zum ESP die dazu führt einen Slot zu gießen
    @OnClick(R.id.iconGiessen)
    void giessen() {
        //"p0e" ist der Befehl zum bewässern
        if (sendMessage("p0e") == true) {
            //falls die Nachricht abgeschickt werden konnte zeige dem User eine Nachricht
            Toast.makeText(getApplicationContext(), "Slot wurde manuell bewässert", Toast.LENGTH_LONG).show();
        } else {
            //falls die Nachricht nicht abgeschickt werden zeige Fehlermeldung
            Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.iconConfig)
    void configure() {
        Intent intent = new Intent(BluetoothActivity.this, Auswahl.class);
        startActivityForResult(intent, 2);// Activity is started with requestCode
    }

    //Wird aufgerufen falls der Switch betätigt wird
    @OnCheckedChanged(R.id.switch1)
    void disEnable(Switch s) {
        //falls er nach dem betätigen true ist:
        if (s.isChecked() == true) {
            //Sende den Befehl "e0" an den ESP, diese Befehl aktiviert die Pumpe
            if (sendMessage("e0") == true) {
                //falls die Nachricht abgeschickt werden konnte zeige dem User eine Nachricht
                Toast.makeText(getApplicationContext(), "Slot wurde aktiviert", Toast.LENGTH_LONG).show();
            } else {
                //falls die Nachricht nicht abgeschickt werden zeige Fehlermeldung
                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
            }
        }
        //Sende den Befehl "d0" an den ESP, diese Befehl deaktiviert die Pumpe
        else {
            if (sendMessage("d0") == true) {
                //falls die Nachricht abgeschickt werden konnte zeige dem User eine Nachricht
                Toast.makeText(getApplicationContext(), "Slot wurde deaktiviert", Toast.LENGTH_LONG).show();
            } else {
                //falls die Nachricht nicht abgeschickt werden zeige Fehlermeldung
                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
            }
        }
    }

    //CallBack Funktion um eine Nachricht von einer anderen Activity zu empfangen
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Warte 500 Millisekunden da die Bluetooth Verbindung etwas Zeit brauch um wieder aufgebaut zu werden
        //Wenn man nicht warten würde, würde man die Nachricht aus dem Callback versuchen an den Esp zu schicken bevor die Bluetooth verbindung wieder da ist
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // überprüfe ob der resultCode ==2
                if (requestCode == 2) {
                    TextView t = (TextView) findViewById(R.id.textView10);
                    //Überprüfe die Nachricht
                    switch (data.getStringExtra("MESSAGE")) {
                        case "Kaktus":
                            //Send den Befehl "s=10" an den ESP
                            //Damit der Sollwert 10 eingestellt wird
                            if (sendMessage("s=10") == true) {
                                //Falls die Nachricht verschickt werden konnte:
                                //Zeige dem User eine Nachricht an
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                //Setzte den Text
                                t.setText(data.getStringExtra("MESSAGE"));
                                //Setze das Icon
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i9, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                //falls die Nachricht nicht abgeschickt werden zeige Fehlermeldung
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Blume":
                            if (sendMessage("s=50") == true) {
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                t.setText(data.getStringExtra("MESSAGE"));

                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i6, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Palme":
                            if (sendMessage("s=35") == true) {
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                t.setText(data.getStringExtra("MESSAGE"));

                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i2, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Bonsei":
                            if (sendMessage("s=65") == true) {
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                t.setText(data.getStringExtra("MESSAGE"));
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i4, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Bambus":
                            if (sendMessage("s=70") == true) {
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                t.setText(data.getStringExtra("MESSAGE"));
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i5, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Farne":
                            if (sendMessage("s=60") == true) {
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                t.setText(data.getStringExtra("MESSAGE"));
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i8, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Strauch":
                            if (sendMessage("s=30") == true) {
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                t.setText(data.getStringExtra("MESSAGE"));
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i1, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Gras":
                            if (sendMessage("s=75") == true) {
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                t.setText(data.getStringExtra("MESSAGE"));
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i7, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Kräuter":
                            if (sendMessage("s=55") == true) {
                                Toast.makeText(getApplicationContext(), data.getStringExtra("MESSAGE") + " wurde ausgewählt", Toast.LENGTH_LONG).show();
                                t.setText(data.getStringExtra("MESSAGE"));
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.i3, 0);
                                t.setGravity(Gravity.CENTER);
                            } else {
                                Toast.makeText(getApplicationContext(), "Verbindungsfehler - Bitte versuchen sie es erneut", Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }, 500);//time in milisecond
    }

    //Wird bei dem erstellen dieser Activity aufgerufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        assert getSupportActionBar() != null;
        myHandler handler = new myHandler(BluetoothActivity.this);
        device = getIntent().getExtras().getParcelable(Constants.EXTRA_DEVICE);
        bluetoothService = new BluetoothService(handler, device);
        setTitle(device.getName());
        //Erstelle einen (Warte) AlertDialog der signalisiert das momentan Daten empfangen werden/ auf Daten gewartet werden
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Empfange Daten von Smart Watering, bitte warten");
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //Warte 3 Sekunden um sicher zu stellen das die Bluetooth Verbindung aufgebaut ist
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Sende ein "?" an den Esp
                if (sendMessage("?") == true) {
                    //Falls die Nachricht am Esp angekommen ist
                    // Warte 2 Sekunden um sicher zu stellen das Daten empfangen wurden
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Schließe den Warte Dialog
                            alertDialog.cancel();
                            //Zeige eine Nachricht an damit der User weiß das Daten empfangen wurden
                            Toast.makeText(getApplicationContext(), "Daten erfolgreich empfangen", Toast.LENGTH_LONG).show();
                            //Setze die Empfangene Konfiguration
                            setConfig();

                        }
                    }, 2000);//time in milisecond
                } else {
                    Toast.makeText(getApplicationContext(), "Keine Daten empfangen, überprüfen sie die Verbindung", Toast.LENGTH_LONG).show();
                    alertDialog.cancel();
                }
            }
        }, 3000);//time in milisecond
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        bluetoothService.connect();
        Log.d(Constants.TAG, "Connecting");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bluetoothService != null) {
            bluetoothService.stop();
            Log.d(Constants.TAG, "Stopping");
        }
        unregisterReceiver(mReceiver);
    }

    //Schicke eine Nachricht über die aufgebaute Verbindung ab
    //Rückgabewert signalisiert das die Nachricht verschickt werden konnte
    public boolean sendMessage(String message) {
        // Überprüfe die Verbindung
        if (bluetoothService.getState() != Constants.STATE_CONNECTED) {
            //Es konnte keine Verbindung hergestellt werden
            return false;
        } else {
            //Falls eine Verbindung hergestellt werden konnte, schicke die übergebene Nachricht über die aufgebaute Verbindung ab
            byte[] send = message.getBytes();
            bluetoothService.write(send);
            return true;
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        snackTurnOn.show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        if (snackTurnOn.isShownOrQueued()) snackTurnOn.dismiss();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        reconnect();
                }
            }
        }
    };

    private void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
    }

    private void reconnect() {
        reconnectButton.setVisible(false);
        bluetoothService.stop();
        bluetoothService.connect();
    }

}
