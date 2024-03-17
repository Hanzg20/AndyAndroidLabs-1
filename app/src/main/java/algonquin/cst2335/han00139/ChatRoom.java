package algonquin.cst2335.han00139;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.han00139.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.han00139.databinding.ReceiveMessageBinding;
import algonquin.cst2335.han00139.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            chatModel.messages.postValue(messages=new ArrayList<ChatMessage>());
        }
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendButton.setOnClickListener(click->{
            String currentDateAndTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.textInput.getText().toString(),currentDateAndTime, true));
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click->{
            String currentDateAndTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.textInput.getText().toString(),currentDateAndTime, false));
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
        });

        binding.recycleView.setAdapter(myAdapter= new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                switch (viewType) {
                    case 0:
                        // Inflate send_message layout
                        SentMessageBinding binding1= SentMessageBinding.inflate(getLayoutInflater());
                        return new MyRowHolder(binding1.getRoot());
                    case 1:
                        // Inflate receive_message layout
                        ReceiveMessageBinding binding2= ReceiveMessageBinding.inflate(getLayoutInflater());
                        return new MyRowHolder(binding2.getRoot());
                    default:
                        // Handle the default case or throw an exception
                        throw new IllegalArgumentException("Invalid viewType: " + viewType);
                }

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage = messages.get(position);
                return chatMessage.isSentButton()?1:0;
            }
        });
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                MessageDatabase.class, "database-name").build();
        ChatMessageDAO cmDAO = db.cmDAO();

        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( cmDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
            builder.setTitle("Question");
            builder.setPositiveButton("No",(dialog,cl) ->{});
            builder.setNegativeButton("YES",(dialog,cl) ->{
               // ChatMessage m = messages.get(position);
            });

            itemView.setOnClickListener(clk ->{
              //  int position = getAbsoluteAdapterPosition();
            });
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
