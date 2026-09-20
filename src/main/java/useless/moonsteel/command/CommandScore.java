package useless.moonsteel.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.net.command.helpers.EntitySelector;

import static useless.moonsteel.command.CommandScore.ReturnValues.*;

public class CommandScore implements CommandManager.CommandRegistry {

	public static SimpleCommandExceptionType INVALID_ENTITY;

	@Override
	public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		INVALID_ENTITY = new SimpleCommandExceptionType(new LiteralMessage(I18n.getInstance().translateKey("score.command.not.applicable")));
		commandDispatcher.register(ArgumentBuilderLiteral.<CommandSource>literal("moonsteel:score")
			.then(ArgumentBuilderLiteral.<CommandSource>literal("add")
				.requires(src -> src.hasAdmin() && src.getSender() != null)
				.then(ArgumentBuilderRequired.<CommandSource, EntitySelector>argument("player", ArgumentTypeEntity.username())
					.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("value", ArgumentTypeInteger.integer())
						.executes(ctx -> addScore(ctx, 0))
					)
				)
			)
			.then(ArgumentBuilderLiteral.<CommandSource>literal("set")
				.requires(src -> src.hasAdmin() && src.getSender() != null)
				.then(ArgumentBuilderRequired.<CommandSource, EntitySelector>argument("player", ArgumentTypeEntity.username())
					.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("value", ArgumentTypeInteger.integer())
						.executes(CommandScore::setScore)
					)
				)
			)
			.then(ArgumentBuilderLiteral.<CommandSource>literal("remove")
				.requires(src -> src.hasAdmin() && src.getSender() != null)
				.then(ArgumentBuilderRequired.<CommandSource, EntitySelector>argument("player", ArgumentTypeEntity.username())
					.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("value", ArgumentTypeInteger.integer())
						.executes(ctx -> removeScore(ctx, 0))
					)
				)
			)
			.then(ArgumentBuilderLiteral.<CommandSource>literal("list")
				.then(ArgumentBuilderRequired.<CommandSource, EntitySelector>argument("player", ArgumentTypeEntity.username())
					.executes(CommandScore::infoScore)
				)
			)
		);
	}

	private static Player getPlayer(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		Player player = null;
		try {
			player = ctx.getArgument("player", Player.class);
		} catch (IllegalArgumentException e) {
			player = ctx.getSource().getSender();
		}
		if (player == null) {
			throw INVALID_ENTITY.create();
		}
		return player;
	}

	private static int addScore(CommandContext<CommandSource> ctx, int counter) throws CommandSyntaxException {
		if (counter > 2) {
			ctx.getSource().sendTranslatableMessage("score.command.cannot");
			return code(FAIL);
		}
		final Player player = getPlayer(ctx);
		int value = ctx.getArgument("value", Integer.class);
		if (value < 0) {
			return removeScore(ctx, counter + 1);
		}
		value = CommandScore.addScore(player, value);
		ctx.getSource().sendTranslatableMessage("score.command.add", player.getDisplayName(), value);
		return Command.SINGLE_SUCCESS;
	}

	private static int setScore(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final Player player = getPlayer(ctx);
		final int value = ctx.getArgument("value", Integer.class);
		CommandScore.setScore(player, value);
		ctx.getSource().sendTranslatableMessage("score.command.set", player.getDisplayName(), value);
		return code(OK);
	}

	private static int removeScore(CommandContext<CommandSource> ctx, int counter) throws CommandSyntaxException {
		if (counter > 2) {
			ctx.getSource().sendTranslatableMessage("score.command.cannot");
			return code(FAIL);
		}
		final Player player = getPlayer(ctx);
		int value = ctx.getArgument("value", Integer.class);
		if (value < 0) {
			return addScore(ctx, counter + 1);
		}
		value = CommandScore.subScore(player, value);
		ctx.getSource().sendTranslatableMessage("score.command.remove", player.getDisplayName(), value);
		return Command.SINGLE_SUCCESS;
	}

	private static int infoScore(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		Player player = getPlayer(ctx);
		ctx.getSource().sendTranslatableMessage("score.command.info", player.getDisplayName(), player.getScore());
		return Command.SINGLE_SUCCESS;
	}

	public static int addScore(Player player, int value) {
		int currentScore = player.getScore();
		if (currentScore > Integer.MAX_VALUE - value) {
			player.score = Integer.MAX_VALUE;
			return Integer.MAX_VALUE - currentScore;
		}
		player.score += value;
		return value;
	}

	public static int subScore(Player player, int value) {
		int currentScore = player.getScore();
		if (currentScore < Integer.MIN_VALUE + value) {
			player.score = Integer.MIN_VALUE;
			return currentScore - Integer.MIN_VALUE + 1;
		}
		player.score -= value;
		return value;
	}

	public static void setScore(Player player, int value) {
		player.score = value;
	}

	public enum ReturnValues {
		CANNOT(-2),
		FAIL(-1),
		OK(0);
		private final int code;

		ReturnValues(int code) {
			this.code = code;
		}

		public static int code(ReturnValues v) {
			return v.code;
		}
	}

}
