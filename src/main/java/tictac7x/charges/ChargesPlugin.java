package tictac7x.charges;

import javax.inject.Inject;

import net.runelite.api.*;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@Slf4j
@PluginDescriptor(
	name = "Item Charges Improved",
	description = "Show charges of various items",
	tags = { "charges" }
)
public class ChargesPlugin extends Plugin {
	public static final int SLOTS_INVENTORY = 28;
	public static final int SLOTS_EQUIPMENT = 14;

	@Inject
	private Client client;

	@Inject
	private ItemManager items;

	@Inject
	private InfoBoxManager infoboxes;

	@Inject
	private ChargesConfig config;

	@Provides
	ChargesConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChargesConfig.class);
	}

	private ChargesItems charges_items;
	private ChargesInfoBox[] infoboxes_inventory;
	private ChargesInfoBox[] infoboxes_equipment;

	@Override
	protected void startUp() {
		charges_items = new ChargesItems(items, infoboxes);

		// Prepare infoboxes arrays.
		infoboxes_inventory = new ChargesInfoBox[SLOTS_INVENTORY];
		infoboxes_equipment = new ChargesInfoBox[SLOTS_EQUIPMENT];

		// Equipment infoboxes.
		infoboxes_equipment[ 0] = new ChargesInfoBox("equipment_head",   this);
		infoboxes_equipment[ 1] = new ChargesInfoBox("equipment_cape",   this);
		infoboxes_equipment[ 2] = new ChargesInfoBox("equipment_neck",   this);
		infoboxes_equipment[ 3] = new ChargesInfoBox("equipment_weapon", this);
		infoboxes_equipment[ 4] = new ChargesInfoBox("equipment_body",   this);
		infoboxes_equipment[ 5] = new ChargesInfoBox("equipment_shield", this);
		infoboxes_equipment[ 6] = new ChargesInfoBox("empty_1",          this);
		infoboxes_equipment[ 7] = new ChargesInfoBox("equipment_legs",   this);
		infoboxes_equipment[ 8] = new ChargesInfoBox("empty_2",          this);
		infoboxes_equipment[ 9] = new ChargesInfoBox("equipment_gloves", this);
		infoboxes_equipment[10] = new ChargesInfoBox("equipment_boots",  this);
		infoboxes_equipment[11] = new ChargesInfoBox("empty_3",          this);
		infoboxes_equipment[12] = new ChargesInfoBox("equipment_ring",   this);
		infoboxes_equipment[13] = new ChargesInfoBox("equipment_cape",   this);

		// Inventory infoboxes.
		for (int i = 0; i < SLOTS_INVENTORY; i++) {
			infoboxes_inventory[i] = new ChargesInfoBox("inventory_" + (i + 1), this);
		}

		// Add all infoboxes to overlay.
		for (final InfoBox infobox : infoboxes_inventory) infoboxes.addInfoBox(infobox);
		for (final InfoBox infobox : infoboxes_equipment) infoboxes.addInfoBox(infobox);
	}

	@Override
	protected void shutDown() {
		// Remove all infoboxes from overlay.
		for (final InfoBox infobox : infoboxes_inventory) infoboxes.removeInfoBox(infobox);
		for (final InfoBox infobox : infoboxes_equipment) infoboxes.removeInfoBox(infobox);

	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		if (event.getContainerId() == InventoryID.INVENTORY.getId() || event.getContainerId() == InventoryID.EQUIPMENT.getId()) {
			charges_items.updateInfoboxes(
					client.getItemContainer(InventoryID.INVENTORY),
					client.getItemContainer(InventoryID.EQUIPMENT),
					infoboxes_inventory,
					infoboxes_equipment
			);
		}
	}
}

