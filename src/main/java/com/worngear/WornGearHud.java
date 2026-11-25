package com.worngear;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = WornGearMod.MOD_ID, value = Dist.CLIENT)
public final class WornGearHud {
    private static final EquipmentSlot[] SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };
    private static final int SLOT_STEP = 20;

    private WornGearHud() {
    }

    @SubscribeEvent
    public static void render(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        int x = minecraft.getWindow().getGuiScaledWidth() / 2 + 98;
        int y = minecraft.getWindow().getGuiScaledHeight() - 19;
        for (EquipmentSlot slot : SLOTS) {
            ItemStack stack = minecraft.player.getItemBySlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            graphics.renderItem(stack, x, y);
            if (stack.isDamageableItem()) {
                drawDurability(graphics, stack, x, y);
            }
            x += SLOT_STEP;
        }
    }

    private static void drawDurability(GuiGraphics graphics, ItemStack stack, int x, int y) {
        int remaining = stack.getMaxDamage() - stack.getDamageValue();
        int width = Math.round(13.0F * remaining / stack.getMaxDamage());
        width = Math.max(0, Math.min(13, width));
        int left = x + 2;
        int top = y + 13;
        int color = stack.getBarColor() | 0xFF000000;
        graphics.fill(RenderType.guiOverlay(), left, top, left + 13, top + 2, 0xFF000000);
        if (width > 0) {
            graphics.fill(RenderType.guiOverlay(), left, top, left + width, top + 1, color);
        }
    }
}
