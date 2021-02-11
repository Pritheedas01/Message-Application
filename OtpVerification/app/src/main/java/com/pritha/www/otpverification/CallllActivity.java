package com.pritha.www.otpverification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pritha.www.otpverification.Adapter.AdapterChatlist;
import com.pritha.www.otpverification.Model.Chatlist;
import com.pritha.www.otpverification.Model.ModelUsers;

import java.util.ArrayList;
import java.util.List;

public class CallllActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    FirebaseUser fuser;
    DatabaseReference reference;
    private List<Chatlist> chatlistList;
    private List<ModelUsers> userList;
    private AdapterChatlist adapterChatlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callll);
        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        chatlistList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reference = FirebaseDatabase.getInstance().getReference("calllist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlistList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chatlist chatlist = ds.getValue(Chatlist.class);
                    chatlistList.add(chatlist);
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadChats() {
        userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUsers users = ds.getValue(ModelUsers.class);
                    for (Chatlist chatlist : chatlistList) {
                        if (users.getId() != null && users.getId().equals(chatlist.getId())) {
                            userList.add(users);
                            break;
                        }
                    }
                }
                adapterChatlist = new AdapterChatlist(CallllActivity.this, userList);
                recyclerView.setAdapter(adapterChatlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
