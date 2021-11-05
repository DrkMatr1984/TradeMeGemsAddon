package me.drkmatr1984.TradeMeGemsAddon;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.Zrips.TradeMe.TradeMe;
import me.Zrips.TradeMe.Containers.AmountClickAction;
import me.Zrips.TradeMe.Containers.TradeAction;
import me.drkmatr1984.MinevoltGems.GemsAPI;
import me.drkmatr1984.MinevoltGems.MinevoltGems;
import me.elementalgaming.ElementalGems.GemAPI;

public class TradeMeGemsAddon extends JavaPlugin {
	
    private static String gemsType;

    @Override
    public void onEnable() {
      if(!Bukkit.getServer().getPluginManager().isPluginEnabled("TradeMe")) {
    	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.RED + "TradeMe not found. Shutting down...");
    	  return;
      }
    	  
      if(Bukkit.getServer().getPluginManager().isPluginEnabled("MinevoltGems")) {
    	  gemsType = "minevolt";
    	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.GREEN + "Found MinevoltGems as Gems Provider!");
      }else if(Bukkit.getServer().getPluginManager().isPluginEnabled("ElementalGems")) {
    	  gemsType = "elemental";
    	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.GREEN + "Found ElementalGems as Gems Provider!");
      }else {
    	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.RED + "No Gems plugin found. (ElementalGems or MinevoltGems)");
    	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.RED + "Shutting down...");
    	  return;
      }
      
    	
      TradeAction tradeAction = new TradeAction("Gems", AmountClickAction.Amounts, false);	
	  TradeMe.getInstance().addNewTradeMode(tradeAction, new Gems(TradeMe.getInstance(), "Gems"));
	
	  // Reloads TradeMe config files to implement new trade mode
	  TradeMe.getInstance().getConfigManager().reload();
	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.GOLD + "Injected Gems Trades");
    }
    
    public static int getGemBalance(UUID id) {
      if(gemsType == "minevolt") {
    	  if(!GemsAPI.isRegistered(id))
    		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
    	  return GemsAPI.getGems(id);
      }else {
    	  if(!GemAPI.playerExists(id))
    		  GemAPI.setGems(id, 0);
    	  return (int)GemAPI.getGems(id);
      }	      
    }
    
    public static double getGemBalanceDouble(UUID id) {
        if(gemsType == "minevolt") {
      	  if(!GemsAPI.isRegistered(id))
      		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
      	  return GemsAPI.getGems(id);
        }else {
      	  if(!GemAPI.playerExists(id))
      		  GemAPI.setGems(id, 0);
      	  return (int)GemAPI.getGems(id);
        }	      
      }
    
    public static void setGems(UUID id, int amount) {
    	if(gemsType == "minevolt") {
      	  if(!GemsAPI.isRegistered(id))
      		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
      	  GemsAPI.setGems(id, amount);
        }else {
      	  if(!GemAPI.playerExists(id))
      		  GemAPI.setGems(id, 0);
      	  GemAPI.setGems(id, amount);
        }	
    }
    
    public static boolean takeGems(UUID id, int amount) {
    	if(gemsType == "minevolt") {
      	  if(!GemsAPI.isRegistered(id))
      		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
      	  return GemsAPI.removeGems(id, amount);
        }else {
      	  if(!GemAPI.playerExists(id))
      		  GemAPI.setGems(id, 0);
      	  if(GemAPI.getGems(id) - amount > 0) {
      		GemAPI.removeGems(id, amount);
        	  return true;
      	  }	  
        }
    	return false;
    }
    
    public static void giveGems(UUID id, int amount) {
    	if(gemsType == "minevolt") {
      	  if(!GemsAPI.isRegistered(id))
      		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
      	  GemsAPI.addGems(id, amount);
        }else {
      	  if(!GemAPI.playerExists(id))
      		  GemAPI.setGems(id, 0);
      	  GemAPI.addGems(id, amount); 
        }
    }
}
