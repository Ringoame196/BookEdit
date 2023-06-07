package com.github.ringoame196.bookedit.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class bookedit implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
// プレイヤーでない場合の処理
            commandSender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
            return true;
        }
        Player player = (Player) commandSender;
        ItemStack mainitem = player.getInventory().getItemInMainHand();
        if (!mainitem.getType().equals(Material.WRITTEN_BOOK)) {
            player.sendMessage(ChatColor.RED + "記入済みの本をメインハンドに持って実行してください");
            return true;
        }
        if (strings.length == 0) {
            player.sendMessage(ChatColor.AQUA + "記入前に戻す-/bookedit undo");
            player.sendMessage(ChatColor.AQUA + "タイトルを変更する-/bookedit title <変更するタイトル>");
            player.sendMessage(ChatColor.AQUA + "著者を変更する(OP必須)-/bookedit author <変更する著者>");
            return true;
        }

        String subcommand = strings[0];
        BookMeta meta = (BookMeta) player.getInventory().getItemInMainHand().getItemMeta();
        Sound anvil = Sound.BLOCK_ANVIL_USE;

        if (!player.isOp() && !meta.getAuthor().equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "他のプレイヤーが署名したものを編集する場合はOPが必要です");
            return true;
        }

        if (subcommand.equals("undo")) {
            if (mainitem.getAmount() != 1) {
                player.sendMessage(ChatColor.RED + "このコマンドは記入済みの本が一つの場合のみ実行可能です");
                return true;
            }
            ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
            book.setItemMeta(meta);
            player.getInventory().setItemInMainHand(book);
            player.updateInventory();
            player.sendMessage(ChatColor.GREEN + "署名をする前に戻しました");
            player.playSound(player.getLocation(), anvil, 1, 1);
        } else {
            if (strings.length < 2) {
                player.sendMessage(ChatColor.RED + "/bookedit title(またはauthor) <設定する文字>");
                return true;
            }

            String setting_text = strings[1];
            for (int i = 2; i < strings.length; i++) {
                setting_text += " " + strings[i];
            }
            if (subcommand.equals("title")) {
                meta.setTitle(setting_text);
            } else if (subcommand.equals("author")) {
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "このコマンドはOPのみ実行可能です");
                    return true;
                }
                meta.setAuthor(setting_text);
            }
            mainitem.setItemMeta(meta);
            player.playSound(player.getLocation(), anvil, 1, 1);
            player.sendMessage(ChatColor.GREEN+"変更しました");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Tab補完候補の生成ロジックを実装する
        if (args.length == 1) {
            // 第1引数のTab補完候補を生成する処理
            return Arrays.asList("undo", "title", "author");
        }
        return Collections.emptyList();
    }
}