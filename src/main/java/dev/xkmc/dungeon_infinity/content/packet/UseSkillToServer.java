package dev.xkmc.dungeon_infinity.content.packet;

import dev.xkmc.dungeon_infinity.content.buff.core.MazeBuff;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record UseSkillToServer(Identifier id) implements SerialPacketBase<UseSkillToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var data = DIMeta.HISTORY.type().getOrCreate(player).buff;
		var buff = MazeBuff.get(id);
		if (buff == null || data.buffs.getOrDefault(id, 0) <= 0) return;
		buff.onSkillUse(sp);
	}

}
