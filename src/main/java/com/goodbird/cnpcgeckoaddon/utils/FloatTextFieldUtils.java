package com.goodbird.cnpcgeckoaddon.utils;


import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

public class FloatTextFieldUtils {
    public static boolean isFloat(GuiTextFieldNop field) {
        try {
            Float.parseFloat(field.getValue());
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public static float getFloat(GuiTextFieldNop field) {
        return Float.parseFloat(field.getValue());
    }

    public static void performFloatChecks(float min, float max, float def, GuiTextFieldNop field){
        if (!field.isEmpty() && isFloat(field)) {
            if (getFloat(field) < min) {
                field.setValue(min + "");
            } else if (getFloat(field) > max) {
                field.setValue(max + "");
            }
        } else {
            field.setValue(def + "");
        }
    }
}