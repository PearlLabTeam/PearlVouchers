package team.pearllab.pearlvouchers.voucher;

import me.java4life.generating.Generator;
import me.java4life.storage.Holder;
import me.java4life.storage.SerializeType;
import org.bukkit.inventory.ItemStack;
import team.pearllab.pearlvouchers.PearlVouchers;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;
import team.pearllab.pearlvouchers.utils.Materials;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class VoucherHolder extends Holder<Voucher> {

    // The plugin instance.
    private final PearlVouchers plugin;

    public VoucherHolder(PearlVouchers plugin) {
        this.plugin = plugin;
    }

    /**
     * The newVoucher function is used to create a new Voucher object.
     *
     * @param owner Set the owner of the voucher
     * @param items Set the voucher's inventory
     * @param hold  Whether to hold the voucher or not - This is used when the voucher is being created to edit it later
     *              but not to use it yet.
     * @return A voucher object
     */
    public Voucher newVoucher(UUID owner, ItemStack[] items, boolean hold) {
        String voucherID = assertUniqueID();

        // Set the data that the voucher needs to store to start working.
        Voucher voucher = new Voucher(this.plugin, voucherID, owner, true);
        voucher.setVoucherName("Voucher " + voucherID);
        voucher.setVoucherMaterial(Materials.getMaterial("PAPER"));
        voucher.setVoucherGlowing(false);
        voucher.setVoucherMessages(new ArrayList<>());
        voucher.setVoucherSounds(new ArrayList<>());
        voucher.setVoucherTitle("");
        voucher.setVoucherSubtitle("");
        voucher.setVoucherInventory(new VoucherInventory(voucher));

        // Set the voucher's inventory.
        for (ItemStack item : items) {
            voucher.getVoucherInventory().addItem(item);
        }

        voucher.setHolder(this);

        if (hold) {
            hold(voucher, false);
        }

        return voucher;
    }

    /**
     * The loadVoucher function loads a voucher from the file system.
     *
     * @param file        Get the file's name and extension
     * @param creatorUUID Get the creator of the voucher
     * @return An optional<voucher> object
     */
    public Optional<Voucher> loadVoucher(File file, String creatorUUID) {

        // Get the file's name but not the extension.
        String voucherID = file.getName().split("\\.")[0];

        // Get the file's extension but not the name.
        String extension = file.getName().split("\\.")[1];

        // Check if the file is a voucher file.

        if (!extension.equals("voucher")) {
            Console.sendMessage(LogType.WARNING, "The file " + file.getName() + " is not a voucher file.");
            return Optional.empty();
        }

        // Checks if the creatorUUID is a valid UUID.

        if (!creatorUUID.matches("[a-fA-F0-9]{8}-([a-fA-F0-9]{4}-){3}[a-fA-F0-9]{12}")) {
            Console.sendMessage(LogType.WARNING, "The creatorUUID " + creatorUUID + " is not a valid UUID.");
            return Optional.empty();
        }


        // Create and return the voucher object.
        Voucher voucher = new Voucher(plugin, voucherID, UUID.fromString(creatorUUID), false);
        voucher.setHolder(this);
        hold(voucher, false);

        return Optional.of(voucher);
    }

    /**
     * The saveVoucher function saves the voucher to a file.
     *
     * @param voucher Pass the voucher object to the function
     */
    public void saveVoucher(Voucher voucher) {
        voucher.serialize(SerializeType.VOID);
    }

    /**
     * The saveVoucher function saves a voucher to the database.
     *
     * @param voucherID Find the voucher that is to be saved
     */
    public void saveVoucher(String voucherID) {
        for (Voucher voucher : getContents()) {
            if (Objects.equals(voucher.getVoucherId(), voucherID)) {
                voucher.serialize(SerializeType.VOID);
                return;
            }
        }
    }

    /**
     * The destroy function is used to delete the voucher file from the local
     * storage. It also releases the voucher object from memory.
     *
     * @param voucher Get the voucherfile from the voucher object
     */
    public void destroy(Voucher voucher) {
        voucher.getVoucherFile().getFile().ifPresent(File::delete);
        release(voucher);
    }

    /**
     * The destroy function is used to delete a voucher from the VoucherPool.
     * It takes in a String, which is the ID of the voucher that needs to be deleted.
     * The function then iterates through all vouchers in the pool and checks if any of them have an ID matching that given as input.
     * If it finds one, it deletes its file (if present) and releases it from this pool.
     *
     * @param voucherID Find the voucher to be destroyed
     */
    public void destroy(String voucherID) {
        for (Voucher voucher : getContents()) {
            if (Objects.equals(voucher.getVoucherId(), voucherID)) {
                voucher.getVoucherFile().getFile().ifPresent(File::delete);
                release(voucher);
                return;
            }
        }
    }

    public void loadVouchers() {
        File folder = new File(this.plugin.getDataFolder() + "/vouchers");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] voucherFiles = folder.listFiles();
        if (voucherFiles == null || voucherFiles.length == 0) {
            Console.sendMessage(LogType.ANNOUNCE, "Found '0' vouchers to load.");
            return;
        }

        Console.sendMessage(LogType.ANNOUNCE, "Found '" + voucherFiles.length + "' vouchers to load.");

        for (File file : voucherFiles) {
            if (file != null && file.isFile()) {
                String fileName = file.getName();
                String creatorUUID = fileName.split("\\.")[0].split("_")[0];
                loadVoucher(file, creatorUUID);
            }
        }

        Console.sendMessage(LogType.ANNOUNCE, "Loaded a total of '" + voucherFiles.length + "' vouchers.");

    }

    /**
     * The assertUniqueID function generates a random voucher ID, and then checks if the voucher ID is already in use. If it is, it generates a new one.
     *
     * @return A unique voucher id
     */
    private String assertUniqueID() {

        // Generate a random voucher ID.
        String voucherID = Generator.randomText("aDDDDDD");


        // Check if the voucher ID is already in use. If it is, generate a new one.
        while (get(voucherID).isPresent()) {
            voucherID = Generator.randomText("aDDDDDD");
        }

        return voucherID;
    }

}
