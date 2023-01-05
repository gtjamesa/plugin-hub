package tictac7x.charges;

import javax.inject.Inject;

import net.runelite.api.*;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.infoboxes.*;

import java.util.Arrays;

@Slf4j
@PluginDescriptor(
	name = "Item Charges Improved",
	description = "Show charges of various items",
	tags = { "charges" }
)
public class ChargesImprovedPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ClientThread client_thread;

	@Inject
	private ItemManager items;

	@Inject
	private ConfigManager configs;

	@Inject
	private InfoBoxManager infoboxes;

	@Inject
	private OverlayManager overlays;

	@Inject
	private ChargesImprovedConfig config;

	@Provides
	ChargesImprovedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChargesImprovedConfig.class);
	}

	private ChargedItemsOverlay overlay_charged_items;

	private ChargedItemInfoBox[] infoboxes_charged_items;

	@Override
	protected void startUp() {
		infoboxes_charged_items = new ChargedItemInfoBox[]{
			new Arclight(client, client_thread, configs, items, this),
			new CrystalShield(client, client_thread, configs, items, this),

			new AhrimHood(client, client_thread, configs, items, this),
			new AhrimRobetop(  client, client_thread, configs, items, this),
			new AhrimRobeskirt(client, client_thread, configs, items, this),
			new AhrimStaff(client, client_thread, configs, items, this),

			new DharokHelm(client, client_thread, configs, items, this),
			new DharokPlatebody(  client, client_thread, configs, items, this),
			new DharokPlatelegs(client, client_thread, configs, items, this),
			new DharokGreataxe(client, client_thread, configs, items, this),

			new GuthanHelm(client, client_thread, configs, items, this),
			new GuthanPlatebody(  client, client_thread, configs, items, this),
			new GuthanChainskirt(client, client_thread, configs, items, this),
			new GuthanWarspear(client, client_thread, configs, items, this),

			new KarilCoif(client, client_thread, configs, items, this),
			new KarilLeathertop(  client, client_thread, configs, items, this),
			new KarilLeatherskirt(client, client_thread, configs, items, this),
			new KarilCrossbow(client, client_thread, configs, items, this),

			new ToragHelm(client, client_thread, configs, items, this),
			new ToragPlatebody(  client, client_thread, configs, items, this),
			new ToragPlatelegs(client, client_thread, configs, items, this),
			new ToragHammers(client, client_thread, configs, items, this),

			new VeracHelm(client, client_thread, configs, items, this),
			new VeracBrassard(  client, client_thread, configs, items, this),
			new VeracPlateskirt(client, client_thread, configs, items, this),
			new VeracFlail(client, client_thread, configs, items, this),
		};
		overlay_charged_items = new ChargedItemsOverlay(infoboxes_charged_items);

		overlays.add(overlay_charged_items);
		for (final ChargedItemInfoBox infobox : infoboxes_charged_items) {
			infoboxes.addInfoBox(infobox);
		}
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay_charged_items);
		for (final ChargedItemInfoBox infobox : infoboxes_charged_items) {
			infoboxes.removeInfoBox(infobox);
		}
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		if (
			event.getContainerId() == InventoryID.INVENTORY.getId() ||
			event.getContainerId() == InventoryID.EQUIPMENT.getId()
		) {
			final ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
			final ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);

			if (inventory != null && equipment != null) {
				Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onItemContainersChanged(inventory, equipment));
			}
		}
	}

	@Subscribe
	public void onChatMessage(final ChatMessage event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onChatMessage(event));
	}

	@Subscribe
	public void onAnimationChanged(final AnimationChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onAnimationChanged(event));
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onConfigChanged(event));
	}

	@Subscribe
	public void onHitsplatApplied(final HitsplatApplied event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onHitsplatApplied(event));
	}
}

