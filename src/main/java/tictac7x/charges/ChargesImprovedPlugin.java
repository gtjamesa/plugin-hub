package tictac7x.charges;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.infoboxes.*;
import tictac7x.charges.infoboxes.barrows.AhrimsHood;
import tictac7x.charges.infoboxes.barrows.AhrimsRobeskirt;
import tictac7x.charges.infoboxes.barrows.AhrimsRobetop;
import tictac7x.charges.infoboxes.barrows.AhrimsStaff;
import tictac7x.charges.infoboxes.barrows.DharoksGreataxe;
import tictac7x.charges.infoboxes.barrows.DharoksHelm;
import tictac7x.charges.infoboxes.barrows.DharoksPlatebody;
import tictac7x.charges.infoboxes.barrows.DharoksPlatelegs;
import tictac7x.charges.infoboxes.barrows.GuthansChainskirt;
import tictac7x.charges.infoboxes.barrows.GuthansHelm;
import tictac7x.charges.infoboxes.barrows.GuthansPlatebody;
import tictac7x.charges.infoboxes.barrows.GuthansWarspear;
import tictac7x.charges.infoboxes.barrows.KarilsCoif;
import tictac7x.charges.infoboxes.barrows.KarilsCrossbow;
import tictac7x.charges.infoboxes.barrows.KarilsLeatherskirt;
import tictac7x.charges.infoboxes.barrows.KarilsLeathertop;
import tictac7x.charges.infoboxes.barrows.ToragsHammers;
import tictac7x.charges.infoboxes.barrows.ToragsHelm;
import tictac7x.charges.infoboxes.barrows.ToragsPlatebody;
import tictac7x.charges.infoboxes.barrows.ToragsPlatelegs;
import tictac7x.charges.infoboxes.barrows.VeracsBrassard;
import tictac7x.charges.infoboxes.barrows.VeracsFlail;
import tictac7x.charges.infoboxes.barrows.VeracsHelm;
import tictac7x.charges.infoboxes.barrows.VeracsPlateskirt;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@PluginDescriptor(
	name = "Item Charges Improved",
	description = "Show charges of various items",
	tags = {
		"charges",
		"barrows",
		"bracelet",
		"ring",
		"xeric",
		"talisman",
		"book",
		"chronicle",
		"shield",
		"ash",
		"bone",
		"bottomless",
		"bucket",
		"fish",
		"gricoller",
		"can",
		"soul",
		"arclight",
		"bryophyta",
		"staff",
		"iban",
		"pharaoh",
		"sceptre",
		"skull",
		"sanguinesti",
		"trident",
		"dragonfire",
		"circlet",
		"camulet"
	}
)
public class ChargesImprovedPlugin extends Plugin {
	private final String plugin_version = "v0.3.2";
	private final String plugin_message = "" +
		"<colHIGHLIGHT>Item Charges Improved " + plugin_version + ":<br>" +
		"<colHIGHLIGHT>* Gem bag added.<br>" +
		"<colHIGHLIGHT>* Seed box added.<br>" +
		"<colHIGHLIGHT>* Ring of shadows added.<br>" +
		"<colHIGHLIGHT>* Crystal armor added.<br>" +
		"<colHIGHLIGHT>* Crystal halberd added.";

	private final int VARBIT_MINUTES = 8354;

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

	@Inject
	private Notifier notifier;

