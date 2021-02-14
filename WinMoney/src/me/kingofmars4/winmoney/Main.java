package me.kingofmars4.winmoney;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener{
	
	private static Economy econ = null;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		if (!setupEconomy() ) {
			 getLogger().info(String.format("Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
	}

	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	@EventHandler
	public void openDoor(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			List<Material> doors = new ArrayList<Material>();
			doors.add(Material.WOODEN_DOOR);
			doors.add(Material.ACACIA_DOOR);
			doors.add(Material.JUNGLE_DOOR);
			doors.add(Material.BIRCH_DOOR);
			doors.add(Material.DARK_OAK_DOOR);
			doors.add(Material.SPRUCE_DOOR);

			if (doors.contains(e.getClickedBlock().getType())) {
				EconomyResponse r = econ.depositPlayer(e.getPlayer(), rand(0, 100));
				if(r.transactionSuccess()) {
	                e.getPlayer().sendMessage("You just received: "+econ.format(r.amount));
	            } else {
	                e.getPlayer().sendMessage("An error occured: "+ r.errorMessage);
	            }
			}
		}
	}
	
	
	public int rand(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
