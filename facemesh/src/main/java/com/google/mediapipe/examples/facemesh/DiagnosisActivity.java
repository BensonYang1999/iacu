package com.google.mediapipe.examples.facemesh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;



public class DiagnosisActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    CustomAdapter myListAdapter;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    EditText text_message;
    DateFormat df_time = new SimpleDateFormat("h:mm a");
    DateFormat df_date = new SimpleDateFormat("MMM d", Locale.ENGLISH);
    String time, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        //製造資料
        for (int i = 0;i<0;i++){
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user", "me");
            hashMap.put("message", "first message");
            time = df_time.format(Calendar.getInstance().getTime());
            hashMap.put("time", time);
            arrayList.add(hashMap);
        }

        /*for (int i = 0;i<30;i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("Id","座號："+String.format("%02d",i+1));
            hashMap.put("Sub1",String.valueOf(new Random().nextInt(80) + 20));
            hashMap.put("Sub2",String.valueOf(new Random().nextInt(80) + 20));
            hashMap.put("Avg",String.valueOf(
                    (Integer.parseInt(hashMap.get("Sub1"))
                            +Integer.parseInt(hashMap.get("Sub2")))/2));

            arrayList.add(hashMap);
        }*/

        mRecyclerView = findViewById(R.id.recycler_gchat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        myListAdapter = new CustomAdapter();
        mRecyclerView.setAdapter(myListAdapter);
        text_message = findViewById(R.id.edit_gchat_message);


        // Button
        Button btn_send_message = (Button) findViewById(R.id.button_gchat_send);
        btn_send_message.setOnClickListener((v)->{
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user", "me");
            hashMap.put("message", text_message.getText().toString());
            time = df_time.format(Calendar.getInstance().getTime());
            hashMap.put("time", time);
            date = df_date.format(Calendar.getInstance().getTime());
            hashMap.put("date", date);

            arrayList.add(hashMap);

            text_message.setText("");
            myListAdapter.notifyDataSetChanged();
        });
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView MyMessage, SendTime, SendDate;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                MyMessage = itemView.findViewById(R.id.text_gchat_message_me);
                SendTime = itemView.findViewById(R.id.text_gchat_timestamp_me);
                SendDate = itemView.findViewById(R.id.text_gchat_date_me);
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
            holder.SendDate.setText(arrayList.get(position).get("date"));
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
    }


}