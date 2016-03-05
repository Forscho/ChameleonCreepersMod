package com.vel0cityx.chameleoncreepers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.biome.*;

/**
 * Created by Nikos on 3/3/2016.
 */
public class BiomeColors {

    public static int[] getBiomeColors(EntityLivingBase creeper)
    {
        int entityX = (int) creeper.posX;
        int entityY = (int) creeper.posY;
        int entityZ = (int) creeper.posZ;

        int r = 0;
        int g = 0;
        int b = 0;

        int currCol;

        int blocksCalculated = 9;

        if(creeper.isInWater())
        {
            return new int[]{64, 64, 255};
        }

        if(creeper.isInsideOfMaterial(Material.lava))
        {
            return new int[]{255, 64, 64};
        }

        for (int x0 = -1; x0 <= 1; ++x0) {
            for (int z0 = -1; z0 <= 1; ++z0) {

                Block blockUnderCreeper = creeper.worldObj.getBlock(entityX + x0, (int)(entityY - .5), entityZ + z0);

                // Look up to 2 blocks below, if botha are Air disregard that x,y value
                if(blockUnderCreeper instanceof BlockAir)
                {
                    blockUnderCreeper = creeper.worldObj.getBlock(entityX + x0, (int)(entityY - 1.5), entityZ + z0);
                    if(blockUnderCreeper instanceof BlockAir)
                    {
                        blocksCalculated--;
                    }
                }

                if(blockUnderCreeper instanceof BlockGrass || blockUnderCreeper instanceof BlockLeaves)
                {
                    BiomeGenBase biome = creeper.worldObj.getBiomeGenForCoords(entityX + x0, entityZ + z0);
                    currCol = biome.getBiomeGrassColor(entityX + x0, entityY - 1, entityZ + z0);
                    r += (currCol & 16711680) >> 16;
                    g += (currCol & 65280) >> 8;
                    b += currCol & 255;
                    continue;
                }

                currCol = blockUnderCreeper.getMapColor(0).colorValue;
                r += (currCol & 16711680) >> 16;
                g += (currCol & 65280) >> 8;
                b += currCol & 255;
            }
        }

        r /= blocksCalculated;
        g /= blocksCalculated;
        b /= blocksCalculated;

        return new int[]{r,g,b};
    }
}
