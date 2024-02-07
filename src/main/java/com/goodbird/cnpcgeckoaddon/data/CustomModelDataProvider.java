package com.goodbird.cnpcgeckoaddon.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;


public class CustomModelDataProvider implements ICapabilitySerializable<NBTBase>
{
    @CapabilityInject(ICustomModelData.class)
    public static Capability<ICustomModelData> DATA_CAP = null;

    private ICustomModelData instance = DATA_CAP.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == DATA_CAP;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability == DATA_CAP ? DATA_CAP.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return DATA_CAP.getStorage().writeNBT(DATA_CAP, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        DATA_CAP.getStorage().readNBT(DATA_CAP, this.instance, null, nbt);
    }
}