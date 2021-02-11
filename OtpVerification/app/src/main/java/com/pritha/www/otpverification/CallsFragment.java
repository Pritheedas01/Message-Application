package com.pritha.www.otpverification;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {
    /*RecyclerView recyclerView;
    AdapterCalls adapterCalls;
    List<Calls> callsList;*/

    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        /*recyclerView = view.findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        callsList = new ArrayList<>();

        getAllUsers();*/

        return view;
    }

    /*private void getAllUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Calls user = snapshot.getValue(Calls.class);
                    String phoneNumber= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                    String id=""+snapshot.child("id").getValue();
                    if (!id.equals(phoneNumber)){
                        callsList.add(user);
                    }

                }

            adapterCalls = new AdapterCalls(getActivity(),callsList);
                recyclerView.setAdapter(adapterCalls);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


}
