package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.Ref;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CrateDoubleScreen extends BaseCrateScreen<BaseCrateContainer> {

    private Identifier GUI = new Identifier(Ref.MODID, "textures/gui/crate_double.png");

    public CrateDoubleScreen(BaseCrateContainer container, PlayerInventory inv, Text name) {
        super(container, inv, name);
    }

    @Override
    Identifier getGuiTexture() {
        return GUI;
    }
}