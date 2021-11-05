package me.drkmatr1984.TradeMeGemsAddon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.GUI.*;
import net.Zrips.CMILib.GUI.GUIManager.GUIClickType;
import net.Zrips.CMILib.ActionBar.*;
import net.Zrips.CMILib.Items.CMIMaterial;

import me.Zrips.TradeMe.TradeMe;
import me.Zrips.TradeMe.Containers.Amounts;
import me.Zrips.TradeMe.Containers.OfferButtons;
import me.Zrips.TradeMe.Containers.TradeMap;
import me.Zrips.TradeMe.Containers.TradeModeInterface;
import me.Zrips.TradeMe.Containers.TradeOffer;
import me.Zrips.TradeMe.Containers.TradeResults;
import me.Zrips.TradeMe.Containers.TradeSize;
import me.Zrips.TradeMe.Locale.LC;

public class Gems implements TradeModeInterface {

    private String at = "Gems";

    static List<ItemStack> AmountButtons = new ArrayList<ItemStack>();
    ItemStack OfferedTradeButton = net.Zrips.CMILib.Items.CMIMaterial.EMERALD.newItemStack();
    OfferButtons offerButton = new OfferButtons();
    Amounts amounts = new Amounts(1, 100, 10000, 1000000);
    private TradeMe plugin;

    public Gems(TradeMe plugin, String name) {
    	this.plugin = plugin;
    	at = name;
    }

    @Override
    public HashMap<String, Object> getLocale() {
	HashMap<String, Object> map = new HashMap<String, Object>();
	map.put("Button.Name", "&2Gems increment by &6[amount]");
	map.put("Button.Lore", Arrays.asList(
	    "&eLeft click to add",
	    "&eRight click to take",
	    "&eHold shift to increase 10 times",
	    "&eMaximum available: &6[balance]",
	    "&eCurrent Gems offer: &6[offer] [taxes]"));
	map.put("ToggleButton.Name", "&2Toggle to Gem offer");
	map.put("ToggleButton.Lore", Arrays.asList("&eCurent Gem offer: &6[amount] [taxes]"));
	map.put("OfferedButton.Name", "&2[player]'s Gem offer");
	map.put("OfferedButton.Lore", Arrays.asList("&eCurent Gem offer: &6[amount] [taxes]"));
	map.put("Error", "&e[playername] doesn't have enough Gems!");
	map.put("Limit", "&eYou dont have enough Gems! Amount was set to maximum you can trade: &6[amount]");
	map.put("hardLimit", "&6[playername] &ecan't have more than 10,000,000,000,000 Gems!");
	map.put("InLoanTarget", "&eYour offered Gem amount is to low to get &6[playername] &eout of loan! offer atleast &6[amount]");
	map.put("InLoanYou", "&6[playername] &eoffered Gem amount is to low to get you out of loan!");
	map.put("Got", "&eYou have received &6[amount] &aGems");
	map.put("CantWithdraw", "&cCan't withdraw Gems from player! ([playername])");
	map.put("ChangedOffer", "&6[playername] &ehas changed their Gem offer to: &6[amount]");
	map.put("ChangedOfferTitle", "&8Offered &0[amount] &aGems");

	map.put("log", "&e[amount] &7Gems");
	return map;
    }

    @Override
    public void setAmounts(Amounts amounts) {
	this.amounts = amounts;
    }

    @Override
    public List<ItemStack> getAmountButtons() {
	AmountButtons.add(CMIMaterial.REDSTONE.newItemStack());
	AmountButtons.add(CMIMaterial.LAPIS_LAZULI.newItemStack());
	AmountButtons.add(CMIMaterial.DIAMOND.newItemStack());
	AmountButtons.add(CMIMaterial.EMERALD.newItemStack());
	return AmountButtons;
    }

    @Override
    public ItemStack getOfferedTradeButton() {
	return OfferedTradeButton;
    }

    @Override
    public OfferButtons getOfferButtons() {
	offerButton.addOfferOff(CMIMaterial.COAL.newItemStack());
	offerButton.addOfferOn(CMIMaterial.EMERALD.newItemStack());
	return offerButton;
    }

