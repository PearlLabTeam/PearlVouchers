package team.pearllab.pearlvouchers.voucher;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import me.java4life.storage.Holdable;
import me.java4life.tools.CustomFile;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import team.pearllab.pearlvouchers.PearlVouchers;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;
import team.pearllab.pearlvouchers.utils.Materials;

import java.util.List;
import java.util.UUID;

public class Voucher extends Holdable {

    // The plugin instance
    private final PearlVouchers plugin;

    // The voucher's ID. This is the unique identifier for the voucher.
    private final String voucherId;

    // The UUID of the player who created the voucher.
    private final UUID voucherCreator;

    // The voucher file that contains the voucher's data.
    private CustomFile voucherFile;

    // The material of the voucher.
    private Material voucherMaterial;

    // Whether the voucher is glowing.
    private boolean voucherGlowing;

    // The messages that will be sent to the player when they use the voucher.
    private List<String> voucherMessages;

    // The sounds that will be played to the player when they use the voucher.
    private List<Sound> sounds;

    // The title and subtitle that will be sent to the player when they use the voucher.
    private String title;
    private String subtitle;

    public Voucher(PearlVouchers plugin, String voucherId, UUID voucherCreator) {
        this.plugin = plugin;
        this.voucherId = voucherId;
        this.voucherCreator = voucherCreator;

        // Try to load the voucher file. If something goes wrong, print the stack trace and return.
        if (!loadVoucherFile()) {
            return;
        }


        setVoucherData();
    }

    private void setVoucherData() {

        String data = voucherFile.getData();

        if (data.isEmpty()) {
            Console.sendMessage(LogType.ERROR, "Voucher file for voucher " + voucherId + " is empty! Either this " +
                    "voucher was already used or the file is corrupted.");
            return;
        }

        JsonObject voucherJson = Json.parse(data).asObject();

        // Get the material of the voucher
        this.voucherMaterial = Materials.getMaterial(voucherJson.getString("material", ""));

        // Get whether the voucher is glowing
        this.voucherGlowing = voucherJson.getBoolean("glowing", false);

        // Get the messages that will be sent to the player when they use the voucher.
        this.voucherMessages = voucherJson.get("messages").asArray().values().stream().map(JsonValue::asString).toList();

        // Get the sounds that will be played to the player when they use the voucher.
        this.sounds = voucherJson.get("sounds").asArray().values().stream().map(JsonValue::asString).map(Sound::valueOf).toList();

        // Get the title and subtitle that will be sent to the player when they use the voucher.
        this.title = voucherJson.getString("title", "");
        this.subtitle = voucherJson.getString("subtitle", "");




    }

    /**
     * The loadVoucherFile function attempts to load the voucher file.
     * If it fails, it prints the stack trace and returns false.
     * Otherwise, it returns true.
     *
     * @return A boolean
     */
    private boolean loadVoucherFile() {
        String path = "plugins/PearlVouchers/vouchers/" + this.voucherCreator.toString();

        try {
            this.voucherFile = new CustomFile(path, this.voucherId + ".json");
        } catch (Exception e) {
            Console.sendMessage(LogType.ERROR, "Failed to load voucher file for voucher " + voucherId + "! Look at the stack trace below for more information.\n"
                    + ExceptionUtils.getStackTrace(e));
            return false;
        }

        return true;
    }

    // GETTERS

    public String getVoucherId() {
        return voucherId;
    }

    public UUID getVoucherCreator() {
        return voucherCreator;
    }

    public CustomFile getVoucherFile() {
        return voucherFile;
    }

    public Material getVoucherMaterial() {
        return voucherMaterial;
    }

    public boolean isVoucherGlowing() {
        return voucherGlowing;
    }

    public List<String> getVoucherMessages() {
        return voucherMessages;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    // SETTERS

    public void setVoucherMaterial(Material voucherMaterial) {
        this.voucherMaterial = voucherMaterial;
    }

    public void setVoucherGlowing(boolean voucherGlowing) {
        this.voucherGlowing = voucherGlowing;
    }

    public void setVoucherMessages(List<String> voucherMessages) {
        this.voucherMessages = voucherMessages;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }


}
