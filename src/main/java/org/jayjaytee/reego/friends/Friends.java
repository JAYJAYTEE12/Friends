package org.jayjaytee.reego.friends;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jayjaytee.reego.friends.commands.FriendCommand;
import org.jayjaytee.reego.friends.managers.FileManager;

import java.io.File;

public final class Friends extends JavaPlugin {

    private File friendsFile = new File(getDataFolder(), "players.yml");
    private FileConfiguration friendsConfig = YamlConfiguration.loadConfiguration(friendsFile);

    private final FileManager fileManager = new FileManager(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(!friendsFile.exists()) { saveResource("players.yml", false); }

        fileManager.saveFriendsLoop();
        registerClasses();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        fileManager.saveFriends();
    }

    private void registerClasses(){
        new FriendCommand(this);
    }

    public File getFriendsFile() { return friendsFile; }
    public FileConfiguration getFriendsConfig() { return friendsConfig; }
    public FileManager getFileManager() { return fileManager; }

}
