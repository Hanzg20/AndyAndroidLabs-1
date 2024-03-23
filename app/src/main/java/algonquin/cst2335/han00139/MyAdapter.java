package algonquin.cst2335.han00139;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import algonquin.cst2335.han00139.ChatMessageDAO;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyRowHolder> {

    private final ArrayList<ChatMessage> messages;
    private final Context context;

    private ChatMessageDAO mDAO; // Add this line

    public MyAdapter(Context context, ArrayList<ChatMessage> messages, ChatMessageDAO dao) { // Modified constructor
        this.context = context;
        this.messages = messages;
        this.mDAO = dao; // Assign mDAO
    }
    public MyAdapter(Context context, ArrayList<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void setMessages(ArrayList<ChatMessage> newMessages) {
        this.messages.clear(); // Clear existing messages
        this.messages.addAll(newMessages); // Add all new messages
        notifyDataSetChanged(); // Notify the adapter to refresh the list
    }


    @NonNull
    @Override
    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.sent_message, parent, false);
        } else {
            view = inflater.inflate(R.layout.receive_message, parent, false);
        }
        return new MyRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageText.setText(message.getMessage());
        holder.timeText.setText(message.getTimeSent());

        if (message.isSent()) {
            holder.avatarImageView.setImageResource(R.drawable.sender);
            holder.messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            holder.avatarImageView.setImageResource(R.drawable.receiver);
            holder.messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isSent()) {
            return 0;
        } else {
            return 1;
        }
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Message");
        builder.setMessage("Do you want to delete this message?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the message to be removed
                ChatMessage removedMessage = messages.remove(position);

                // Perform database operation in the background
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Delete from database
                        mDAO.deleteMessage(removedMessage);

                        // Run on UI thread after deletion
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Notify adapter of item removal
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, messages.size());

                                // Show snackbar with undo option
                                Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                                                String.format("You deleted message #%d", position + 1), Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Re-add message to the list and database on undo
                                                messages.add(position, removedMessage);
                                                notifyItemInserted(position);
                                                executor.execute(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // Insert into database
                                                        mDAO.insertMessage(removedMessage);
                                                    }
                                                });
                                            }
                                        }).show();
                            }
                        });
                    }
                });
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


    public static class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        ImageView avatarImageView;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
        }
    }
}