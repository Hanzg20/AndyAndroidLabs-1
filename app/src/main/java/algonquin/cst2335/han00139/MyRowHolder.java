package algonquin.cst2335.han00139;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRowHolder extends RecyclerView.ViewHolder {
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