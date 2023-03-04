package tictac7x.charges;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.infoboxes.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;

@Slf4j
@PluginDescriptor(
	name = "Item Charges Improved",
	description = "Show charges of various items",
	tags = { "charges" }
)
public class ChargesImprovedPlugin extends Plugin {
	private String plugin_version = "0.2.1";
	private String plugin_message = "" +
			"<colHIGHLIGHT>Item Charges Improved v0.2.1:<br>" +
			"<colHIGHLIGHT>* Correctly detect unused barrows gear charges<br>" +
			"<colHIGHLIGHT>* Daily items reset their charges accordingly";
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

	@Inject
	private ChatMessageManager chat_messages;

	@Provides
	ChargesImprovedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChargesImprovedConfig.class);
	}

	private ChargedItemsOverlay overlay_charged_items;

	private ChargedItemInfoBox[] infoboxes_charged_items;

	private final ZoneId timezone = ZoneId.of("Europe/London");
	private Thread hourlyResetChecker = null;

	@Override
	protected void startUp() {
		infoboxes_charged_items = new ChargedItemInfoBox[]{
			new W_Arclight(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new W_TridentOfTheSeas(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new W_SkullSceptre(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new W_IbanStaff(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new W_PharaohSceptre(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new W_BryophytaStaff(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new S_CrystalShield(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new S_FaladorShield(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new S_Chronicle(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new J_ExpeditiousBracelet(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new J_BraceletOfSlaughter(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new J_XericTalisman(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new J_SlayerRing(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new U_BottomlessCompostBucket(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new U_AshSanctifier(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new U_BoneCrusher(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new U_GricollerCan(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new U_SoulBearer(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new BarrowsAhrimHood(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsAhrimRobetop(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsAhrimRobeskirt(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsAhrimStaff(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new BarrowsDharokHelm(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsDharokPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsDharokPlatelegs(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsDharokGreataxe(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new BarrowsGuthanHelm(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsGuthanPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsGuthanChainskirt(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsGuthanWarspear(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new BarrowsKarilCoif(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsKarilLeathertop(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsKarilLeatherskirt(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsKarilCrossbow(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new BarrowsToragHelm(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsToragPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsToragPlatelegs(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsToragHammers(client, client_thread, configs, items, infoboxes, chat_messages, config, this),

			new BarrowsVeracHelm(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsVeracBrassard(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsVeracPlateskirt(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
			new BarrowsVeracFlail(client, client_thread, configs, items, infoboxes, chat_messages, config, this),
		};
		overlay_charged_items = new ChargedItemsOverlay(items, config, infoboxes_charged_items);

		overlays.add(overlay_charged_items);
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infoboxes.addInfoBox(infobox));

		hourlyResetChecker = checkHourlyReset();
		hourlyResetChecker.start();
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay_charged_items);
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infoboxes.removeInfoBox(infobox));
		hourlyResetChecker.interrupt();
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
//		System.out.println("MESSAGE | " +
//			"type: " + event.getType().name() +
//			", message: " + event.getMessage().replaceAll("</?col.*?>", "") +
//			", sender: " + event.getSender()
//		);
	}

	@Subscribe
	public void onAnimationChanged(final AnimationChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onAnimationChanged(event));
//		if (event.getActor() == client.getLocalPlayer()) {
//			System.out.println("ANIMATION | " +
//				"id: " + event.getActor().getAnimation()
//			);
//		}
	}

	@Subscribe
	public void onGraphicChanged(final GraphicChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onGraphicChanged(event));
//		if (event.getActor() == client.getLocalPlayer()) {
//			System.out.println("GRAPHIC | " +
//				"id: " + event.getActor().getGraphic()
//			);
//		}
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onConfigChanged(event));
//		if (event.getGroup().equals(ChargesImprovedConfig.group)) {
//			System.out.println("CONFIG | " +
//				"key: " + event.getKey() +
//				", old value: " + event.getOldValue() +
//				", new value: " + event.getNewValue()
//			);
//		}
	}

	@Subscribe
	public void onHitsplatApplied(final HitsplatApplied event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onHitsplatApplied(event));
//		System.out.println("HITSPLAT | " +
//			"actor: " + (event.getActor() == client.getLocalPlayer() ? "self" : "enemy") +
//			", type: " + event.getHitsplat().getHitsplatType() +
//			", amount:" + event.getHitsplat().getAmount() +
//			", others = " + event.getHitsplat().isOthers() +
//			", mine = " + event.getHitsplat().isMine()
//		);
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onWidgetLoaded(event));
//		System.out.println("WIDGET | " +
//			"group: " + event.getGroupId()
//		);
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onMenuOptionClicked(event));
//		System.out.println("OPTION | " +
//			"option: " + event.getMenuOption() +
//			", target: " + event.getMenuTarget() +
//			", action name: " + event.getMenuAction().name() +
//			", action id: " + event.getMenuAction().getId()
//		);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		// Send message about plugin updates for once.
		if (event.getGameState() == GameState.LOGGED_IN && !config.getVersion().equals(plugin_version)) {
			configs.setConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.version, plugin_version);
			chat_messages.queue(QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(plugin_message)
				.build()
			);
		}
	}

	public static String getChargesMinified(final int charges) {
		if (charges == -1) return "?";
		if (charges > 1000000) return charges / 1000000 + "M";
		if (charges > 1000) return charges / 1000 + "K";
		return String.valueOf(charges);
	}

	private Thread checkHourlyReset() {
		return new Thread(() -> {
			int day = LocalDateTime.now(timezone).getDayOfMonth();

			while (true) {
				final LocalDateTime date = LocalDateTime.now(timezone);
				// Day changed, check for charges resets.
				if (date.getDayOfMonth() != day) {
					for (final ChargedItemInfoBox infobox : infoboxes_charged_items) {
						infobox.resetCharges();
					}
				}

				// Sleep until next hour + 5 seconds to be sure.
				final int secondsToSleepForNextHour = 60 * (59 - date.getMinute()) + 59 - date.getSecond();
				try {
					Thread.sleep(1000 * (secondsToSleepForNextHour + 5));
				} catch (final Exception ignored) {}
			}
		});
	}
}

