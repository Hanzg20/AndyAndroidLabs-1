package algonquin.cst2335.han00139;

import static android.provider.Settings.System.DATE_FORMAT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChatRoom extends AppCompatActivity {

    private static final String DATABASE_NAME = "chat-message_database";
    private MyAdapter myAdapter;
    private ArrayList<ChatMessage> messages;
    private ChatMessageDAO mDAO; // Declare DAO
    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // Only one declaration needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initDatabase();
        initRecyclerView();
        initSendButton();
        initReceiveButton();
    }

    private void initDatabase() {
        // Initialize database and DAO
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                        MessageDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        mDAO = db.cmDAO();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.myRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();
        myAdapter = new MyAdapter(this, messages);
        recyclerView.setAdapter(myAdapter);
    }

    private void initSendButton() {
        Button sendButton = findViewById(R.id.submitButton);
        sendButton.setOnClickListener(v -> sendMessage(true));
    }

    private void initReceiveButton() {
        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener(v -> sendMessage(false));
    }

    private void sendMessage(boolean isSentByUser) {
        EditText editText = findViewById(R.id.editText);
        String messageText = editText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            ChatMessage newMessage = new ChatMessage(messageText, getCurrentTime(), isSentByUser);
            executorService.execute(() -> mDAO.insertMessage(newMessage));

            messages.add(newMessage);
            runOnUiThread(() -> {
                myAdapter.notifyItemInserted(messages.size() - 1);
                editText.setText("");
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            showDeleteAllMessagesDialog();
            return true;
        } else if (id == R.id.action_about) {
            showToastAbout();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteAllMessagesDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Messages")
                .setMessage("Are you sure you want to delete all messages?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    executorService.execute(() -> {
                        mDAO.deleteAll();
                        runOnUiThread(() -> {
                            int size = messages.size();
                            messages.clear();
                            myAdapter.notifyItemRangeRemoved(0, size);
                            Toast.makeText(ChatRoom.this, "All messages deleted", Toast.LENGTH_SHORT).show();
                        });
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showToastAbout() {
        Toast.makeText(this, "Version 1.0, created by Zhaoguo Han", Toast.LENGTH_SHORT).show();
    }

    private String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

}