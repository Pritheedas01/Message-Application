package com.pritha.www.otpverification.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pritha.www.otpverification.MessageActivity;
import com.pritha.www.otpverification.Model.ModelUsers;
import com.pritha.www.otpverification.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder>{
    Context context;
    List<ModelUsers> usersList;
    private HashMap<String, String> lastmessageMap;

    public AdapterChatlist(Context context, List<ModelUsers> userList) {
        this.context=context;
        this.usersList=userList;
        lastmessageMap = new HashMap<>();
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_chatlist,parent,false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        final ModelUsers user=usersList.get(position);
        final String userid=usersList.get(position).getId();
        String username=usersList.get(position).getUsername();
        String userImage=usersList.get(position).getImage();
        String lastmessage1=lastmessageMap.get(userid);
        String online=usersList.get(position).getOnlinestatus();

        holder.name.setText(username);


       /* if(lastmessage1!=null){

            holder.lastmessage.setVisibility(View.GONE);
        }else{
            holder.lastmessage.setVisibility(View.VISIBLE);
            holder.lastmessage.setText(lastmessage1);
        }*/

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        Query query=reference.orderByChild("id").equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String pic =""+ds.child("imageUrl").getValue();
                    try{
                        Picasso.get().load(pic).placeholder(R.mipmap.ic_launcher_round).into(holder.profilepic);
                    }
                    catch (Exception e){
                        Picasso.get().load(R.mipmap.ic_launcher_round).into(holder.profilepic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, MessageActivity.class);
                intent.putExtra("userid",userid);
                context.startActivity(intent);
            }
        });

    }

    public void setLastmessageMap(String userid, String lastmessage){
        lastmessageMap.put(userid,lastmessage);
    }
    @Override
    public int getItemCount() {
        return usersList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView profilepic,onlinestatus;
        TextView name,lastmessage;
        public MyHolder(@NonNull View itemView){
            super((itemView));

            profilepic=itemView.findViewById(R.id.profilepic);
            onlinestatus=itemView.findViewById(R.id.onlinestatus);
            name=itemView.findViewById(R.id.name);

        }
    }
}