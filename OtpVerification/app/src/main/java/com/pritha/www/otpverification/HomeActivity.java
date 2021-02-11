package com.pritha.www.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pritha.www.otpverification.Adapter.AdapterUsers;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
   // private Toolbar toolbar;
   DrawerLayout drawer;
   private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int currentDayNight;
    RecyclerView recyclerView;
    private AdapterUsers adapter;
    private TextView mail;
    private TextView user;
    private FirebaseAuth fAuth;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // toolbar=findViewById(R.id.myTollbar);
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.myViewPager);
        mail=findViewById(R.id.mail);
        user=findViewById(R.id.show_name);
        currentDayNight = AppCompatDelegate.getDefaultNightMode();


        //setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, Contacts.class);
                startActivity(intent);
            }
        });

        drawer = findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        reference= FirebaseDatabase.getInstance().getReference("Users");
        final String myid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query usQuery=reference.orderByChild("id").equalTo(myid);
        usQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String name=""+ds.child("username").getValue();
                    String pic=""+ds.child("imageUrl").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }




    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatFragment(),"CHATS");
        viewPagerAdapter.addFragment(new StatusFragment(),"USERS");
        //viewPagerAdapter.addFragment(new CallsFragment(),"CALLS");
        viewPager.setAdapter(viewPagerAdapter);
    }
    private void status(String status){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        status("online");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (currentDayNight != AppCompatDelegate.getDefaultNightMode()) {
            recreate();
        }
    }

    public void gotoSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:

                Intent intent= new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_gallery:
                Intent in= new Intent(HomeActivity.this, Contacts.class);
                startActivity(in);
                break;
            case R.id.nav_call:
                Intent intentt= new Intent(HomeActivity.this, CallllActivity.class);
                startActivity(intentt);
                break;

            case R.id.nav_slideshow:
                Intent intent3= new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_share:

                break;
            case R.id.nav_logout:
                //FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(HomeActivity.this, Login.class);
                startActivity(intent1);
                finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
