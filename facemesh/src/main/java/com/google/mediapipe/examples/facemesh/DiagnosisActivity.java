package com.google.mediapipe.examples.facemesh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    String uniqueID = UUID.randomUUID().toString();
//    String uniqueID = "123456789";

    RecyclerView mRecyclerView;
//    CustomAdapter myListAdapter;
    MessageListAdapter mMessageAdapter;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    EditText text_message;
    DateFormat df_time = new SimpleDateFormat("h:mm a");
    DateFormat df_date = new SimpleDateFormat("MMM d", Locale.ENGLISH);
    String time, date;

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        //製造資料
        /*for (int i = 0;i<1;i++){
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user", "server");
            hashMap.put("message", "醫生端測試訊息");
            time = df_time.format(Calendar.getInstance().getTime());
            hashMap.put("time", time);
            arrayList.add(hashMap);
        }*/

        // initial recycler view adapter
        mRecyclerView = findViewById(R.id.recycler_gchat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 0));

//        myListAdapter = new CustomAdapter();
//        mRecyclerView.setAdapter(myListAdapter);

        mMessageAdapter = new MessageListAdapter(arrayList);
        mRecyclerView.setAdapter(mMessageAdapter);


        // send initial message to server
        okHttpClient = new OkHttpClient();
        RequestBody formbody_init
                = new FormBody.Builder()
                .add("uid", uniqueID)
                .add("user_name", "貴賓")
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
        Button btn_send_message = (Button) findViewById(R.id.button_gchat_send);
        btn_send_message.setOnClickListener((v)->{
            if (text_message.getText().toString().trim().length() == 0){
                Toast.makeText(this, "No input words", Toast.LENGTH_SHORT).show();
                return;
            }
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
                                List<String> acu_list = new ArrayList<>();
                                for (int i=0; i<acu_json.length(); i++) {
                                    acu_list.add(acu_json.getString(i));
                                }
                                Log.i("List size", String.valueOf(acu_list.size()));
                                String cont = object.getString("continue");
                                Log.i("Continue", cont);
                                if (cont.equals("false") && acu_list.size()!=0) {
                                    Toast.makeText(getApplicationContext(), acu_list.get(0), Toast.LENGTH_SHORT).show();
                                    GlobalVariable gv = GlobalVariable.getInstance();
                                    gv.setAcupoint(acu_list.get(0));
                                    Intent intent = new Intent();
                                    intent.setClass(DiagnosisActivity.this, CameraActivity.class);
                                    startActivity(intent);
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            });

            text_message.setText("");

            /*RequestBody formbody
                    = new FormBody.Builder()
                    .add("uid", uniqueID)
                    .add("user_name", "貴賓")
                    .build();
            Request request = new Request.Builder().url("http://192.168.1.2:3001/init/")
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
                                String jsonData = response.body().string();
                                JSONObject object = new JSONObject(jsonData);
                                String uid = object.getString("UID");
                                String question = object.getString("response");

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

            });*/
        });


    }
    /*private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView MyMessage, SendTime, SendDate;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                MyMessage = itemView.findViewById(R.id.text_gchat_message_me);
                SendTime = itemView.findViewById(R.id.text_gchat_timestamp_me);
//                SendDate = itemView.findViewById(R.id.text_gchat_date_me);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_me, parent, false);
            return new ViewHolder(view);
//            return null;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.MyMessage.setText(arrayList.get(position).get("message"));
            holder.SendTime.setText(arrayList.get(position).get("time"));
//            holder.SendDate.setText(arrayList.get(position).get("date"));
//            holder.MyMessage.setText("test message.");
//            time = df.format(Calendar.getInstance().getTime());
//            holder.SendTime.setText(time);
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            // viewHolder.getTextView().setText(localDataSet[position]);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            //return localDataSet.length;
            return arrayList.size();
        }
    }*/


}