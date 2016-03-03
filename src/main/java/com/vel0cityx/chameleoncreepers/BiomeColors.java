package com.vel0cityx.chameleoncreepers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.biome.*;


/**
 * Created by Nikos on 3/3/2016.
 */
public class BiomeColors {

    public static int[] getBiomeColors(EntityLivingBase entity)
    {
        int x = (int) entity.posX;
        int y = (int) entity.posY;
        int z = (int) entity.posZ;

        int r = 0;
        int g = 0;
        int b = 0;

        int currCol;

        for (int x0 = -1; x0 <= 1; ++x0) {
            for (int y0 = -1; y0 <= 1; ++y0) {
                BiomeGenBase biome = entity.worldObj.getBiomeGenForCoords(x + x0, z + y0);

                if(biome instanceof BiomeGenBeach)
                {
                    r += 216; g += 206; b += 159;
                }
                else if(biome instanceof BiomeGenStoneBeach)
                {
                    r += 100; g += 100; b += 100;
                }
                else if(biome instanceof BiomeGenDesert)
                {
                    r += 225; g += 218; b += 166;
                }
                else if(biome instanceof BiomeGenSnow)
                {
                    r += 245; g += 245; b += 255;
                }
                else if(biome instanceof BiomeGenMesa)
                {
                    r += 167; g += 33; b += 86;
                }
                else
                {
                    currCol = biome.getBiomeGrassColor(x + x0, y, z + y0);
                    r += (currCol & 16711680) >> 16;
                    g += (currCol & 65280) >> 8;
                    b += currCol & 255;
                }
            }
        }

        r /= 9;
        g /= 9;
        b /= 9;

        return new int[]{r,g,b};
    }
}
