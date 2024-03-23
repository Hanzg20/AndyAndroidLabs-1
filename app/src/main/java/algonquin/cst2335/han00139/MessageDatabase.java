package algonquin.cst2335.han00139;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {
    private static MessageDatabase instance;

    public abstract ChatMessageDAO cmDAO();

    public static synchronized MessageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDatabase.class, "message_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

