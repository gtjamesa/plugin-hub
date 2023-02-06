package tictac7x.charges;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
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
	private final boolean debug = false;

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
			new W_Arclight(client, client_thread, configs, items, infoboxes, config, this),
			new W_TridentOfTheSeas(client, client_thread, configs, items, infoboxes, config, this),
			new W_SkullSceptre(client, client_thread, configs, items, infoboxes, config, this),
			new W_IbanStaff(client, client_thread, configs, items, infoboxes, config, this),
			new W_PharaohSceptre(client, client_thread, configs, items, infoboxes, config, this),
			new W_BryophytaStaff(client, client_thread, configs, items, infoboxes, config, this),

			new S_CrystalShield(client, client_thread, configs, items, infoboxes, config, this),
			new S_FaladorShield(client, client_thread, configs, items, infoboxes, config, this),
			new S_Chronicle(client, client_thread, configs, items, infoboxes, config, this),

			new J_ExpeditiousBracelet(client, client_thread, configs, items, infoboxes, config, this),
			new J_BraceletOfSlaughter(client, client_thread, configs, items, infoboxes, config, this),
			new J_XericTalisman(client, client_thread, configs, items, infoboxes, config, this),
			new J_SlayerRing(client, client_thread, configs, items, infoboxes, config, this),

			new U_BottomlessCompostBucket(client, client_thread, configs, items, infoboxes, config, this),
			new U_AshSanctifier(client, client_thread, configs, items, infoboxes, config, this),
			new U_BoneCrusher(client, client_thread, configs, items, infoboxes, config, this),
			new U_GricollerCan(client, client_thread, configs, items, infoboxes, config, this),
			new U_SoulBearer(client, client_thread, configs, items, infoboxes, config, this),

			new BarrowsAhrimHood(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsAhrimRobetop(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsAhrimRobeskirt(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsAhrimStaff(client, client_thread, configs, items, infoboxes, config, this),

			new BarrowsDharokHelm(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsDharokPlatebody(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsDharokPlatelegs(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsDharokGreataxe(client, client_thread, configs, items, infoboxes, config, this),

			new BarrowsGuthanHelm(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsGuthanPlatebody(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsGuthanChainskirt(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsGuthanWarspear(client, client_thread, configs, items, infoboxes, config, this),

			new BarrowsKarilCoif(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsKarilLeathertop(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsKarilLeatherskirt(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsKarilCrossbow(client, client_thread, configs, items, infoboxes, config, this),

			new BarrowsToragHelm(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsToragPlatebody(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsToragPlatelegs(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsToragHammers(client, client_thread, configs, items, infoboxes, config, this),

			new BarrowsVeracHelm(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsVeracBrassard(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsVeracPlateskirt(client, client_thread, configs, items, infoboxes, config, this),
			new BarrowsVeracFlail(client, client_thread, configs, items, infoboxes, config, this),
		};
		overlay_charged_items = new ChargedItemsOverlay(items, config, infoboxes_charged_items);

		overlays.add(overlay_charged_items);
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infoboxes.addInfoBox(infobox));
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay_charged_items);
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infoboxes.removeInfoBox(infobox));
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
		if (debug) {
			System.out.println("MESSAGE | " +
				"type: " + event.getType().name() +
				", message: " + event.getMessage().replaceAll("</?col.*?>", "") +
				", sender: " + event.getSender()
			);
		}
	}

	@Subscribe
	public void onAnimationChanged(final AnimationChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onAnimationChanged(event));
		if (debug && event.getActor() == client.getLocalPlayer()) {
			System.out.println("ANIMATION | " +
				"id: " + event.getActor().getAnimation()
			);
		}
	}

	@Subscribe
	public void onGraphicChanged(final GraphicChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onGraphicChanged(event));
		if (debug && event.getActor() == client.getLocalPlayer()) {
			System.out.println("GRAPHIC | " +
				"id: " + event.getActor().getGraphic()
			);
		}
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onConfigChanged(event));
		if (debug && event.getGroup().equals(ChargesImprovedConfig.group)) {
			System.out.println("CONFIG | " +
				"key: " + event.getKey() +
				", old value: " + event.getOldValue() +
				", new value: " + event.getNewValue()
			);
		}
	}

	@Subscribe
	public void onHitsplatApplied(final HitsplatApplied event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onHitsplatApplied(event));
		if (debug) {
			System.out.println("HITSPLAT | " +
				"actor: " + (event.getActor() == client.getLocalPlayer() ? "self" : "enemy") +
				", type: " + event.getHitsplat().getHitsplatType() +
				", amount:" + event.getHitsplat().getAmount() +
				", others = " + event.getHitsplat().isOthers() +
				", mine = " + event.getHitsplat().isMine()
			);
		}
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onWidgetLoaded(event));
		if (debug) {
			System.out.println("WIDGET | " +
				"group: " + event.getGroupId()
			);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onMenuOptionClicked(event));
		if (debug) {
			System.out.println("OPTION | " +
				"option: " + event.getMenuOption() +
				", target: " + event.getMenuTarget() +
				", action name: " + event.getMenuAction().name() +
				", action id: " + event.getMenuAction().getId()
			);
		}
	}

	public static String getChargesMinified(final int charges) {
		if (charges == -1) return "?";
		if (charges > 1000000) return charges / 1000000 + "M";
		if (charges > 1000) return charges / 1000 + "K";
		return String.valueOf(charges);
	}
}

