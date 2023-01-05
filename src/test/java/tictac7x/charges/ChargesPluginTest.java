package tictac7x.charges;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ChargesPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(ChargesImprovedPlugin.class);
		RuneLite.main(args);
	}
}