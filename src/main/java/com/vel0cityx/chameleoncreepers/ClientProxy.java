package com.vel0cityx.chameleoncreepers;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Nikos on 3/3/2016.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent evt) {
        RenderingRegistry.registerEntityRenderingHandler(EntityCreeper.class,
                new RenderChameleonCreeper());

        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }

    @Override
    public void init(FMLInitializationEvent evt) {
        super.init(evt);
    }

    @Override
    public void postInit(FMLPostInitializationEvent evt) {
        super.postInit(evt);
    }
}
