package com.github.ringoame196.bookedit;

import com.github.ringoame196.bookedit.Commands.bookedit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BookEdit extends JavaPlugin {
    private static JavaPlugin plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();

        getCommand("bookedit").setExecutor(new bookedit());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
    }
    public static JavaPlugin getPlugin(){return plugin;}
}
