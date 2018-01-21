package com.pencetcodestudio.chatyuk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    int SIGN_IN_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Posisi login, apakah udah login atau belum
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        }
        else {
            Toast.makeText(this, "Hello " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                    Toast.LENGTH_LONG).show();
            displayMessages();
        }

        //Kirim Pesan
        FloatingActionButton fly = (FloatingActionButton) findViewById(R.id.fly_button);
        fly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Deklarasi ID input
                EditText input = (EditText) findViewById(R.id.input);
                //Kirim data ke Database firebase
                FirebaseDatabase.getInstance().getReference().child("Pesan").push().setValue(new activity_chatroom(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                //Setelah pencet send makan edittext akan di clear
                input.setText("");
            }
        });

    }

    //Tampilkan pesan
    public void displayMessages() {
        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        FirebaseListAdapter<activity_chatroom> adapter = new FirebaseListAdapter<activity_chatroom>
                (this, activity_chatroom.class, R.layout.activity_chatroom,
                        FirebaseDatabase.getInstance().getReference().child("Pesan")) {
            @Override
            //model nama var
            protected void populateView(View v, activity_chatroom model, int position) {
                //Deklarasi id
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                //SetText (Ngambil data dari model)
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    //Posisi Login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //OK
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
                displayMessages();
            }
            //Bad
            else {
                Toast.makeText(this,
                        "Please try again later.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    //Menu Logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "signed out.", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
        return true;
    }
}