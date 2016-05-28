package com.vel0cityx.chameleoncreepers;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;

@Mod(modid = ChameleonCreepersMod.MODID, version = ChameleonCreepersMod.VERSION, name = ChameleonCreepersMod.NAME,
        acceptedMinecraftVersions="*", acceptableRemoteVersions="*", canBeDeactivated = true)
public class ChameleonCreepersMod
{
    public static final String MODID = "chameleoncreepers";
    public static final String NAME = "Chameleon Creepers";
    public static final String VERSION = "1.4.1";

    @SidedProxy(clientSide="com.vel0cityx.chameleoncreepers.ClientProxy", serverSide="com.vel0cityx.chameleoncreepers.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }
}
