package team.pearllab.pearlvouchers.console;

public enum LogType {

    ANNOUNCE("&8[&bPearl&eGifts&8] &7"),
    WARNING("&7[&bPearl&eGifts&7] &e(WARNING) &7"),
    ERROR("&7[&bPearl&eGifts&7] &c(ERROR) &7"),
    NONE("");

    private String format;
    LogType(String format){
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
