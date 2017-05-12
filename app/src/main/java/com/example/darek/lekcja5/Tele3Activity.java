package com.example.darek.lekcja5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Darek on 2017-04-06.
 */

public class Tele3Activity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele);

        ImageView imView = (ImageView)findViewById(R.id.imageView1);
        imView.setImageResource(R.drawable.huawei3);

        lv = (ListView) findViewById(R.id.specifications);
        initResources();
        initSpecificationListView();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private ListView lv;
    private String[] spec;

    private void initResources(){
        Resources res = getResources();
        spec = res.getStringArray(R.array.specification3);
    }

    private void initSpecificationListView(){
        lv.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.customlayout, spec));
    }
}