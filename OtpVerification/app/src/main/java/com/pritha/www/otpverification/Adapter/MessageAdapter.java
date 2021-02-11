package com.pritha.www.otpverification.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pritha.www.otpverification.Model.Chat;
import com.pritha.www.otpverification.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    Context context;
    List<Chat> chats;
    FirebaseUser fuser;

    public MessageAdapter(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public MessageAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.message_right, viewGroup,false);
            return new MessageAdapter.MyHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.left_message, viewGroup,false);
            return new MessageAdapter.MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyHolder holder, final int i) {
        Chat chat =chats.get(i);
        holder.show.setText(chat.getMessage());
        final String userid=chats.get(i).getRecevier();
        String timestamp=chats.get(i).getTimestamp();
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime= DateFormat.format("hh:mm",cal).toString();
        holder.time.setText(dateTime);
        holder.message_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this message?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(i,userid);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();
            }
        });


    }

    private void deleteMessage(int position, final String receiver) {
        final String myid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String msgTimestamp=chats.get(position).getTimestamp();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");
        Query query=reference.orderByChild("timestamp").equalTo(msgTimestamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.child("sender").getValue().equals(myid) || ds.child("recevier").getValue().equals(receiver)){
                        ds.getRef().removeValue();
                        Toast.makeText(context,"message deleted...",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context,"you cn delete your message",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView show,time;
        RelativeLayout message_layout;
        public MyHolder(View itemView){
            super(itemView);

            show=itemView.findViewById(R.id.show);
            time=itemView.findViewById(R.id.time_details);
            message_layout=itemView.findViewById(R.id.message_layout);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
