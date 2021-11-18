package tictac7x.charges;

import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@Slf4j
@PluginDescriptor(
	name = "Item charges",
	description = "Show charges of various items",
	tags = { "charges" }
)
public class ChargesPlugin extends Plugin {
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

	private ChargesInfobox worn_ring;

	@Override
	protected void startUp() {
		worn_ring = new ChargesInfobox(client, infoboxes, items, ItemSlot.EQUIPMENT_RING, this);
		infoboxes.addInfoBox(worn_ring);
	}

	@Override
	protected void shutDown() {
		infoboxes.removeInfoBox(worn_ring);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		worn_ring.onItemContainerChanged(event);
	}
}
