package dev.xkmc.dungeon_infinity.init.data;

import dev.xkmc.l2core.serial.advancements.IAdvBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.List;

public record RewardBuilder(int exp, ResourceKey<LootTable> loot) implements IAdvBuilder {

	public void onBuild(String id, Advancement.Builder builder, List<ICondition> conditions) {
		builder.rewards(AdvancementRewards.Builder.loot(this.loot).addExperience(this.exp).build());
	}

}
