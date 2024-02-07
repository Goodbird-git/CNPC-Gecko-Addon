package com.goodbird.cnpcgeckoaddon.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

/**
 * This class is responsible for saving and reading mana data from or to server
 */
public class CustomModelDataStorage implements IStorage<ICustomModelData>
{
    @Override
    public NBTBase writeNBT(Capability<ICustomModelData> capability, ICustomModelData instance, EnumFacing side)
    {
        return new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<ICustomModelData> capability, ICustomModelData instance, EnumFacing side, NBTBase nbt)
    {
    }
}