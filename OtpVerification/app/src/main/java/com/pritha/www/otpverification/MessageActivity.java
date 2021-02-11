package com.pritha.www.otpverification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pritha.www.otpverification.Adapter.MessageAdapter;
import com.pritha.www.otpverification.Model.Chat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {
    TextView sendername,details;
    FirebaseUser fuser;
    ImageView userpic,round;
    DatabaseReference reference,reference1;
    ImageView send;
    EditText message_send;
    MessageAdapter messageAdapter;
    List<Chat> mChats;
    RecyclerView recyclerView;
    String telNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sendername=findViewById(R.id.Susername);
        details=findViewById(R.id.detail);
        userpic=findViewById(R.id.userpic);
        send=findViewById(R.id.btn_send);
        message_send=findViewById(R.id.text_send);
        round=findViewById(R.id.round);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView=findViewById(R.id.recycler_view_message);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent =getIntent();
        final String userid=intent.getStringExtra("userid");


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=message_send.getText().toString();
                if(!msg.equals("")){
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    sendMessage(fuser.getUid(),userid,msg,timestamp);
                }else{
                    Toast.makeText(MessageActivity.this,"you can't send empty message",Toast.LENGTH_SHORT).show();
                }
                message_send.setText("");
            }
        });

        reference= FirebaseDatabase.getInstance().getReference("Users");
        Query usQuery=reference.orderByChild("id").equalTo(userid);
        final String myid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        usQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String name=""+ds.child("username").getValue();
                    String status=""+ds.child("status").getValue();
                    String pic=""+ds.child("imageUrl").getValue();
                    String typingStatus=""+ds.child("typingto").getValue();
                    telNum=""+ds.child("phone").getValue();

                    if (typingStatus.equals(myid)){
                        details.setText("typing....");
                    }
                    else{
                        if(status.equals("online")){
                            details.setText(status);
                        }
                        else{
                            //details.setText("");
                            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(Long.parseLong(status));
                            String dateTime= DateFormat.format("dd/MM/yyyy  hh:mm:aa",cal).toString();
                            details.setText("Last seen at: "+dateTime);
                        }
                    }
                    try{
                        Picasso.get().load(pic).placeholder(R.mipmap.ic_launcher_round).into(userpic);

                    }catch (Exception e){
                        Picasso.get().load(R.mipmap.ic_launcher_round).into(userpic);
                    }
                    sendername.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference1= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage(fuser.getUid(),userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        message_send.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length()==0){
                    typing("noOne");
                }else {
                    typing(userid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                //telNum = sendername.getText().toString();
                if (telNum.trim().isEmpty()) {
                    intentCall.setData(Uri.parse("tel:567788"));
                    //Toast.makeText(getApplicationContext(),"Please Enter Num",Toast.LENGTH_SHORT).show();
                } else {
                    intentCall.setData(Uri.parse("tel:" + telNum));
                }
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Please grant permission", Toast.LENGTH_SHORT).show();
                    requestPermission();
                } else {
                    startActivity(intentCall);
                }
                final DatabaseReference chatref1=FirebaseDatabase.getInstance().getReference("calllist").child(myid).child(userid);
                chatref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            chatref1.child("id").setValue(userid);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final DatabaseReference chatref2=FirebaseDatabase.getInstance().getReference("calllist").child(userid).child(myid);
                chatref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            chatref2.child("id").setValue(myid);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(MessageActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

    private void  sendMessage(String sender, String recevier, String message,String timestamp){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("recevier",recevier);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        reference.child("chats").push().setValue(hashMap);
    }
    private void readMessage(final String myid, final String userid){
        mChats=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChats.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat =snapshot.getValue(Chat.class);
                    if(chat.getRecevier().equals(myid) && chat.getSender().equals(userid) ||
                        chat.getRecevier().equals(userid) && chat.getSender().equals(myid)){
                        mChats.add(chat);
                    }
                    messageAdapter =  new MessageAdapter(MessageActivity.this,mChats);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference chatref1=FirebaseDatabase.getInstance().getReference("chatlist").child(myid).child(userid);
        chatref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if(!dataSnapshot.exists()){
                 chatref1.child("id").setValue(userid);
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference chatref2=FirebaseDatabase.getInstance().getReference("chatlist").child(userid).child(myid);
        chatref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatref2.child("id").setValue(myid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void status(String status){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }
    private void typing(String typing){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("typingto",typing);
        reference.updateChildren(hashMap);
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }
    @Override
    protected void onPause() {
        super.onPause();
        String timestamp = String.valueOf(System.currentTimeMillis());
        status(timestamp);
        typing("noOne");
    }

    @Override
    protected void onStart() {
        super.onStart();
        status("online");
    }

}
