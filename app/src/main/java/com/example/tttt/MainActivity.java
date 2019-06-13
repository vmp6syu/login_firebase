package com.example.tttt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView tView;
    Button mB1;
    Button mB2;
    EditText e1;
    EditText e2;
    String account_count;
    String[] acc = new String[10000];
    String[] pwd =new String[10000];
    boolean flag;
    DatabaseReference dBReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mode =dBReference.child("user");

    DatabaseReference dbaccount =mode.child("account");
    DatabaseReference dbpassword =mode.child("password");

    DatabaseReference dbdetail =mode.child("database");
    DatabaseReference usercount =dbdetail.child("count");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tView =(TextView)findViewById(R.id.textView);
        mB1=(Button) findViewById(R.id.button);
        mB2=(Button)findViewById(R.id.button2);
        e1=(EditText)findViewById(R.id.textView3); //acount
        e2=(EditText)findViewById(R.id.textView4); //password
        getuser();
    }
    protected  void onStart(){
        super.onStart();
        usercount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text =dataSnapshot.getValue(String.class);
                account_count=text;
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
        //craeat user
        mB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e1.getText().toString().equals("")||e2.getText().toString().equals(""))
               {
                   Toast.makeText(MainActivity.this,"Wrong enter",Toast.LENGTH_SHORT).show();
               }
               else if (accountcheck())
               {
                   Toast.makeText(MainActivity.this,"Existing account",Toast.LENGTH_SHORT).show();
               }
               else
                {
                   creatuser();
                   for(int i=0;i<Integer.valueOf(account_count);i++)
                   {
                       tView.setText(tView.getText().toString()+acc[i]);
                   }
               }
               getuser();
            }
        });
        mB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getuser();
                if (e1.getText().toString().equals("")||e2.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Wrong enter",Toast.LENGTH_SHORT).show();
                }
                else if (!accountcheck())
                {
                    Toast.makeText(MainActivity.this,"No Existing account",Toast.LENGTH_SHORT).show();
                }
                else if (login())
                {
                    Toast.makeText(MainActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"wrong password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void creatuser(){

        DatabaseReference tem =dbaccount.child(account_count);
        tem.setValue(e1.getText().toString());
        DatabaseReference tem2 =dbpassword.child(account_count);
        tem2.setValue(e2.getText().toString());
        int i = Integer.valueOf(account_count);
        usercount.setValue(Integer.toString(i+1));


    }

    void  getuser(){
        dbaccount.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i <dataSnapshot.getChildrenCount();i++)
                {
                    acc[i]=dataSnapshot.child(Integer.toString(i)).getValue(String.class);
                }
            }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        dbpassword.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i <dataSnapshot.getChildrenCount();i++)
                {
                    pwd[i]=dataSnapshot.child(Integer.toString(i)).getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    boolean  accountcheck(){
        flag=false;
        for (int i=0 ;i<Integer.valueOf(account_count);i++)
        {
            if (e1.getText().toString().equals(acc[i]))
            {
                flag= true;
            }
        }
        return flag;
    }

    boolean login()
    {
        flag=false;
        for (int i=0 ;i<Integer.valueOf(account_count);i++)
        {
            if (e1.getText().toString().equals(acc[i]))
            {
                if(e2.getText().toString().equals(pwd[i]))
                {
                    flag= true;
                }
            }
        }
        return flag;
    }
}
