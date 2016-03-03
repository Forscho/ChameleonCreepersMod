package com.vel0cityx.chameleoncreepers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public final class EntityEventHandler {

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent evt) {
        if (evt.entity.getClass() == EntityCreeper.class){
            //evt.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void orRenderLiving(RenderLivingEvent evt)
    {
        if(evt.entity instanceof EntityCreeper)
        {
        }
    }

}