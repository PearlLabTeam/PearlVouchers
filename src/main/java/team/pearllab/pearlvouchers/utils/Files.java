package team.pearllab.pearlvouchers.utils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.simpleyaml.configuration.file.YamlFile;
import team.pearllab.pearlvouchers.PearlVouchers;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;

public class Files {

    private final PearlVouchers plugin;

    private final YamlFile voucherEditor = new YamlFile("plugins/PearlVouchers/guis/voucherEditor.yml");

    public Files(PearlVouchers plugin) {
        this.plugin = plugin;
        loadVoucherEditorFile();
    }

    private void loadVoucherEditorFile() {
        try {
            if (!voucherEditor.exists()) {
                plugin.saveResource("guis/voucherEditor.yml", false);
            }
            voucherEditor.load();
        } catch (Exception e) {
            Console.sendMessage(LogType.ERROR, "There was an error while trying to load the voucher editor file! Look at " +
                    "the log below for more information...\n" + ExceptionUtils.getFullStackTrace(e));
        }
    }

    public YamlFile getVoucherEditorFile() {
        return voucherEditor;
    }

}
