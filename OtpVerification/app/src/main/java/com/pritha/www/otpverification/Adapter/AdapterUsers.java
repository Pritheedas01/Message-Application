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

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {
      Context context;
      List<ModelUsers>usersList;

    public AdapterUsers(Context context, List<ModelUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item , viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int i) {
         final String userId= usersList.get(i).getId();
        String username=usersList.get(i).getUsername();
        holder.mNameTv.setText(username);

       DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        Query usQuery=reference.orderByChild("id").equalTo(userId);
        usQuery.addValueEventListener(new ValueEventListener() {
           @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String name=""+ds.child("username").getValue();
                    final String pic=""+ds.child("imageUrl").getValue();
                    try{
                        Picasso.get().load(pic).placeholder(R.mipmap.ic_launcher_round).into(holder.profile_image);

                    }catch (Exception e){
                        Picasso.get().load(R.mipmap.ic_launcher_round).into(holder.profile_image);
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =new Intent(context, MessageActivity.class);
                            intent.putExtra("userid",userId);
                            context.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
       TextView mNameTv;
       ImageView profile_image;
        public MyHolder(View itemView){
            super(itemView);

            mNameTv=itemView.findViewById(R.id.nameTv);
            profile_image=itemView.findViewById(R.id.profile_image);
        }
    }
}
