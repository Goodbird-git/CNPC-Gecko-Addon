package com.goodbird.cnpcgeckoaddon.utils;

import noppes.npcs.client.gui.util.GuiNpcTextField;

public class FloatTextFieldUtils {
    public static boolean isFloat(GuiNpcTextField field) {
        try {
            Float.parseFloat(field.getText());
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public static float getFloat(GuiNpcTextField field) {
        return Float.parseFloat(field.getText());
    }

    public static void performFloatChecks(float min, float max, float def, GuiNpcTextField field){
        if (!field.isEmpty() && isFloat(field)) {
            if (getFloat(field) < min) {
                field.setText(min + "");
            } else if (getFloat(field) > max) {
                field.setText(max + "");
            }
        } else {
            field.setText(def + "");
        }
    }
}
