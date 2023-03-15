package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesItem;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerMenuOption;
import tictac7x.charges.triggers.TriggerWidget;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class U_FishBarrel extends ChargedItemInfoBox {
    private final ChargedItemInfoBox infobox;

    private final int FISH_BARREL_SIZE = 28;
    private final int[] RAW_FISHES = new int[]{
        ItemID.RAW_SHRIMPS,
        ItemID.RAW_SARDINE,
        ItemID.RAW_GUPPY,
        ItemID.RAW_HERRING,
        ItemID.RAW_ANCHOVIES,
        ItemID.RAW_MACKEREL,
        ItemID.RAW_TROUT,
        ItemID.RAW_CAVEFISH,
        ItemID.RAW_COD,
        ItemID.RAW_PIKE,
        ItemID.RAW_SLIMY_EEL,
        ItemID.RAW_SALMON,
        ItemID.FROG_SPAWN,
        ItemID.RAW_TETRA,
        ItemID.RAW_TUNA,
        ItemID.RAW_RAINBOW_FISH,
        ItemID.RAW_CAVE_EEL,
        ItemID.RAW_LOBSTER,
        ItemID.BLUEGILL,
        ItemID.RAW_BASS,
        ItemID.RAW_CATFISH,
        ItemID.LEAPING_TROUT,
        ItemID.RAW_SWORDFISH,
        ItemID.RAW_LAVA_EEL,
        ItemID.COMMON_TENCH,
        ItemID.LEAPING_SALMON,
        ItemID.RAW_MONKFISH,
        ItemID.RAW_KARAMBWAN,
        ItemID.LEAPING_STURGEON,
        ItemID.MOTTLED_EEL,
        ItemID.RAW_SHARK,
        ItemID.RAW_SEA_TURTLE,
        ItemID.INFERNAL_EEL,
        ItemID.RAW_MANTA_RAY,
        ItemID.MINNOW,
        ItemID.RAW_ANGLERFISH,
        ItemID.RAW_DARK_CRAB,
        ItemID.SACRED_EEL,
        ItemID.GREATER_SIREN
    };

    public U_FishBarrel(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(ChargesItem.FISH_BARREL, ItemID.FISH_BARREL, client, client_thread, configs, items, infoboxes, chat_messages, config, plugin);
        this.infobox = this;

        this.config_key = ChargesImprovedConfig.fish_barrel;
        this.zero_charges_is_positive = true;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.FISH_BARREL),
            new TriggerItem(ItemID.OPEN_FISH_BARREL)
        };
        this.triggers_menu_options = new TriggerMenuOption[]{
            new TriggerMenuOption("Empty").fixedCharges(0),
            new TriggerMenuOption("Fill").extraConsumer(message -> {
                int charges = infobox.getCharges();

                if (infobox.inventory != null) {
                    for (final Item item : infobox.inventory.getItems()) {
                        if (item.getId() == -1 || item.getQuantity() != 1) continue;
                        if (Arrays.stream(RAW_FISHES).filter(fish -> fish == item.getId()).count() > 0) {
                            charges += 1;
                        }
                    }
                }

                infobox.setCharges(Math.min(charges, FISH_BARREL_SIZE));
            })
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("(You catch .*)|(.* enabled you to catch an extra fish.)").extraConsumer(message -> {
                // Fish barrel is open and is not full.
                if (infobox.item_id == ItemID.OPEN_FISH_BARREL && infobox.getCharges() < FISH_BARREL_SIZE) {
                    infobox.increaseCharges(1);
                }
            })
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("The barrel is empty").fixedCharges(0),
            new TriggerWidget("The barrel contains:").extraConsumer(message -> {
                int charges = 0;

                final Matcher matcher = Pattern.compile(".*?(\\d+)").matcher(message);
                while (matcher.find()) {
                    charges += Integer.parseInt(matcher.group(1));
                }

                infobox.setCharges(charges);
            })
        };
    }
}
