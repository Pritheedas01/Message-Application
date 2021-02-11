package com.pritha.www.otpverification;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pritha.www.otpverification.Adapter.AdapterUsers;
import com.pritha.www.otpverification.Model.ModelUsers;

import java.util.ArrayList;
import java.util.List;

/**

import java.util.ArrayList;
import java.util.List;

 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {


    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUsers> usersList;
    public StatusFragment(){

      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status2, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       usersList = new ArrayList<>();

       getAllUsers();

        return view;
    }

    private void getAllUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    ModelUsers user = snapshot.getValue(ModelUsers.class);
                    String myid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String id=""+snapshot.child("id").getValue();
                  if (!id.equals(myid)){
                      usersList.add(user);
                  }

                }

                adapterUsers = new AdapterUsers(getActivity(),usersList);
                recyclerView.setAdapter(adapterUsers);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
