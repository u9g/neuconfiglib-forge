package dev.u9g.configlib;

import com.google.common.collect.Lists;
import dev.u9g.configlib.config.MyModConfigEditor;
import dev.u9g.configlib.config.ScreenElementWrapper;
import net.fabricmc.api.ModInitializer;
import net.legacyfabric.fabric.api.command.v2.CommandRegistrar;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.minecraft.text.LiteralText;

public class ExampleMod implements ModInitializer {
	@Override
	public void onInitialize() {
//		CommandRegistrar.EVENT.register((manager, dedicated) -> {
//			manager.register(CommandSpec.builder().executor((src, args) -> {
//				// Done so this is run after we send the command result of success
//				// this is needed so closing the chat screen doesn't capture the mouse cursor before we open the screen
//				// instead we capture the mouse pointer after we close the chat screen
//				M.C.execute(() -> {
//					M.C.openScreen(new ScreenElementWrapper(new MyModConfigEditor(MyModConfig.INSTANCE)));
//				});
//				if (MyModConfig.INSTANCE.player.playersAreSmall) {
//					M.C.player.addMessage(new LiteralText("You just used /mymodcmd"));
//				}
//				return CommandResult.success();
//			}).build(), "mymodcmd");
//
//			manager.register(CommandSpec.builder().executor((src, args) -> {
//				NotificationHandler.displayNotification(Lists.newArrayList(
//						"\u00a7eMagma boss has spawned!  ",
//						"\u00a77You have to be quick, it will die soon...      "
//				), true);
//				return CommandResult.success();
//			}).build(), "mymodnoti");
//		});
	}
}
