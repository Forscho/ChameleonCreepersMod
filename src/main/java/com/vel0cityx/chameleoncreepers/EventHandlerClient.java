package com.vel0cityx.chameleoncreepers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.io.IOException;

/**
 * Created by Nikos on 21/4/2016.
 */
public class EventHandlerClient {

    @SubscribeEvent
    public void onLoadResourcepack(TextureStitchEvent.Post evt)
    {
        try
        {
            RenderChameleonCreeper.convertTextureToGrayScale();
        }
        catch (IOException ie)
        {
            System.out.println("Couldn't convert creeper texture to grayscale");
            ie.printStackTrace();
        }
    }
}
