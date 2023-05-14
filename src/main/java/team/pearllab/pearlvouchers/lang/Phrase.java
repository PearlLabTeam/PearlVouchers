package team.pearllab.pearlvouchers.lang;

public enum Phrase {
    NO_PERMISSIONS("no-perm"),
    PLAYER_OFFLINE("player-offline");
    private String key;

    Phrase(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}