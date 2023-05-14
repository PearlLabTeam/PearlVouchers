package team.pearllab.pearlvouchers.utils;

import org.bukkit.Material;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;

public class Materials {

    public static Material getMaterial(String name){
        try {
            return Material.valueOf(name);
        }catch (Exception e){
            Console.sendMessage(LogType.WARNING, "Couldn't find material type " + name + ". Replacing with " +
                    "stone");
            e.printStackTrace();
            return Material.STONE;
        }
    }

}
