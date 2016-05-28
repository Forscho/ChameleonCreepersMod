package com.vel0cityx.chameleoncreepers;

import net.minecraft.block.*;
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

        int blocksCalculated = 3 * 3 * 3;

        if(creeper.isInWater())
        {
            return new int[]{64, 64, 255};
        }

        if(creeper.isInsideOfMaterial(Material.lava))
        {
            return new int[]{255, 64, 64};
        }

        for (int x0 = -1; x0 <= 1; ++x0) {
            for (int y0 = 0; y0 <= 2; ++y0) {
                for (int z0 = -1; z0 <= 1; ++z0) {

                    Block blockUnderCreeper = creeper.worldObj.getBlock(entityX + x0, (int) (entityY - .5), entityZ + z0);

                    // Look up to 2 blocks below, if both are Air disregard that x,y value
                    if (blockUnderCreeper instanceof BlockAir) {
                        blockUnderCreeper = creeper.worldObj.getBlock(entityX + x0, (int) (entityY - 1.5), entityZ + z0);
                        if (blockUnderCreeper instanceof BlockAir) {
                            blocksCalculated--;
                        }
                    }

                    if (blockUnderCreeper instanceof BlockGrass || blockUnderCreeper instanceof BlockTallGrass
                            || blockUnderCreeper instanceof BlockLeaves) {
                        BiomeGenBase biome = creeper.worldObj.getBiomeGenForCoords(entityX + x0, entityZ + z0);
                        currCol = biome.getBiomeGrassColor(entityX + x0, entityY - 1, entityZ + z0);
                    } else {
                        currCol = blockUnderCreeper.getMapColor(0).colorValue;
                    }
                    r += (currCol & 16711680) >> 16;
                    g += (currCol & 65280) >> 8;
                    b += currCol & 255;
                }
            }
        }

        // Default to Grey color if the Creeper is falling from the air (without any blocks near it)
        if(blocksCalculated == 0) {
            return new int[]{135,135,135};
        }

        r /= blocksCalculated;
        g /= blocksCalculated;
        b /= blocksCalculated;

        return new int[]{r,g,b};
    }
}
