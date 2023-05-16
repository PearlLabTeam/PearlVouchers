package team.pearllab.pearlvouchers.voucher;

import me.java4life.serializers.ItemSerializer;
import org.bukkit.inventory.ItemStack;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoucherInventory {

    // The voucher that this inventory is linked to.
    private final Voucher linkedVoucher;

    // The items that are in this inventory. This is the contents of the items that will be given to the player when they use the voucher.
    private final List<ItemStack> items;


    /**
     * The VoucherInventory function is a constructor for the VoucherInventory class.
     * It takes in two parameters: a linkedVoucher and data. The linkedVoucher parameter
     * is used to link the inventory to its voucher, while the data parameter contains all
     * the items that are contained within this inventory. The function then adds these items
     * into an ArrayList called "items". This ArrayList will be used later on when we want to access
     * or modify any of these items in this voucher's inventory.
     *
     * @param linkedVoucher Link the voucherInventory to a specific voucher
     * @param data          Add the contents of the inventory
     */
    public VoucherInventory(Voucher linkedVoucher, String data) {
        this.linkedVoucher = linkedVoucher;
        this.items = new ArrayList<>();

        // Add the contents to the inventory. Uses the data from the voucher file.
        addContents(data);
    }

    /**
     * The VoucherInventory function is a constructor for the VoucherInventory class.
     * It takes in a linkedVoucher and creates an ArrayList of items.
     *
     * @param linkedVoucher Link the voucherInventory to a specific voucher
     */
    public VoucherInventory(Voucher linkedVoucher) {
        this.linkedVoucher = linkedVoucher;
        this.items = new ArrayList<>();
    }

    /**
     * The addContents function is used to add the contents of a voucher to an inventory.
     *
     * @param data Store the items in the inventory
     */
    private void addContents(String data) {

        // Checks if there is no items stored in the data. If there is no items, return.
        if (data.isEmpty()) {
            return;
        }

        // Try to add the items to the inventory. If something goes wrong, print the stack trace and return.
        try {
            items.addAll(Arrays.asList(ItemSerializer.itemStackArrayFromBase64(data)));
        } catch (Exception e) {
            Console.sendMessage(LogType.WARNING, "Couldn't add contents to voucher " + linkedVoucher.getVoucherId() + ". " +
                    "Replacing with empty inventory.");
        }
    }

    /**
     * The toString function is used to convert the contents of an inventory into a string.
     * This is useful for saving inventories in files, or sending them over the network.
     *
     * @return A string of the items in the inventory
     */
    public String toString() {
        return ItemSerializer.itemStackArrayToBase64(items.toArray(new ItemStack[0]));
    }

    public void fromString(String data) {

        // Clear the items arraylist if there are items in it.
        if (items.size() > 0) {
            items.clear();
        }

        addContents(data);
    }

    /**
     * The addItem function adds an item to the inventory.
     *
     * @param item Add an item to the items arraylist
     */
    public void addItem(ItemStack item) {
        items.add(item);
    }

    // GETTERS

    public Voucher getLinkedVoucher() {
        return linkedVoucher;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    // SETTERS

    public void setItems(List<ItemStack> items) {
        this.items.clear();
        this.items.addAll(items);
    }


}
