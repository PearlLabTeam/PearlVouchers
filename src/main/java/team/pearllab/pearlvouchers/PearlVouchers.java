package team.pearllab.pearlvouchers;

import me.java4life.dialog.UserInputHolder;
import me.java4life.guis.GUIManager;
import me.java4life.guis.PagedGUI;
import me.java4life.guis.PagedGUIManager;
import org.bukkit.plugin.java.JavaPlugin;
import team.pearllab.pearlvouchers.commands.VoucherCommand;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;
import team.pearllab.pearlvouchers.lang.LangManager;
import team.pearllab.pearlvouchers.lang.Phrase;
import team.pearllab.pearlvouchers.utils.Configuration;
import team.pearllab.pearlvouchers.utils.Files;
import team.pearllab.pearlvouchers.voucher.VoucherHolder;

public final class PearlVouchers extends JavaPlugin {

    private Configuration pluginConfiguration;

    // The UserInputHolder class is used to store the user's input and prompt them for more information.
    private UserInputHolder userInputHolder;
    private LangManager langManager;

    private VoucherHolder voucherManager;

    private GUIManager guiManager;
    private PagedGUIManager pagedGUIManager;
    private Files files;

    @Override
    public void onEnable() {
        Console.sendMessage(LogType.ANNOUNCE, "Loading PearlVoucers v" + getDescription().getVersion() + "...");

        this.pluginConfiguration = new Configuration(this);
        this.userInputHolder = new UserInputHolder(this);
        this.langManager = new LangManager(this, pluginConfiguration.language);

        loadPhrases();

        Console.sendMessage(LogType.ANNOUNCE, "Loaded PearlVouchers v" + getDescription().getVersion() + "!");

        // Try to load the voucher manager.
        this.voucherManager = new VoucherHolder(this);
        voucherManager.loadVouchers();

        this.guiManager = new GUIManager(this);
        this.pagedGUIManager = new PagedGUIManager(this);
        this.files = new Files(this);

        registerCommands();


    }

    private void registerCommands() {
        getCommand("pvoucher").setExecutor(new VoucherCommand(this));
    }

    private void loadPhrases() {
        for (Phrase phrase : Phrase.values()) {
            langManager.getPhrase(phrase, null);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public Configuration getPluginConfiguration() {
        return pluginConfiguration;
    }

    public UserInputHolder getUserInputHolder() {
        return userInputHolder;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public GUIManager getGUIManager() {
        return guiManager;
    }

    public PagedGUIManager getPagedGUIManager() {
        return pagedGUIManager;
    }

    public Files getFiles() {
        return files;
    }

    public VoucherHolder getVoucherManager() {
        return voucherManager;
    }
}
