package com.goodbird.cnpcgeckoaddon.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.SubGuiInterface;

import java.util.List;
import java.util.function.Consumer;

public class GuiStringSelection extends SubGuiInterface {
    public GuiNPCStringSlot slot;
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
        addLabel(new GuiNpcLabel(0, title, width / 2 - (this.font.width(title) / 2), 20, 0xffffff));
        options.sort(String.CASE_INSENSITIVE_ORDER);
        slot = new GuiNPCStringSlot(options, this, false);
        this.children.add(this.slot);
        this.addButton(new GuiNpcButton(this, 2, width / 2 - 100, height - 44, 98, 20, "gui.back"));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.slot.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void doubleClicked() {
        action.accept(slot.getSelectedString());
        close();
    }

    public void buttonEvent(GuiNpcButton guibutton) {
        int id = guibutton.id;
        if (id == 2) {
            close();
        }
    }
}