package team.pearllab.pearlvouchers.lang;

public enum Phrase {
    NO_PERMISSIONS("no-perm"),
    PLAYER_OFFLINE("player-offline"),
    PREFIX("prefix"),
    TRUE("true"),
    FALSE("false"),
    RENAME_VOUCHER("rename-voucher"),
    RENAME_VOUCHER_CANCEL("rename-voucher-cancel"),
    RENAME_VOUCHER_SUCCESS("rename-voucher-success");


    private String key;

    Phrase(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}