package org.jayjaytee.reego.friends.managers;

import org.jayjaytee.reego.friends.Friends;

import java.io.IOException;

public class FileManager {
    Friends plugin;
    public FileManager(Friends plugin){
        this.plugin = plugin;
    }

    public void saveFriends(){
        try{
            plugin.getFriendsConfig().save(plugin.getFriendsFile());
            plugin.getServer().getConsoleSender().sendMessage("§aSuccessfully saved friends!");
        }catch(IOException e){
            e.printStackTrace();
            plugin.getServer().getConsoleSender().sendMessage("§cThere was an error trying to save friends!");
        }
    }
    public void saveFriendsLoop(){
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                saveFriends();
                saveFriendsLoop();
            }
        }, 20 * 600);
    }
}
