package com.vel0cityx.chameleoncreepers;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

/**
 * Created by Nikos on 21/4/2016.
 */
public class EventHandlerClient {

    @SubscribeEvent
    void onLoadResourcepack(TextureStitchEvent.Post evt)
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
