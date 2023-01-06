package tictac7x.charges;

import javax.inject.Inject;

import net.runelite.api.*;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.events.*;
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
			new W_Arclight(client, client_thread, configs, items, this),
			new S_CrystalShield(client, client_thread, configs, items, this),
			new W_TridentOfTheSeas(client, client_thread, configs, items, this),
			new B_ExpeditiousBracelet(client, client_thread, configs, items, this),
			new B_BraceletOfSlaughter(client, client_thread, configs, items, this),

			new BarrowsAhrimHood(client, client_thread, configs, items, this),
			new BarrowsAhrimRobetop(  client, client_thread, configs, items, this),
			new BarrowsAhrimRobeskirt(client, client_thread, configs, items, this),
			new BarrowsAhrimStaff(client, client_thread, configs, items, this),

			new BarrowsDharokHelm(client, client_thread, configs, items, this),
			new BarrowsDharokPlatebody(  client, client_thread, configs, items, this),
			new BarrowsDharokPlatelegs(client, client_thread, configs, items, this),
			new BarrowsDharokGreataxe(client, client_thread, configs, items, this),

			new BarrowsGuthanHelm(client, client_thread, configs, items, this),
			new BarrowsGuthanPlatebody(  client, client_thread, configs, items, this),
			new BarrowsGuthanChainskirt(client, client_thread, configs, items, this),
			new BarrowsGuthanWarspear(client, client_thread, configs, items, this),

			new BarrowsKarilCoif(client, client_thread, configs, items, this),
			new BarrowsKarilLeathertop(  client, client_thread, configs, items, this),
			new BarrowsKarilLeatherskirt(client, client_thread, configs, items, this),
			new BarrowsKarilCrossbow(client, client_thread, configs, items, this),

			new BarrowsToragHelm(client, client_thread, configs, items, this),
			new BarrowsToragPlatebody(  client, client_thread, configs, items, this),
			new BarrowsToragPlatelegs(client, client_thread, configs, items, this),
			new BarrowsToragHammers(client, client_thread, configs, items, this),

			new BarrowsVeracHelm(client, client_thread, configs, items, this),
			new BarrowsVeracBrassard(  client, client_thread, configs, items, this),
			new BarrowsVeracPlateskirt(client, client_thread, configs, items, this),
			new BarrowsVeracFlail(client, client_thread, configs, items, this),
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
		log.debug(event.getType() + " - " + event.getMessage().replaceAll("</?col.*?>", ""));
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

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onWidgetLoaded(event));
	}
}

