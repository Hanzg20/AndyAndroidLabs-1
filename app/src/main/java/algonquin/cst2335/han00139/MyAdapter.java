package algonquin.cst2335.han00139;import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyRowHolder> {

    private final ArrayList<ChatMessage> messages;
    private final Context context;

    public MyAdapter(Context context, ArrayList<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
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
        } else {
            holder.avatarImageView.setImageResource(R.drawable.receiver);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  showDeleteDialog(holder.getAdapterPosition());
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
                ChatMessage removedMessage = messages.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, messages.size());

                Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                                String.format("You deleted message #%d", position + 1), Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                messages.add(position, removedMessage);
                                notifyItemInserted(position);
                            }
                        }).show();
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