    @Override
    public void setTrade(TradeOffer trade, int i) {	
	    trade.setP1Money(TradeMeGemsAddon.getGemBalanceDouble(trade.getP1().getUniqueId()));
	    trade.setP2Money(TradeMeGemsAddon.getGemBalanceDouble((trade.getP2().getUniqueId())));
	    trade.getButtonList().add(trade.getPosibleButtons().get(i));
    }

    @Override
    public CMIGui Buttons(final TradeOffer trade, CMIGui GuiInv, final int slot) {

	String firstBalance = plugin.getUtil().TrA((long) trade.getP1Money());
	String firstOffer = plugin.getUtil().TrA(trade.getOffer(at));

	ItemStack ob = trade.getOffer(at) == 0 ? offerButton.getOfferOff() : offerButton.getOfferOn();

	String taxes = plugin.getUtil().GetTaxesString(at, trade.getOffer(at));

	String mid = "";
	if (trade.getButtonList().size() > 4)
	    mid = "\n" + plugin.getMessage("MiddleMouse");
	if (trade.Size == TradeSize.REGULAR)
	    GuiInv.updateButton(new CMIGuiButton(slot, plugin.getUtil().makeSlotItem(ob, plugin.getMessage(at, "ToggleButton.Name"),
		plugin.getMessageListAsString(at, "ToggleButton.Lore",
		    "[amount]", plugin.getUtil().TrA(trade.getOffer(at)),
		    "[taxes]", taxes) + mid)) {
		@Override
		public void click(GUIClickType click) {
		    trade.toogleMode(at, click, slot);
		}
	    });

	if (trade.getAction() == at) {

	    String lore = plugin.getMessageListAsString(at, "Button.Lore",
		"[balance]", firstBalance,
		"[offer]", firstOffer,
		"[taxes]", taxes);
	    for (int i = 45; i < 49; i++) {
		TradeMe.getInstance().d(AmountButtons.get(i - 45).getType());
		GuiInv.updateButton(new CMIGuiButton(i, plugin.getUtil().makeSlotItem(AmountButtons.get(i - 45),
		    plugin.getMessage(at, "Button.Name", "[amount]", plugin.getUtil().TrA(amounts.getAmount(i - 45))), lore)) {

		    @Override
		    public void click(GUIClickType click) {
			trade.amountClick(at, click, this.getSlot() - 45, slot);
		    }
		});
	    }
	}

	return GuiInv;
    }

    @Override
    public void Change(TradeOffer trade, int slot, GUIClickType button) {
	Double amount = amounts.getAmount(slot);
	double playerGems = trade.getP1Money();
	double targetGems = trade.getP2Money();
	double offeredGems = trade.getOffer(at);

	if (button.isShiftClick())
	    amount *= 10;

	if (button.isLeftClick()) {
	    if (offeredGems + amount + targetGems >= 10000000000000D) {
		amount = 10000000000000D - offeredGems - targetGems;
		trade.getP1().sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "hardLimit", "[playername]", trade.getP2Name()));
	    }

