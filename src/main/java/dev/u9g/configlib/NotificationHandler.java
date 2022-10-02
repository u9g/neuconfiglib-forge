package dev.u9g.configlib;

import dev.u9g.configlib.util.Utils;
import dev.u9g.configlib.util.render.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.Window;

import java.util.List;

/**
 * if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
 * 			NotificationHandler.notificationDisplayMillis = 0;
 *  }
 *
 *  Run that code inside a post render game overlay event.
 */

public class NotificationHandler {
	public static List<String> notificationLines = null;
	public static boolean showNotificationOverInv = false;
	public static long notificationDisplayMillis = 0;

	public static void displayNotification(List<String> lines, boolean showForever) {
		displayNotification(lines, showForever, false);
	}

	public static void displayNotification(List<String> lines, boolean showForever, boolean overInventory) {
		if (showForever) {
			notificationDisplayMillis = -420;
		} else {
			notificationDisplayMillis = System.currentTimeMillis();
		}
		notificationLines = lines;
		showNotificationOverInv = overInventory;
	}

	/**
	 * Stops rendering the notification, if one is displayed
	 */
	public static void cancelNotification() {
		notificationDisplayMillis = 0;
	}

	public static void renderNotification() {
		long timeRemaining = 15000 - (System.currentTimeMillis() - notificationDisplayMillis);
		boolean display = timeRemaining > 0 || notificationDisplayMillis == -420;
		if (display && notificationLines != null && notificationLines.size() > 0) {
			int width = 0;
			int height = notificationLines.size() * 10 + 10;

			for (String line : notificationLines) {
				int len = M.C.textRenderer.getStringWidth(line) + 8;
				if (len > width) {
					width = len;
				}
			}

			Window sr = Utils.pushGuiScale(2);

			int midX = sr.getWidth() / 2;
			int topY = sr.getHeight() * 3 / 4 - height / 2;
			RenderUtils.drawFloatingRectDark(midX - width / 2, sr.getHeight() * 3 / 4 - height / 2, width, height);

			int xLen = M.C.textRenderer.getStringWidth("[X] Close");
			M.C.textRenderer.draw(
				"[X] Close",
				midX + width / 2f - 3 - xLen,
				topY + 3,
				0xFFFF5555,
				false
			);

			if (notificationDisplayMillis > 0) {
				M.C.textRenderer.draw(
					(timeRemaining / 1000) + "s",
					midX - width / 2f + 3,
					topY + 3,
					0xFFaaaaaa,
					false
				);
			}

			Utils.drawStringCentered(
				notificationLines.get(0),
				M.C.textRenderer,
				midX,
				topY + 4 + 5,
				false,
				-1
			);
			for (int i = 1; i < notificationLines.size(); i++) {
				String line = notificationLines.get(i);
				Utils.drawStringCentered(
					line,
					M.C.textRenderer,
					midX,
					topY + 4 + 5 + 2 + i * 10,
					false,
					-1
				);
			}

			Utils.pushGuiScale(-1);
		}
	}

	public static boolean shouldRenderOverlay(Screen gui) {
		boolean validGui = gui instanceof HandledScreen;
//		if (gui instanceof ChestScreen) {
//			ChestScreen eventGui = (ChestScreen) gui;
//			ChestScreen cc = ((ChestScreen) eventGui).screenHandler;
//			String containerName = cc.getLowerChestInventory().getDisplayName().getUnformattedText();
//			if (containerName.trim().equals("Fast Travel")) {
//				validGui = false;
//			}
//		}
		return validGui;
	}
}