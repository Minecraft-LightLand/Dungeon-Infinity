package dev.xkmc.dungeon_infinity.init;

import dev.xkmc.dungeon_infinity.content.block.merchant.MerchantOverlay;
import dev.xkmc.dungeon_infinity.content.map.MapOverlay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(value = Dist.CLIENT, modid = DungeonInfinity.MODID)
public class DIClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public static void registerOverlay(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, DungeonInfinity.loc("merchant"), new MerchantOverlay());
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, DungeonInfinity.loc("minimap"), new MapOverlay());
	}

}
