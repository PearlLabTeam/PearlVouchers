package team.pearllab.pearlvouchers.console;

import me.java4life.visuals.Text;
import org.bukkit.Bukkit;

public class Console {

    public static void sendMessage(LogType type, String message){
        Bukkit.getConsoleSender().sendRawMessage(Text.toChatColor(type.getFormat() + message));
    }

}
