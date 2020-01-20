package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    EditText messageText;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    private ArrayList<String> chatMessages = new ArrayList<>();  //RecyclerViewAdapter sınıfına paslanacak ArrayList mesajlar buna eklenir

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.options_menu_sign_out){

            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);

        }else if (item.getItemId() == R.id.options_menu_profile){
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.chat);
        setContentView(R.layout.activity_chat);

        messageText = findViewById(R.id.chat_activity_message_text);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(chatMessages);

        RecyclerView.LayoutManager recyclerViewManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        getData();
    }

    @Override
    public void onBackPressed() {

    }

    public void sendMessage(View view) {

        if (!messageText.getText().toString().trim().equals("")){
            String messageToSend = messageText.getText().toString();

            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();

            FirebaseUser user = mAuth.getCurrentUser();

            String userEmail = "";
            if (user != null) {
                userEmail = user.getEmail();
            }

            databaseReference.child("Chats").child(uuidString).child("usermessage").setValue(messageToSend);
            databaseReference.child("Chats").child(uuidString).child("useremail").setValue(userEmail);
            databaseReference.child("Chats").child(uuidString).child("userMessageTime").setValue(ServerValue.TIMESTAMP);

            messageText.setText("");

            getData();

        }else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.you_can_not_send_empty_message), Toast.LENGTH_LONG).show();
        }
    }

    public void getData() {

        DatabaseReference newReference = database.getReference("Chats");

        Query query = newReference.orderByChild("userMessageTime");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //System.out.println("dataSnapshot Children: " + dataSnapshot.getChildren());
                //System.out.println("dataSnapshot Value: " + dataSnapshot.getValue());
                //System.out.println("dataSnapshot Key: " + dataSnapshot.getKey());

                chatMessages.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    //System.out.println("data value: " + ds.getValue());

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    String userEmail = hashMap.get("useremail");
                    String userMessage = hashMap.get("usermessage");

                    chatMessages.add(userEmail + ": " + userMessage);

                    recyclerViewAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
