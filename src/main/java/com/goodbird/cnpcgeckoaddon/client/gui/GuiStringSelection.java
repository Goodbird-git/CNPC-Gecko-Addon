package com.goodbird.cnpcgeckoaddon.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiStringSlotNop;

import java.util.List;
import java.util.function.Consumer;

public class GuiStringSelection extends GuiNPCInterface {
    public GuiStringSlotNop slot;
    public Consumer<String> action;
    public Screen parent;
    public String title;
    public List<String> options;

    public GuiStringSelection(Screen parent, String title, List<String> options, Consumer<String> action) {
        drawDefaultBackground = false;
        this.parent = parent;
        this.action = action;
        this.title = title;
        this.options = options;
    }

    @Override
    public void init() {
        super.init();
        addLabel(new GuiLabel(0, title, width / 2 - (this.font.width(title) / 2), 20, 0xffffff));
        options.sort(String.CASE_INSENSITIVE_ORDER);
        slot = new GuiStringSlotNop(options, this, false);
        addWidget(this.slot);
        this.addButton(new GuiButtonNop(this, 2, width / 2 - 100, height - 44, 98, 20, "gui.back"));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.slot.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void doubleClicked() {
        action.accept(slot.getSelectedString());
        close();
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 2) {
            close();
        }
    }
}