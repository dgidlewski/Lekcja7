package com.example.darek.lekcja5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private String[] tele;
    private Class[] activities={Tele1Activity.class, Tele2Activity.class, Tele3Activity.class, Tele4Activity.class, Tele5Activity.class};

    static final private int ALERT_DIALOG_PLAIN = 1;
    static final private int ALERT_DIALOG_BUTTONS = 2;
    static final private int ALERT_DIALOG_LIST = 3;
    static final private int CUSTOM_ALERT_DIALOG = 4;
    private Button btnNewAlertDialog;
    private Button btnNewAlertDialogButton;
    private Button btnNewAlertDialogList;
    private Button btnNewCustomAlertDialog;

    private MediaPlayer mediaPlayer;

    protected Button btnStartRecording;
    private Button btnStopRecording;
    private Button btnPlayRecordedSound;
    private Button btnStopRecordedSound;

    private MediaRecorder myAudioRecorder;
    private MediaPlayer m;
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.telephones);
        initResources();
        initTelephonesListView();

        btnNewAlertDialog = (Button) findViewById(R.id.btnNewAlertDialog);
        btnNewAlertDialogButton = (Button) findViewById(R.id.btnNewAlertDialogButton);
        btnNewAlertDialogList = (Button) findViewById(R.id.btnNewAlertDialogList);
        btnNewCustomAlertDialog = (Button) findViewById(R.id.btnNewCustomAlertDialog);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }
            }
        });
        initButtonsClick();

        btnStartRecording = (Button) findViewById(R.id.button3);
        btnStopRecording = (Button) findViewById(R.id.button4);
        btnPlayRecordedSound = (Button) findViewById(R.id.button5);
        btnStopRecordedSound = (Button) findViewById(R.id.button6);

        btnStopRecording.setEnabled(false);
        btnPlayRecordedSound.setEnabled(false);
        btnStopRecordedSound.setEnabled(false);

        btnStartRecording.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnStopRecording.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try{
                    stopRecording();
                }catch(RuntimeException stopException){
                    //handle cleanup here
                }
            }
        });

        btnPlayRecordedSound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    playSound();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnStopRecordedSound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopSound();
            }
        });

        ListView lv = (ListView)findViewById(R.id.telephones);  // your listview inside scrollview
        lv.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

    }

    private void setRecordFile(){
        mFileName = Environment.getExternalStorageDirectory().getPath();
        mFileName += "/audiorecordtest.3gp";
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(mFileName);
    }

    private void startRecording() throws IOException {
        if(myAudioRecorder != null) {
           stopRecording();
        }
        myAudioRecorder = new MediaRecorder();
        setRecordFile();
        btnStopRecording.setEnabled(true);
        myAudioRecorder.prepare();
        myAudioRecorder.start();
    }

    private void stopRecording(){
        if( myAudioRecorder != null) {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
            btnPlayRecordedSound.setEnabled(true);
        }
    }

    private void playSound() throws IOException {
        if(m != null) stopSound();  //zabezpieczenie przed ciągłym zapętlaniu dźwięku
        m = new MediaPlayer();
        m.setDataSource(mFileName);
        m.prepare();
        m.start();
        btnStopRecordedSound.setEnabled(true);
    }

    private void stopSound(){
        if(m != null) {
            m.stop();
            m.release();
            m = null;
        }
    }

    private void initResources(){
        Resources res = getResources();
        tele = res.getStringArray(R.array.telephones);
    }

    private void initTelephonesListView(){
        lv.setAdapter(new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1,tele));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id){
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context,activities[pos]);
                startActivity(intent);
            }
        });
    }

    private void initButtonsClick() {
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnNewAlertDialog:
                        showDialog(ALERT_DIALOG_PLAIN);
                        break;
                    case R.id.btnNewAlertDialogButton:
                        showDialog(ALERT_DIALOG_BUTTONS);
                        break;
                    case R.id.btnNewAlertDialogList:
                        showDialog(ALERT_DIALOG_LIST);
                        break;
                    case R.id.btnNewCustomAlertDialog:
                        showDialog(CUSTOM_ALERT_DIALOG);
                        break;
                    default:
                        break;
                }
            }
        };
        btnNewAlertDialog.setOnClickListener(listener);
        btnNewAlertDialogButton.setOnClickListener(listener);
        btnNewAlertDialogList.setOnClickListener(listener);
        btnNewCustomAlertDialog.setOnClickListener(listener);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ALERT_DIALOG_PLAIN:
                return createPlainAlertDialog();
            case ALERT_DIALOG_BUTTONS:
                return createAlertDialogWithButtons();
            case ALERT_DIALOG_LIST:
                return createAlertDialogWithList();
            case CUSTOM_ALERT_DIALOG:
                return createCustomAlertDialog();
            default:
                return null;
        }
    }
    private Dialog createPlainAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Witam w aplikacji");
        dialogBuilder.setMessage("Ta wiadomość jest do Ciebie!");
        return dialogBuilder.create();
    }

    private Dialog createAlertDialogWithButtons() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Wyjście");
        dialogBuilder.setMessage("Czy napewno chcesz wyjść?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Wychodzę");
                MainActivity.this.finish();
            }
        });
        dialogBuilder.setNegativeButton("Nie", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Anulowaleś wyjście");
            }
        });
        return dialogBuilder.create();
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }

    private Dialog createCustomAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layout = getCustomDialogLayout();
        dialogBuilder.setView(layout);
        dialogBuilder.setTitle("HEHEHE");
        return dialogBuilder.create();
    }
    private View getCustomDialogLayout() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        //return inflater.inflate(R.layout.hehe_layout, (ViewGroup)findViewById(R.id.activity_main));
        return inflater.inflate(R.layout.hehe_layout,null);
    }

    private Dialog createAlertDialogWithList() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final String[] options = {"Odwtórz 1 utwór", "Odtwórz 2 utwór","Odtwórz 3 utwór","Odtwórz 4 utwór", "Zastopuj"};
        dialogBuilder.setTitle("Odtwarzacz");
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int position){
                if(position==4){
                    if(mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer = null;
                    }
                }
                if(position==0) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.pila);
                    mediaPlayer.start();
                }
                if(position==1) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.okruszek);
                    mediaPlayer.start();
                }
                if(position==2) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.balagane);
                    mediaPlayer.start();
                }
                if(position==3) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.miller);
                    mediaPlayer.start();
                }
            }
        });
        return dialogBuilder.create();
    }

}
