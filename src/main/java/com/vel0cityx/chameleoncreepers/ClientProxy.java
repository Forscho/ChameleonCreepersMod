package com.vel0cityx.chameleoncreepers;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.monster.EntityCreeper;

/**
 * Created by Nikos on 3/3/2016.
 */
public class ClientProxy extends CommonProxy {

    private static final CreeperRenderFactory creeperRenderFactory = new CreeperRenderFactory();

    public static boolean onlyUseGrassColors;

    private static class CreeperRenderFactory implements IRenderFactory<EntityCreeper>
    {
        @Override
        public Render<? super EntityCreeper> createRenderFor(RenderManager manager)
        {
            return new RenderChameleonCreeper(manager);
        }
    }

    @Override
    public void preInit(FMLPreInitializationEvent evt) {

        Configuration config = new Configuration(evt.getSuggestedConfigurationFile());

        config.load();

        onlyUseGrassColors = config.getBoolean("onlyUseGrassColors", Configuration.CATEGORY_CLIENT,
                false, "Whether creepers should be limited to only biome(green-ish) colors.");

        config.save();

        RenderingRegistry.registerEntityRenderingHandler(EntityCreeper.class,
                creeperRenderFactory);
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