	@Provides
	ChargesImprovedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChargesImprovedConfig.class);
	}

	private Store store;

	private ChargedItemsOverlay overlay_charged_items;

	private ChargedItem[] infoboxes_charged_items;

	private final ZoneId timezone = ZoneId.of("Europe/London");

	@Override
	protected void startUp() {
		store = new Store();

		infoboxes_charged_items = new ChargedItem[]{
			// Weapons
			new W_Arclight(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_TridentOfTheSeas(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_SkullSceptre(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_IbansStaff(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_PharaohsSceptre(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_BryophytasStaff(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_SanguinestiStaff(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_CrystalBow(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_CrystalHalberd(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Shields
			new S_KharedstMemoirs(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_Chronicle(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_CrystalShield(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_FaladorShield(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_DragonfireShield(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Jewellery
			new J_BraceletOfClay(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_BraceletOfExpeditious(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_BraceletOfFlamtaer(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_BraceletOfSlaughter(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_CelestialRing(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_EscapeCrystal(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_RingOfRecoil(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_RingOfSuffering(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_SlayerRing(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_XericsTalisman(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_Camulet(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_RingOfShadows(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Helms
			new H_CircletOfWater(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Capes
			new C_ArdougneCloak(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new C_Coffin(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new C_MagicCape(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Utilities
			new U_AshSanctifier(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_BoneCrusher(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_BottomlessCompostBucket(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_FishBarrel(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_GemBag(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_FungicideSpray(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_GricollersCan(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_SeedBox(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_SoulBearer(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_TeleportCrystal(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_Waterskin(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_OgreBellows(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_LogBasket(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Armour sets
			new A_CrystalBody(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new A_CrystalHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new A_CrystalLegs(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new AhrimsHood(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new AhrimsRobetop(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new AhrimsRobeskirt(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new AhrimsStaff(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new DharoksHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new DharoksPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new DharoksPlatelegs(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new DharoksGreataxe(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new GuthansHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new GuthansPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new GuthansChainskirt(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new GuthansWarspear(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new KarilsCoif(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new KarilsLeathertop(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new KarilsLeatherskirt(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new KarilsCrossbow(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new ToragsHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new ToragsPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new ToragsPlatelegs(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new ToragsHammers(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new VeracsHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new VeracsBrassard(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new VeracsPlateskirt(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new VeracsFlail(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
		};
		overlay_charged_items = new ChargedItemsOverlay(client, config, infoboxes_charged_items);

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
		store.onItemContainerChanged(event);

		for (final ChargedItem infobox : infoboxes_charged_items) {
			infobox.onItemContainersChanged(event);
		}

		store.onInventoryItemsChanged(event);
		store.onBankItemsChanged(event);

//		System.out.println("ITEM CONTAINER | " + event.getContainerId());
//		for (final Item item : event.getItemContainer().getItems()) {
//			System.out.println(item.getId() + ": " + items.getItemComposition(item.getId()).getName() + ", q: " + item.getQuantity());
//		}
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
		store.onMenuOptionClicked(event);

//		System.out.println("MENU OPTION | " +
//			"option: " + event.getMenuOption() +
//			", target: " + event.getMenuTarget() +
//			", action name: " + event.getMenuAction().name() +
//			", action id: " + event.getMenuAction().getId()
//		);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		if (event.getGameState() == GameState.LOGGING_IN) {
			checkForChargesReset();
		}

		if (event.getGameState() != GameState.LOGGED_IN) return;

		// Send message about plugin updates for once.
		if (!config.getVersion().equals(plugin_version)) {
			configs.setConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.version, plugin_version);
			chat_messages.queue(QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(plugin_message)
				.build()
			);
		}
	}

	@Subscribe
	public void onStatChanged(final StatChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onStatChanged(event));

//		System.out.println("STAT CHANGED | " +
//			event.getSkill().getName() +
//			", level: " + event.getLevel() +
//			", xp: " + event.getXp()
//		);
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.onVarbitChanged(event));

		// If server minutes are 0, it's a new day!
		if (event.getVarbitId() == VARBIT_MINUTES && client.getGameState() == GameState.LOGGED_IN && event.getValue() == 0) {
			checkForChargesReset();
		}

//		System.out.println("VARBIT CHANGED | " +
//			"id: " + event.getVarbitId() +
//			", value: " + event.getValue()
//		);
	}

	@Subscribe
	public void onGameTick(final GameTick gametick) {
		store.onGameTick(gametick);
	}

	private void checkForChargesReset() {
		final String date = LocalDateTime.now(timezone).format(DateTimeFormatter.ISO_LOCAL_DATE);
		if (date.equals(config.getResetDate())) return;

		configs.setConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.date, date);
		Arrays.stream(infoboxes_charged_items).forEach(infobox -> infobox.resetCharges());
	}

	public static String getChargesMinified(final int charges) {
		if (charges == Charges.UNLIMITED) return "∞";
		if (charges == Charges.UNKNOWN) return "?";
		if (charges < 1000) return String.valueOf(charges);
		if (charges >= 1000000) return charges / 1000000 + "M";

		final int thousands = charges / 1000;
		final int hundreds = Math.min((charges % 1000 + 50) / 100, 9);

		return thousands + (thousands < 10 && hundreds > 0 ? "." + hundreds : "") + "K";
	}
}

