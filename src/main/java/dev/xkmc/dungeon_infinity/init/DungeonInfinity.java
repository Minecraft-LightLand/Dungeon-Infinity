package dev.xkmc.dungeon_infinity.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.dungeon_infinity.content.cap.DefeatRoomPacket;
import dev.xkmc.dungeon_infinity.content.cap.SetRadiusPacket;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.init.data.*;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.dungeon_infinity.init.reg.DIWorldGen;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.ConfigTypeEntry;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2serial.network.PacketHandler;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DungeonInfinity.MODID)
@EventBusSubscriber(modid = DungeonInfinity.MODID)
public class DungeonInfinity {

	public static final String MODID = "dungeon_infinity";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			DungeonInfinity.MODID, 1,
			e -> e.create(DefeatRoomPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(SetRadiusPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);

	public static final ConfigTypeEntry<TemplateConfig> TEMPLATES = new ConfigTypeEntry<>(HANDLER, "templates", TemplateConfig.class);

	public DungeonInfinity(IEventBus bus) {
		DIItems.register();
		DIMeta.register();
		DIWorldGen.register();
		DIConfig.init();

	}

	@SubscribeEvent
	public static void modifyAttributes(EntityAttributeModificationEvent event) {
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent.Client event) {
		REGISTRATE.addDataGenerator(ProviderType.LANG, DILang::genLang);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, DITagGen::genItemTags);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, DITagGen::genBlockTags);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, DILootGen::genLoot);
		var init = REGISTRATE.getDataGenInitializer();
		DIDimensionGen.init(init);
		var gen = event.getGenerator();
		var output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		gen.addProvider(true, new DIConfigGen(gen, pvd));
	}

	public static Identifier loc(String id) {
		return Identifier.fromNamespaceAndPath(MODID, id);
	}

}
