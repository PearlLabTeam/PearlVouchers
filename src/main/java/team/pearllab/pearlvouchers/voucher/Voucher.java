package team.pearllab.pearlvouchers.voucher;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import me.java4life.storage.Holdable;
import me.java4life.storage.SerializeType;
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
    private List<Sound> voucherSounds;

    // The title and subtitle that will be sent to the player when they use the voucher.
    private String voucherTitle;
    private String voucherSubtitle;

    private VoucherInventory voucherInventory;

    public Voucher(PearlVouchers plugin, String voucherId, UUID voucherCreator, boolean newVoucher) {
        this.plugin = plugin;
        this.voucherId = voucherId;
        this.voucherCreator = voucherCreator;

        // Try to load the voucher file. If something goes wrong, print the stack trace and return.
        if (!loadVoucherFile(newVoucher)) {
            return;
        }

        // Set the voucher data if this is not a new voucher.
        if (!newVoucher) {
            loadVoucherData();
        }
    }

    /**
     * The loadVoucherData function is used to set the data of a voucher.
     * It does this by reading the data from a file and parsing it into JSON format, then setting all the variables
     * in this class based on what was read from that file.
     */
    private void loadVoucherData() {

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
        this.voucherSounds = voucherJson.get("sounds").asArray().values().stream().map(JsonValue::asString).map(Sound::valueOf).toList();

        // Get the title and subtitle that will be sent to the player when they use the voucher.
        this.voucherTitle = voucherJson.getString("title", "");
        this.voucherSubtitle = voucherJson.getString("subtitle", "");

        // Gets the voucher inventory if it exists, otherwise creates a new one.
        if (voucherJson.get("inventory") != null) {
            this.voucherInventory = new VoucherInventory(this, voucherJson.getString("inventory", ""));
        } else {
            this.voucherInventory = new VoucherInventory(this);
        }

    }

    /**
     * The loadVoucherFile function attempts to load the voucher file.
     * If it fails, it prints the stack trace and returns false.
     * Otherwise, it returns true.
     *
     * @return A boolean
     */
    private boolean loadVoucherFile(boolean newVoucher) {
        String path = "plugins/PearlVouchers/vouchers/" + this.voucherCreator.toString();

        try {
            this.voucherFile = new CustomFile(path, this.voucherId + ".json");
        } catch (Exception e) {
            if (newVoucher) {
                Console.sendMessage(LogType.ERROR, "Failed to create voucher file for voucher " + this.voucherId + "!");
            } else {
                Console.sendMessage(LogType.ERROR, "Failed to load voucher file for voucher " + this.voucherId + "!");
            }
        }

        return true;
    }

    @Override
    public Object serialize(SerializeType type) {
        JsonObject serializedVoucher = new JsonObject();

        serializedVoucher.add("material", this.voucherMaterial.toString());
        serializedVoucher.add("glowing", this.voucherGlowing);
        serializedVoucher.add("messages", this.voucherMessages.toString());
        serializedVoucher.add("sounds", this.voucherSounds.toString());
        serializedVoucher.add("title", this.voucherTitle);
        serializedVoucher.add("subtitle", this.voucherSubtitle);
        serializedVoucher.add("inventory", this.voucherInventory.toString());

        // dump the data to the custom file
        this.voucherFile.dumpData(serializedVoucher.toString());

        // return the serialized voucher data in JSON format. The data will already be dumped to the file.
        return serializedVoucher;
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

    public List<Sound> getVoucherSounds() {
        return this.voucherSounds;
    }

    public String getVoucherTitle() {
        return this.voucherTitle;
    }

    public String getVoucherSubtitle() {
        return this.voucherSubtitle;
    }

    public VoucherInventory getVoucherInventory() {
        return voucherInventory;
    }

    public PearlVouchers getPlugin() {
        return plugin;
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

    public void setVoucherSounds(List<Sound> sounds) {
        this.voucherSounds = sounds;
    }

    public void setVoucherTitle(String title) {
        this.voucherTitle = title;
    }

    public void setVoucherSubtitle(String subtitle) {
        this.voucherSubtitle = subtitle;
    }

    public void setVoucherInventory(VoucherInventory voucherInventory) {
        this.voucherInventory = voucherInventory;
    }

    public void setVoucherFile(CustomFile voucherFile) {
        this.voucherFile = voucherFile;
    }

}
