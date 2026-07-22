package dev.xkmc.dungeon_infinity.compat;

import dev.xkmc.dungeon_infinity.content.cap.MobRoomHolder;
import dev.xkmc.dungeon_infinity.content.spawn.MobSpawnTicker;
import dev.xkmc.golemdungeons.content.config.TrialConfig;
import dev.xkmc.golemdungeons.content.spawner.TrialData;
import dev.xkmc.golemdungeons.content.spawner.TrialTicker;
import dev.xkmc.golemdungeons.init.GolemDungeons;
import dev.xkmc.golemdungeons.init.data.GDLang;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SerialClass
public class GolemSpawnTicker implements TrialTicker, MobSpawnTicker {

	@SerialField
	private final TrialData data = new TrialData();
	@SerialField
	public @Nullable Identifier trial = null;
	@SerialField
	public final List<BlockPos> targets = new ArrayList<>();
	@SerialField
	public boolean active = false;
	@SerialField
	public boolean completed = false;
	@SerialField
	public final List<UUID> passive = new ArrayList<>();

	private @Nullable CustomBossEvent bar;

	public CustomBossEvent makeBar() {
		return new CustomBossEvent(UUID.randomUUID(), this.trial, GDLang.fromTrial(this.trial), () -> {
		});
	}

	@Override
	public void addTargetPos(BlockPos pos) {
		targets.add(pos);
	}

	@Override
	public void stop(ServerLevel level, MobRoomHolder ins) {
		active = false;
		data.stop(level, this);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void start(ServerLevel level, MobRoomHolder ins) {
		completed = false;
		active = true;
		if (trial == null) return;
		TrialConfig config = GolemDungeons.TRIAL.getEntry(this.trial);
		if (config == null) return;
		if (this.bar == null) {
			this.bar = this.makeBar();
			this.bar.setPlayers(ins.data.players);
		}
		this.data.start(this, level.getGameTime(), this.trial, config);
	}

	@Override
	public void tick(ServerLevel level, MobRoomHolder ins) {
		if (completed && active) {
			ins.healAll(level, 0.01f);
		}
		if (completed || !active) {
			if (!passive.isEmpty()) {
				for (var id : passive) {
					if (level.getEntity(id) instanceof LivingEntity le) {
						if (le.isAlive() && le.getPassengers().isEmpty()) {
							le.discard();
						}
					}
				}
				passive.clear();
			}
		}
		this.data.tickTrial(this, level, level.getGameTime());
		if (this.bar == null && !completed && active) {
			bar = this.makeBar();
			bar.setPlayers(ins.data.players);
		}
		if (bar != null) {
			this.data.updateBar(this.bar, level, level.getGameTime());
			bar.setPlayers(ins.data.players);
		}
	}

	public void addCost(int cost, long time) {
	}

	public void stop() {
		completed = false;
		active = false;
		if (this.bar != null) {
			this.bar.removeAllPlayers();
		}
		this.bar = null;
	}

	public void complete(ServerLevel level, TrialConfig config, long time) {
		completed = true;
		BlockPos pos = targets.getFirst();

		if (config.reward == null) return;

		LootTable loot = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, config.reward));
		LootParams params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).create(LootContextParamSets.CHEST);
		List<ItemStack> list = loot.getRandomItems(params);
		BlockPos up = pos.above();
		if (config.generateChest && level.getBlockState(up).isAir()) {
			level.setBlockAndUpdate(up, Blocks.CHEST.defaultBlockState());
		}

		ResourceHandler<ItemResource> cap = level.getCapability(Capabilities.Item.BLOCK, up, Direction.DOWN);
		if (cap != null) {
			ArrayList<ItemStack> ans = new ArrayList<>();

			for (ItemStack stack : list) {
				int inserted = ResourceHandlerUtil.insertStacking(cap, ItemResource.of(stack), stack.getCount(), null);
				if (inserted < stack.getCount()) {
					ans.add(stack.copyWithCount(stack.getCount() - inserted));
				}
			}

			list = ans;
		}

		for (ItemStack stack : list) {
			Block.popResource(level, pos.above(), stack);
		}

	}

	public void configureEntity(LivingEntity e, int index) {
		BlockPos pos = targets.get(index % targets.size());
		for (int x = -1; x <= 1; ++x) {
			for (int z = -1; z <= 1; ++z) {
				for (int y = 0; y <= 2; ++y) {
					e.level().removeBlock(pos.offset(x, y, z), false);
				}
			}
		}

		Vec3 vec = Vec3.atCenterOf(pos);
		e.setPos(vec);
		for (Entity le : e.getSelfAndPassengers().toList())
			if (le instanceof LivingEntity && !(le instanceof AbstractGolemEntity<?, ?>))
				passive.add(le.getUUID());
	}

	@Override
	public boolean isValidTracked(Entity entity) {
		return true;
	}

	public void configureGolem(AbstractGolemEntity<?, ?> golem, int mobIndex) {
	}

	public boolean isOnGoing() {
		return active;
	}

}