	    if (offeredGems + amount > playerGems) {
		if (playerGems < 0)
		    trade.setOffer(at, 0);
		else
		    trade.setOffer(at, Math.floor(playerGems));
		trade.getP1().sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "Limit", "[amount]", plugin.getUtil().TrA(trade.getOffer(at))));
	    } else {
		trade.addOffer(at, amount);
	    }
	}
	if (button.isRightClick())
	    if (offeredGems - amount < 0) {
		trade.setOffer(at, 0);
	    } else {
		trade.takeFromOffer(at, amount);
	    }

	String msg = plugin.getMessage(at, "ChangedOffer", "[playername]", trade.getP1Name(), "[amount]", plugin.getUtil().TrA(trade.getOffer(at)));

	CMIActionBar.send(trade.getP2(), msg);

	TradeMe.getInstance().getUtil().updateInventoryTitle(trade.getP2(), plugin.getMessage(at, "ChangedOfferTitle", "[playername]", trade.getP1().getName(), "[amount]", trade.getOffer(at)), 1000L);

    }

    @Override
    public ItemStack getOfferedItem(TradeOffer trade) {
	if (trade.getOffer(at) > 0) {
	    String taxes = plugin.getUtil().GetTaxesString(at, trade.getOffer(at));
	    ItemStack item = plugin.getUtil().makeSlotItem(OfferedTradeButton,
		plugin.getMessage(at, "OfferedButton.Name",
		    "[player]", trade.getP1().getName()),
		plugin.getMessageListAsString(at, "OfferedButton.Lore",
		    "[amount]", plugin.getUtil().TrA(trade.getOffer(at)),
		    "[taxes]", taxes));
	    return item;
	}
	return null;
    }

    @Override
    public boolean isLegit(TradeMap trade) {
	Player p1 = trade.getP1Trade().getP1();
	Player p2 = trade.getP2Trade().getP1();

	if (check(p1, p2, trade.getP1Trade().getOffer(at), trade.getP2Trade().getOffer(at)))
	    return true;
	return false;
    }

    private boolean check(Player p1, Player p2, Double offer1, Double offer2) {
	if (plugin.getEconomy().enabled()) {
	    long balance = TradeMeGemsAddon.getGemBalance(p1.getUniqueId());
//	    if (balance < 0 && balance + offer2 < 0 && !plugin.EssPresent) {
//		p1.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "InLoanYou", "[playername]", p2.getName()));
//		p2.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "InLoanTarget", "[playername]", p1.getName(), "[amount]",
//		    plugin.getUtil().TrA(-balance)));
//		return false;
//	    }
	    if (balance < offer1) {
		p1.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "Error", "[playername]", p1.getName()));
		p2.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "Error", "[playername]", p1.getName()));
		return false;
	    }

	    balance = TradeMeGemsAddon.getGemBalance(p2.getUniqueId());
//	    if (balance < 0 && balance + offer1 < 0 && !plugin.EssPresent) {
//		p1.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "InLoanYou", "[playername]", p1.getName()));
//		p2.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "InLoanTarget", "[playername]", p2.getName(), "[amount]",
//		    plugin.getUtil().TrA(-balance)));
//		return false;
//	    }
	    if (balance < offer2) {
		p1.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "Error", "[playername]", p2.getName()));
		p2.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "Error", "[playername]", p2.getName()));
		return false;
	    }
	    return true;
	}
	return false;
    }

    @Override
    public boolean finish(TradeOffer trade) {
	Player target = trade.getP2();
	Player source = trade.getP1();

	if (!check(source, target, trade.getOffer(this.at), 0D))
	    return false;
	if (trade.getOffer(this.at) <= 0.0D)
	    return true;

	double amount = trade.getOffer(this.at);
	if (amount < 0)
	    return false;
	if (source != null) {
		if(TradeMeGemsAddon.getGemBalance(source.getUniqueId()) < amount)
		return false;
	    boolean done = TradeMeGemsAddon.takeGems(source.getUniqueId(), ((Double)amount).intValue());
	    if (!done)
		return false;
	}
	double tamount = plugin.getUtil().CheckTaxes(this.at, amount);
	if (tamount < 0) {
	    return false;
	}
	trade.setOffer(this.at, tamount);
	if (target != null) {
		TradeMeGemsAddon.giveGems(target.getUniqueId(), ((Double)tamount).intValue());
	    target.sendMessage(plugin.getMsg(LC.info_prefix) + plugin.getMessage(at, "Got", "[amount]", plugin.getUtil().TrA(trade.getOffer(at))));
	}
	return true;
    }

    @Override
    public void getResults(TradeOffer trade, TradeResults TR) {
	if (trade.getOffer(at) > 0) {
	    double amount = trade.getOffer(at);
	    amount = amount - plugin.getUtil().CheckFixedTaxes(at, amount);
	    amount = amount - plugin.getUtil().CheckPercentageTaxes(at, amount);
	    TR.add(at, amount);
	}
    }

    @Override
    public String Switch(TradeOffer trade, GUIClickType button) {
	return null;
    }
}
