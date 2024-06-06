package com.ugb.controlesbasicos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chatsoporte extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView chatListView;
    private EditText messageEditText;
    private Button sendButton;
    private Button salir;
    private ArrayList<String> messages;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatsoporte);

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Inicializar vistas
        chatListView = findViewById(R.id.chatListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        chatListView.setAdapter(adapter);
        salir = findViewById(R.id.regre);

        // Limpiar el chat al abrir la actividad
        clearChat();

        // Configurar el botón de enviar mensaje
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    messageEditText.setText("");
                } else {
                    Toast.makeText(chatsoporte.this, "Por favor, escriba un mensaje.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clearChat() {
        databaseReference.child("chats").removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Escuchar eventos en la base de datos después de limpiar el chat
                setupChatListener();
            } else {
                Toast.makeText(chatsoporte.this, "Error al limpiar el chat: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupChatListener() {
        databaseReference.child("chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                String message = dataSnapshot.child("message").getValue(String.class);
                String response = dataSnapshot.child("response").getValue(String.class);
                if (message != null && response != null) {
                    messages.add("User: " + message);
                    messages.add("Bot: " + response);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(chatsoporte.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String message) {
        // Obtener la respuesta predeterminada
        databaseReference.child("predeterminedResponses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String response = "";
                if (messages.size() == 0) {
                    response = dataSnapshot.child("step1").getValue(String.class);
                } else if (messages.size() == 2) {
                    response = dataSnapshot.child("step2").getValue(String.class);
                } else if (messages.size() == 4) {
                    response = dataSnapshot.child("step3").getValue(String.class);
                }

                if (response != null) {
                    // Guardar el mensaje y la respuesta en la base de datos
                    String chatId = databaseReference.child("chats").push().getKey();
                    if (chatId != null) {
                        Map<String, String> chatMessage = new HashMap<>();
                        chatMessage.put("message", message);
                        chatMessage.put("response", response);
                        databaseReference.child("chats").child(chatId).setValue(chatMessage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(chatsoporte.this, "Error al obtener respuestas: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Eliminar todos los mensajes de la base de datos al cerrar la actividad
        databaseReference.child("chats").removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(chatsoporte.this, "Chat eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(chatsoporte.this, "Error al eliminar el chat: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
