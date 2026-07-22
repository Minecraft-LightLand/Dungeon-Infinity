package dev.xkmc.dungeon_infinity.content.block.merchant;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@SerialClass
public class MerchantBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

	@SerialField
	public String type = "groceries";

	@SerialField
	public long nextSpawnTime;
	@SerialField
	@Nullable
	public UUID prevMerchant;

	public MerchantBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void setType(String type) {
		this.type = type;
		sync();
		if (prevMerchant != null && level != null) {
			var prev = level.getEntity(prevMerchant);
			if (prev != null) {
				prev.discard();
			}
			prevMerchant = null;
		}
		nextSpawnTime = 0;
	}

	@Nullable
	private ServerPlayer hasPlayerNearby(Level level, int dist) {
		var pos = getBlockPos().getCenter();
		var players = level.players();
		int y = getBlockPos().getY() >> 4;
		for (var p : players) {
			int py = p.getBlockY() >> 4;
			if (py != y) continue;
			if (p.position().distanceTo(pos) > dist) continue;
			if (p instanceof ServerPlayer sp && !sp.isSpectator())
				return sp;
		}
		return null;
	}

	@Override
	public void tick() {
		var level = getLevel();
		if (!(level instanceof ServerLevel sl)) return;
		long time = level.getGameTime();
		if (time % 20 != 0) return;
		if (prevMerchant != null) {
			var sp = hasPlayerNearby(level, 48);
			if (sp == null || !MazeHistory.inMazeDim(sp)) {
				var prev = level.getEntity(prevMerchant);
				if (prev != null) {
					prev.discard();
				}
				prevMerchant = null;
				nextSpawnTime = 0;
				setChanged();
				return;
			}
		}
		if (time < nextSpawnTime) return;
		var sp = hasPlayerNearby(level, 24);
		if (sp == null || !MazeHistory.inMazeDim(sp)) {
			return;
		}
		setChanged();
		var pos = getBlockPos().above().getCenter();
		if (prevMerchant != null) {
			var prev = level.getEntity(prevMerchant);
			if (prev instanceof Merchant) {
				if (prev.position().distanceTo(pos) > 10)
					prev.snapTo(pos);
				nextSpawnTime = time + 200;
				return;
			} else if (prev != null) {
				prev.discard();
				prevMerchant = null;
			}
		}
		nextSpawnTime = time + 1200;
		var merchant = MerchantBlock.summonMerchant(sl, sp, getBlockPos(), type);
		if (merchant == null) return;
		merchant.snapTo(pos);
		sl.addFreshEntity(merchant);
		prevMerchant = merchant.getUUID();
	}

}
