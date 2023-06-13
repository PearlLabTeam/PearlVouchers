package team.pearllab.pearlvouchers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.pearllab.pearlvouchers.PearlVouchers;
import team.pearllab.pearlvouchers.guis.VoucherEditor;

public class VoucherCommand implements CommandExecutor {

    private final PearlVouchers plugin;

    public VoucherCommand(PearlVouchers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //usage: /<command> [help | reload | create | edit | delete | give | list | info | redeem]

        if (command.getName().equalsIgnoreCase("pvoucher")) {
            Player player = (Player) sender;
            // Open a new VoucherEditor GUI. In this case we are passing null as the voucher, which means we are creating a new voucher
            // reference which will be the current voucher being edited.
            player.openInventory(new VoucherEditor(player, plugin, null).getInventory());
        }

        return false;
    }
}
