package com.google.mediapipe.examples.facemesh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DiagnosisActivity extends AppCompatActivity {
    GlobalVariable gv = GlobalVariable.getInstance();

    String uniqueID = UUID.randomUUID().toString();
//    String uniqueID = "123456789";

    RecyclerView mRecyclerView;
    MessageListAdapter mMessageAdapter;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    EditText text_message;
    DateFormat df_time = new SimpleDateFormat("h:mm a");
    DateFormat df_date = new SimpleDateFormat("MMM d", Locale.ENGLISH);
    String time, date;

    ArrayList<String> arrayName = new ArrayList<String>();

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        // initial recycler view adapter
        mRecyclerView = findViewById(R.id.recycler_gchat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 0));

        mMessageAdapter = new MessageListAdapter(arrayList);
        mRecyclerView.setAdapter(mMessageAdapter);

        Button btn_send_message = (Button) findViewById(R.id.button_gchat_send);
        Button btn_show_acupoint = (Button) findViewById(R.id.btn_show_acp);
        Button btn_stop_diagnosis = (Button) findViewById(R.id.btn_stop_diag);

        // load acupoint database
        JSONArray jsonArray = gv.getAcuJson();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                arrayName.add(object.getString("穴道"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // acupoint list
        List<String> acu_list = new ArrayList<>();

        // send initial message to server
        okHttpClient = new OkHttpClient();
        RequestBody formbody_init
                = new FormBody.Builder()
                .add("uid", uniqueID)
                .add("user_name", "老兄")
                .build();
        Request request_init = new Request.Builder().url("http://benson.myftp.org:3001/init/")
                .post(formbody_init)
                .build();
        okHttpClient.newCall(request_init).enqueue(new Callback() {
            @Override
            public void onFailure(
                    @NotNull Call call,
                    @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("response", response.toString());
                            String jsonData = response.body().string();
                            JSONObject object = new JSONObject(jsonData);
                            String uid = object.getString("uid");
                            String question = object.getString("question");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("user", "server");
                            hashMap.put("message", question);
                            time = df_time.format(Calendar.getInstance().getTime());
                            hashMap.put("time", time);
                            arrayList.add(hashMap);
                            mMessageAdapter.notifyItemChanged(arrayList.size());

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

        // send message button
        text_message = findViewById(R.id.edit_gchat_message);
        btn_send_message.setOnClickListener((v)->{
            if (text_message.getText().toString().trim().length() == 0){
                Toast.makeText(this, "No input words", Toast.LENGTH_SHORT).show();
                return;
            }

            acu_list.clear();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user", "me");
            hashMap.put("message", text_message.getText().toString());
            time = df_time.format(Calendar.getInstance().getTime());
            hashMap.put("time", time);
            date = df_date.format(Calendar.getInstance().getTime());
            hashMap.put("date", date);

            arrayList.add(hashMap);


//            mMessageAdapter.notifyDataSetChanged();
            mMessageAdapter.notifyItemChanged(arrayList.size());

            okHttpClient = new OkHttpClient();
            RequestBody formbody
                    = new FormBody.Builder()
                    .add("uid", uniqueID)
                    .add("answer", text_message.getText().toString())
                    .build();
            Request request = new Request.Builder().url("http://benson.myftp.org:3001/ask/")
                    .post(formbody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(
                        @NotNull Call call,
                        @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String jsonData = response.body().string();
                                JSONObject object = new JSONObject(jsonData);
                                String uid = object.getString("uid");
                                String response_json = object.getString("response");

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("user", "server");
                                hashMap.put("message", response_json);
                                time = df_time.format(Calendar.getInstance().getTime());
                                hashMap.put("time", time);
                                arrayList.add(hashMap);
                                mMessageAdapter.notifyItemChanged(arrayList.size());

                                JSONArray acu_json = object.getJSONArray("acu_points");
                                for (int i=0; i<acu_json.length(); i++) {
                                    acu_list.add(acu_json.getString(i));
                                }
                                Log.i("List size", String.valueOf(acu_list.size()));
                                String cont = object.getString("continue");
                                Log.i("Continue", cont);
                                if (cont.equals("false") && acu_list.size()!=0) {
                                    /*Toast.makeText(getApplicationContext(), acu_list.get(0), Toast.LENGTH_SHORT).show();
                                    GlobalVariable gv = GlobalVariable.getInstance();
                                    gv.setAcupoint((String[]) acu_list.toArray(new String[acu_list.size()]));
                                    Intent intent = new Intent();
                                    intent.setClass(DiagnosisActivity.this, CameraActivity.class);
                                    startActivity(intent);*/

                                    btn_stop_diagnosis.setVisibility(View.INVISIBLE);
                                    btn_show_acupoint.setVisibility(View.VISIBLE);
                                }else{
                                    btn_stop_diagnosis.setVisibility(View.VISIBLE);
                                    btn_show_acupoint.setVisibility(View.INVISIBLE);
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            });

            text_message.setText("");

        });

        // show acupoint button
        btn_show_acupoint.setOnClickListener((v -> {
            Toast.makeText(getApplicationContext(), Arrays.toString(acu_list.toArray()), Toast.LENGTH_SHORT).show();
            GlobalVariable gv = GlobalVariable.getInstance();
            gv.setAcupoint((String[]) acu_list.toArray(new String[acu_list.size()]));
            int[] idx = new int[acu_list.size()];
            for (int i=0; i<acu_list.size(); i++) {
                idx[i] = arrayName.indexOf(acu_list.get(i));
            }
            gv.setAcuIdx(idx);
            Intent intent = new Intent();
            intent.setClass(DiagnosisActivity.this, CameraActivity.class);
            startActivity(intent);
            btn_stop_diagnosis.setVisibility(View.INVISIBLE);
        }));

        // stop diagnosis button
        btn_stop_diagnosis.setOnClickListener(v -> {
            text_message.setText("沒有症狀了");
            btn_send_message.performClick();
            closeKeyboard();
        });

    }

    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}