package dev.u9g.configlib;

import com.google.common.collect.Lists;
import dev.u9g.configlib.config.MyModConfigEditor;
import dev.u9g.configlib.config.ScreenElementWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "configlib")
public class ExampleMod {
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
//		MinecraftForge.EVENT_BUS.register(this);
	}
//
//	@SubscribeEvent
//	public void onType(ClientChatReceivedEvent event) {
//		if (event.message.getUnformattedTextForChat().startsWith("Unknown command.")) {
//			Minecraft.getMinecraft().displayGuiScreen(new ScreenElementWrapper(new MyModConfigEditor(MyModConfig.INSTANCE)));
//			event.setCanceled(true);
//		}
//	}
}
