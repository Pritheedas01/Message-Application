package com.pritha.www.otpverification;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pritha.www.otpverification.Adapter.AdapterChatlist;
import com.pritha.www.otpverification.Model.Chat;
import com.pritha.www.otpverification.Model.Chatlist;
import com.pritha.www.otpverification.Model.ModelUsers;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    FirebaseUser fuser;
    DatabaseReference reference;
    private List<Chatlist> chatlistList;
    private List<ModelUsers> userList;
    private AdapterChatlist adapterChatlist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.recycler_view_chat);
        chatlistList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chatlist").child(fuser.getUid());
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


        return view;
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
                adapterChatlist = new AdapterChatlist(getContext(), userList);
                recyclerView.setAdapter(adapterChatlist);
                for (int i = 0; i < userList.size(); i++) {
                    lastMessage(userList.get(i).getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void lastMessage(final String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theLast = "default";
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if (chat == null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String recevier = chat.getRecevier();
                    if (sender == null || recevier == null) {
                        continue;
                    }
                    if (chat.getRecevier().equals(fuser.getUid()) && chat.getSender().equals(userid) || chat.getRecevier().equals(userid) && chat.getSender().equals(fuser.getUid())) {
                        theLast = chat.getMessage();
                    }
                }
                adapterChatlist.setLastmessageMap(userid, theLast);
                adapterChatlist.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


