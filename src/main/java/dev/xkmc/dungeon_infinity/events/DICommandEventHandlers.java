package dev.xkmc.dungeon_infinity.events;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = DungeonInfinity.MODID)
public class DICommandEventHandlers {

	@SubscribeEvent
	public static void register(RegisterCommandsEvent event) {
		event.getDispatcher().register(literal("maze")
				.then(literal("buff")
						.then(argument("player", EntityArgument.player())
								.then(literal("clear")
										.executes(DICommandEventHandlers::clear))
								.then(literal("add_small")
										.then(argument("count", IntegerArgumentType.integer(1))
												.executes(DICommandEventHandlers::addSmall)))
								.then(literal("add_large")
										.then(argument("count", IntegerArgumentType.integer(1))
												.executes(DICommandEventHandlers::addLarge)))
								.then(literal("add_reroll")
										.then(argument("count", IntegerArgumentType.integer(1))
												.executes(DICommandEventHandlers::addReroll)))
						)));
	}

	private static int clear(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		EntitySelector sel = ctx.getArgument("player", EntitySelector.class);
		ServerPlayer sp = sel.findSinglePlayer(ctx.getSource());
		var data = DIMeta.HISTORY.type().getOrCreate(sp);
		data.buff.buffs.clear();
		data.buff.sync(sp);
		return 0;
	}

	private static int addSmall(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		EntitySelector sel = ctx.getArgument("player", EntitySelector.class);
		int count = ctx.getArgument("count", Integer.class);
		ServerPlayer sp = sel.findSinglePlayer(ctx.getSource());
		var data = DIMeta.HISTORY.type().getOrCreate(sp);
		data.buff.smallBuff += count;
		data.buff.sync(sp);
		return 0;
	}

	private static int addLarge(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		EntitySelector sel = ctx.getArgument("player", EntitySelector.class);
		int count = ctx.getArgument("count", Integer.class);
		ServerPlayer sp = sel.findSinglePlayer(ctx.getSource());
		var data = DIMeta.HISTORY.type().getOrCreate(sp);
		data.buff.largeBuff += count;
		data.buff.sync(sp);
		return 0;
	}

	private static int addReroll(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		EntitySelector sel = ctx.getArgument("player", EntitySelector.class);
		int count = ctx.getArgument("count", Integer.class);
		ServerPlayer sp = sel.findSinglePlayer(ctx.getSource());
		var data = DIMeta.HISTORY.type().getOrCreate(sp);
		data.buff.rerollChance += count;
		data.buff.sync(sp);
		return 0;
	}

	protected static LiteralArgumentBuilder<CommandSourceStack> literal(String str) {
		return LiteralArgumentBuilder.literal(str);
	}

	protected static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

}
