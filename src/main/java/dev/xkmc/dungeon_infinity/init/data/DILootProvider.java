package dev.xkmc.dungeon_infinity.init.data;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.dungeon_infinity.compat.MazeRoomLootGen;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2core.serial.loot.LootTableTemplate;
import dev.xkmc.modulargolems.content.item.data.GolemHolderMaterial;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.List;

public class DILootProvider {


	public static final ResourceKey<LootTable> ENTRY = loot("advancement/entry");

	private static ResourceKey<LootTable> loot(String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, DungeonInfinity.loc(path));
	}

	private static LootPoolEntryContainer.Builder<?> mineable(RegistrateLootTableProvider pvd, Item item, Block... blocks) {
		var blockReg = pvd.getProvider().lookup(Registries.BLOCK).orElseThrow();
		return LootItem.lootTableItem(item).apply(SetComponentsFunction.setComponent(DataComponents.CAN_BREAK, new AdventureModePredicate(List.of(
				BlockPredicate.Builder.block().of(blockReg, blocks).build()
		))));
	}

	private static LootPoolEntryContainer.Builder<?> mineable(RegistrateLootTableProvider pvd, Item item, TagKey<Block> blocks) {
		var blockReg = pvd.getProvider().lookup(Registries.BLOCK).orElseThrow();
		return LootItem.lootTableItem(item).apply(SetComponentsFunction.setComponent(DataComponents.CAN_BREAK, new AdventureModePredicate(List.of(
				BlockPredicate.Builder.block().of(blockReg, blocks).build()
		))));
	}

	public static LootPoolEntryContainer.Builder<?> getGolem(GolemHolder<?, ?> holder, Identifier rl) {
		ArrayList<GolemHolderMaterial.Entry> mats = new ArrayList<>();
		for (var part : holder.getEntityType().values()) {
			mats.add(new GolemHolderMaterial.Entry(part.toItem(), rl));
		}
		return LootItem.lootTableItem(holder).apply(SetComponentsFunction.setComponent(GolemItems.HOLDER_MAT.get(), new GolemHolderMaterial(mats)));
	}

	public static void genLoot(RegistrateLootTableProvider pvd) {
		MazeRoomLootGen.genLoot(pvd);

		pvd.addLootAction(LootContextParamSets.ADVANCEMENT_REWARD, sub -> sub.accept(ENTRY, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(GolemItems.TABLE.asItem())))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(GolemItems.OMNI_COMMAND.asItem())))
				.withPool(LootPool.lootPool().add(getGolem(GolemItems.HOLDER_GOLEM.get(), ModularGolems.loc("iron"))))
				.withPool(LootPool.lootPool().add(mineable(pvd, Items.IRON_PICKAXE, Blocks.RAW_COPPER_BLOCK, Blocks.RAW_IRON_BLOCK)))
				.withPool(LootPool.lootPool().add(mineable(pvd, Items.IRON_AXE, BlockTags.LOGS)))
				.withPool(LootPool.lootPool().add(mineable(pvd, Items.IRON_AXE, Blocks.CLAY)))
				.withPool(LootPool.lootPool().add( mineable(pvd, Items.IRON_SWORD, Blocks.COBWEB)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.IRON_HELMET)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.IRON_CHESTPLATE)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.IRON_LEGGINGS)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.IRON_BOOTS)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.SHIELD)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.BOW)))
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(Items.ARROW, 64)))
		));
	}

}
