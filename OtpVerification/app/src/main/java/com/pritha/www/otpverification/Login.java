package com.pritha.www.otpverification;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pritha.www.otpverification.CountryData;
import com.pritha.www.otpverification.HomeActivity;
import com.pritha.www.otpverification.OtpActivity;
import com.pritha.www.otpverification.R;
import com.pritha.www.otpverification.Signup1;

public class Login extends AppCompatActivity {
    EditText email,password,phone;
    private Spinner spinner;
    TextView otp;
    Button login;
    Button signup;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        //login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        otp=findViewById(R.id.otp1);

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.layout_dialog, null);





                // set alert_dialog.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.edit_username);
                final EditText userInput1 = (EditText) promptsView.findViewById(R.id.edit_password);

                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();

                        String txt_email=userInput.getText().toString();
                        String txt_password= userInput1.getText().toString();
                        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                            Toast.makeText(Login.this,"all feilds are required",Toast.LENGTH_SHORT).show();
                        }else{
                            auth.signInWithEmailAndPassword(txt_email,txt_password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                Intent intent=new Intent(Login.this, HomeActivity.class);
                                                startActivity(intent);
                                            }else {
                                                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });



                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });




                spinner = findViewById(R.id.spinnerCountries);
                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

                findViewById(R.id.otp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                        String number = phone.getText().toString().trim();

                        if (number.isEmpty() || number.length() < 10) {
                            phone.setError("Valid number is required");
                            phone.requestFocus();
                            return;
                        }

                        String phonenumber = "+" + code + number;

                        Intent intent = new Intent(Login.this, OtpActivity.class);
                        intent.putExtra("phonenumber", phonenumber);
                        startActivity(intent);

                    }
                });


                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Login.this, Signup1.class);
                        startActivity(intent);
                    }
                });
               /* login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt_email=email.getText().toString();
                        String txt_password= password.getText().toString();
                        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                            Toast.makeText(Login.this,"all feilds are required",Toast.LENGTH_SHORT).show();
                        }else{
                            auth.signInWithEmailAndPassword(txt_email,txt_password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                Intent intent=new Intent(Login.this, HomeActivity.class);
                                                startActivity(intent);
                                            }else {
                                                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });*/

            }


        }