package com.lfaoanl.marketcrates.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

abstract class BaseCrateScreen<T extends BaseCrateContainer> extends HandledScreen<T> {


    public BaseCrateScreen(T container, PlayerInventory inv, Text name) {
        super(container, inv, name);

    }

    @Override
    protected void drawBackground(DrawContext context, float partialTicks, int x, int y) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);

//        this.minecraft.getTextureManager().bindForSetup(getGuiTexture());
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getGuiTexture());

        int relX = (this.width - this.backgroundWidth) / 2;
        int relY = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(getGuiTexture(), relX, relY, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
//        drawString(Minecraft.getInstance().fontRenderer, "Energy: " + container.getEnergy(), 10, 10, 0xffffff);
    }

    abstract Identifier getGuiTexture();

}