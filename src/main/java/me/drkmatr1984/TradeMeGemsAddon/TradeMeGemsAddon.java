package me.drkmatr1984.TradeMeGemsAddon;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import me.Zrips.TradeMe.TradeMe;
import me.Zrips.TradeMe.Containers.AmountClickAction;
import me.Zrips.TradeMe.Containers.TradeAction;
import me.drkmatr1984.MinevoltGems.GemsAPI;
import me.drkmatr1984.MinevoltGems.MinevoltGems;
import me.elementalgaming.ElementalGems.GemAPI;

public class TradeMeGemsAddon extends JavaPlugin {
	
    private static String gemsType = null;
    

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
    	  if(gemsType != null) {
    		  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.RED + "You can only have one Gems Provider at a time! DISABLING.");
    	      this.setEnabled(false);
    	      return;
    	  }else {
    		  gemsType = "elemental";  	  
        	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.GREEN + "Found ElementalGems as Gems Provider!");	  
    	  } 
      }else if(Bukkit.getServer().getPluginManager().isPluginEnabled("UGems")) {
    	  if(gemsType != null) {
    		  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.RED + "You can only have one Gems Provider at a time! DISABLING.");
    		  this.setEnabled(false);
        	  return;
    	  }else {
    		  gemsType = "ugems";
        	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.GREEN + "Found UGems (UltimateGems) as Gems Provider!");	  
    	  } 
      }else {
    	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.RED + "No Gems plugin found. (ElementalGems, MinevoltGems, or UGems)");
    	  Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[TradeMeGemsAddon] " + ChatColor.RED + "Shutting down...");
    	  this.setEnabled(false);
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
      }else if (gemsType == "elemental"){
    	  if(!GemAPI.playerExists(id))
    		  GemAPI.registerNewPlayer(id);
    	  return (int)GemAPI.getGems(id);
      }else if (gemsType == "ugems") {
    	  OfflinePlayer op = Bukkit.getOfflinePlayer(id);
    	  if(!me.ulrich.gems.api.GemsAPI.getInstance().isAccount(op))
    		  me.ulrich.gems.api.GemsAPI.getInstance().createAccount(op);
          return me.ulrich.gems.api.GemsAPI.getInstance().getGems(op);
      }
      return 0;
    }
    
    public static double getGemBalanceDouble(UUID id) {
        if(gemsType == "minevolt") {
      	  if(!GemsAPI.isRegistered(id))
      		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
      	  return GemsAPI.getGems(id);
        }else if (gemsType == "elemental"){
        	if(!GemAPI.playerExists(id))
      		  GemAPI.registerNewPlayer(id);
      	  return (int)GemAPI.getGems(id);
        }else if (gemsType == "ugems") {
        	OfflinePlayer op = Bukkit.getOfflinePlayer(id);
        	if(!me.ulrich.gems.api.GemsAPI.getInstance().isAccount(op))
      		  me.ulrich.gems.api.GemsAPI.getInstance().createAccount(op);
            return me.ulrich.gems.api.GemsAPI.getInstance().getGems(op);
        }
        return 0;
      }
    
    public static void setGems(UUID id, int amount) {
    	if(gemsType == "minevolt") {
      	  if(!GemsAPI.isRegistered(id))
      		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
      	  GemsAPI.setGems(id, amount);
    	}else if (gemsType == "elemental"){
    		if(!GemAPI.playerExists(id))
      		  GemAPI.registerNewPlayer(id);
      	  GemAPI.setGems(id, amount);
        }else if (gemsType == "ugems") {
        	OfflinePlayer op = Bukkit.getOfflinePlayer(id);
        	if(!me.ulrich.gems.api.GemsAPI.getInstance().isAccount(op))
      		  me.ulrich.gems.api.GemsAPI.getInstance().createAccount(op);
            me.ulrich.gems.api.GemsAPI.getInstance().setGems(op, amount);
        }	
    }
    
    public static boolean takeGems(UUID id, int amount) {
    	if(gemsType == "minevolt") {
      	  if(!GemsAPI.isRegistered(id))
      		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
      	  return GemsAPI.removeGems(id, amount);
    	}else if (gemsType == "elemental"){
    		if(!GemAPI.playerExists(id))
      		  GemAPI.registerNewPlayer(id);
      	  if(GemAPI.getGems(id) - amount > 0) {
      		GemAPI.removeGems(id, amount);
        	  return true;
      	  }	  
        }else if (gemsType == "ugems") {
        	OfflinePlayer op = Bukkit.getOfflinePlayer(id);
        	if(!me.ulrich.gems.api.GemsAPI.getInstance().isAccount(op))
        		  me.ulrich.gems.api.GemsAPI.getInstance().createAccount(op);
            return me.ulrich.gems.api.GemsAPI.getInstance().removeGems(op, amount);
        }
    	return false;
    }
    
    public static void giveGems(UUID id, int amount) {
    	if(gemsType == "minevolt") {
      	  if(!GemsAPI.isRegistered(id))
      		  GemsAPI.register(id, MinevoltGems.getConfigInstance().startAmount);
      	  GemsAPI.addGems(id, amount);
    	}else if (gemsType == "elemental"){
    		if(!GemAPI.playerExists(id))
      		  GemAPI.registerNewPlayer(id);
      	  GemAPI.addGems(id, amount); 
        }else if (gemsType == "ugems") {
        	OfflinePlayer op = Bukkit.getOfflinePlayer(id);
        	if(!me.ulrich.gems.api.GemsAPI.getInstance().isAccount(op))
        		  me.ulrich.gems.api.GemsAPI.getInstance().createAccount(op);
            me.ulrich.gems.api.GemsAPI.getInstance().addGems(op, amount);
        }
    }
    
    public static String getCurrencyName() {
    	if(gemsType == "minevolt")
      	    return MinevoltGems.getLangInstance().currencyName;
    	if (gemsType == "elemental")
      	    return "Gems"; 
        if (gemsType == "ugems") {
        	return "Gems";
        }
        return "Gems";
    }
}
