package team.pearllab.pearlvouchers.guis;

import me.java4life.dialog.UserInput;
import me.java4life.guis.*;
import me.java4life.tools.Conditions;
import me.java4life.visuals.Text;
import me.java4life.visuals.TextFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlFile;
import team.pearllab.pearlvouchers.PearlVouchers;
import team.pearllab.pearlvouchers.lang.Phrase;
import team.pearllab.pearlvouchers.utils.Materials;
import team.pearllab.pearlvouchers.voucher.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherEditor extends GUI {

    private final PearlVouchers plugin;
    private final Voucher currentVoucher;

    public VoucherEditor(Player p, PearlVouchers plugin, @Nullable Voucher currentVoucher) {
        super(p);
        this.plugin = plugin;


        //Check if the @currentVoucher is null, if it is, then we are creating a new voucher.
        if (currentVoucher == null) {
            this.currentVoucher = plugin.getVoucherManager().newVoucher(p.getUniqueId(), new ItemStack[0], false);
        } else {
            this.currentVoucher = currentVoucher;
        }

        YamlFile voucherEditorFile = plugin.getFiles().getVoucherEditorFile();

        Model model = new Model();

        model.setDisplayName(Text.toChatColor(voucherEditorFile.getString("gui-title")));
        model.setSize(voucherEditorFile.getInt("gui-size"));

        for (String key : voucherEditorFile.getConfigurationSection("content").getKeys(false)) {

            // Get the function of the item.
            String function = voucherEditorFile.getString("content." + key + ".type");

            // Get the material of the item.
            Material material = Materials.getMaterial(voucherEditorFile.getString("content." + key + ".material"));

            // Get the name of the item.
            String name = applyPlaceholders(voucherEditorFile.getString("content." + key + ".name"));

            // Get the lore of the item.
            List<String> lore = new ArrayList<>(voucherEditorFile.getStringList("content." + key + ".lore"));

            // Apply the placeholders for the lore
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, applyPlaceholders(lore.get(i)));
            }

            // Get the slots of the item.
            List<Integer> slots = retrieveSlots(voucherEditorFile.getString("content." + key + ".slot"));

            // Sort through the slots and for each slot, create a new Button object and add it to the model.

            for (int slot : slots) {
                Runnable buttonFunction = getFunction(function, getP());

                // Create a new ButtonCreator object and add the data to it.
                ButtonCreator buttonCreator = Button.create();
                buttonCreator.madeOf(material);
                buttonCreator.withName(name);
                buttonCreator.inSlot(slot);
                lore.forEach(buttonCreator::addLine);
                buttonCreator.withAction(ClickType.LEFT, buttonFunction);

                // Add the ButtonCreator object to the model.
                buttonCreator.andAddToModel(model);
            }
        }

        construct(model);
        plugin.getGUIManager().register(this, getP());

    }

    private Runnable getFunction(String function, Player p) {
        return switch (function) {
            case "rename" -> this::promptForName;
            case "material-selector" -> this::promptForMaterial;
            case "toggle-glowing" -> () -> {
                p.sendMessage("toggle-glowing function ran...");
                p.closeInventory();
            };
            case "edit-messages" -> () -> {
                p.sendMessage("edit-messages function ran...");
                p.closeInventory();
            };
            case "edit-sounds" -> () -> {
                p.sendMessage("edit-sounds function ran...");
                p.closeInventory();
            };
            case "edit-title" -> () -> {
                p.sendMessage("edit-title function ran...");
                p.closeInventory();
            };
            case "edit-subtitle" -> () -> {
                p.sendMessage("edit-subtitle function ran...");
                p.closeInventory();
            };
            case "edit-inventory" -> () -> {
                p.sendMessage("edit-inventory function ran...");
                p.closeInventory();
            };
            case "craft" -> () -> {
                p.sendMessage("craft function ran...");
                p.closeInventory();
            };
            case "cancel" -> () -> {
                p.sendMessage("cancel function ran...");
                p.closeInventory();
            };
            default -> () -> {
            };
        };
    }

    /**
     * The retrieveSlots function takes in a string and returns a list of integers.
     * The string is expected to be formatted as follows: "0-2-4-6-7"
     * This means that the function should return [0, 2, 4, 6 , 7]
     *
     * @param string Split the string into a list of strings
     * @return A list of integers
     */
    private List<Integer> retrieveSlots(String string) {
        List<Integer> slots = new ArrayList<>();

        for (String s : string.split("-")) {
            if (Conditions.isInteger(s)) {
                slots.add(Integer.parseInt(s));
            }
        }

        return slots;
    }

    private void promptForName() {

        // Create a new UserInput object which will prompt the user for a name for the voucher. The user
        // will have to type in the name in chat.
        UserInput input = new UserInput(plugin) {
            @Override
            public void onTick() {
            }
        };

        // Set the player of the input to the player who is editing the voucher.
        input.setPlayer(getP());

        // Add the input to the UserInputHolder.
        plugin.getUserInputHolder().submit(getP(), input);

        // Close the player's opened inventory.
        getP().closeInventory();

        // Play a simple sound to indicate that the input prompt has started.
        getP().playSound(getP().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

        // Send the message of prompt to the player.
        getP().sendMessage(Text.toChatColor(plugin.getLangManager().getPhrase(Phrase.RENAME_VOUCHER, getP())));

        // Set the function that will be ran when the user types in the name.
        input.onMessage(() -> {
            String inputName = Text.stripColor(input.getMessage());

            // Check if the input equals "cancel", if it does, then cancel the input prompt, play a sound, and reopen the voucher editor.
            if (inputName.equalsIgnoreCase("cancel")) {
                input.remove(plugin.getUserInputHolder());

                // Open the voucher editor again with the updated @currentVoucher and play a sound in the main thread.
                Bukkit.getScheduler().runTask(plugin, () -> {
                    getP().playSound(getP().getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    getP().openInventory(new VoucherEditor(getP(), plugin, this.currentVoucher).getInventory());
                });

                return;
            }

            // Replace all spaces with underscores.
            inputName = inputName.replaceAll(" ", "_");

            // Limit the name to 32 characters.
            inputName = inputName.substring(0, Math.min(inputName.length(), 32));

            // Set the name of the voucher.
            this.currentVoucher.setVoucherName(inputName);

            // Exit out of the input prompt, then open the voucher editor again with the updated @currentVoucher.
            input.remove(plugin.getUserInputHolder());

            // Open the voucher editor again with the updated @currentVoucher and play a sound in the main thread.
            Bukkit.getScheduler().runTask(plugin, () -> {
                getP().playSound(getP().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                getP().openInventory(new VoucherEditor(getP(), plugin, this.currentVoucher).getInventory());
            });
        });
    }

    private void promptForMaterial() {
        // Create a new material selector and set the onSelect function to set the material of the voucher.
        MaterialSelector materialSelector = new MaterialSelector(getP(), false, true);
        materialSelector.setOnSelect(() -> {
            // Assign the selected material to the voucher material,and reopen the voucher editor.
            Material material = materialSelector.getMaterial();
            this.currentVoucher.setVoucherMaterial(material);
            getP().openInventory(new VoucherEditor(getP(), plugin, currentVoucher).getInventory());

            // Play the same sound played on the promptForName function.
            getP().playSound(getP().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        });

        // Submit the material selector to the GUIManager.
        plugin.getPagedGUIManager().register(getP(), materialSelector);

        // Open the first page of the material selector.
        getP().openInventory(materialSelector.getPages().get(1).getInventory());

        // Play a sound to indicate that the material selector has opened. A barrel opening sound is a good sound to play.
        getP().playSound(getP().getLocation(), Sound.BLOCK_BARREL_OPEN, 1, 1);

    }

    /**
     * The applyPlaceholders function takes in a string and replaces all placeholders with their respective values.
     * The placeholders are as follows:
     * %voucher_name% - The name of the voucher
     * %voucher_id% - The ID of the voucher
     *
     * @param string The string to apply the placeholders to
     * @return The string with the placeholders applied
     */

    private String applyPlaceholders(String string) {
        string = TextFormat.replaceStrings(string,
                "%voucher_name%", this.currentVoucher.getVoucherName(),
                "%voucher_id%", this.currentVoucher.getVoucherId(),
                "%voucher_material%", this.currentVoucher.getVoucherMaterial().name(),
                "%voucher_glowing%", this.currentVoucher.isVoucherGlowing() ? plugin.getLangManager().getPhrase
                        (Phrase.TRUE, getP()) : plugin.getLangManager().getPhrase(Phrase.FALSE, getP()));
        return string;
    }

}
