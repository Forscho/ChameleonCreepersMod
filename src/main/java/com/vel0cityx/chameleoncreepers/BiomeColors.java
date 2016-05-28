package com.vel0cityx.chameleoncreepers;

import jline.internal.Log;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.*;

/**
 * Created by Nikos on 3/3/2016.
 */

public class BiomeColors {
    // Return the average color of whatever blocks are below, not just grass
    public static int[] getBlockColors(EntityLivingBase creeper, boolean onlyDoBiomeColors) {
        int entityX = (int) creeper.posX;
        int entityY = (int) creeper.posY;
        int entityZ = (int) creeper.posZ;

        int r = 0;
        int g = 0;
        int b = 0;

        int currCol;

        int blocksCalculated = 3 * 3 * 3;

        if (creeper.isInWater()) {
            return new int[]{64, 64, 255};
        }

        if (creeper.isInsideOfMaterial(Material.lava)) {
            return new int[]{255, 64, 64};
        }

        for (int x0 = -1; x0 <= 1; ++x0) {
            for (int y0 = 0; y0 <= 2; ++y0) {
                for (int z0 = -1; z0 <= 1; ++z0) {

                    IBlockState iBlockState = creeper.worldObj.getBlockState(new BlockPos(entityX + x0, (int) (entityY + y0 - .5), entityZ + z0));

                    Block blockCloseToCreeper = iBlockState.getBlock();

                    if (blockCloseToCreeper instanceof BlockAir) {
                        blocksCalculated--;
                        continue;
                    }

                    // Only return biome colors (greens)
                    if (onlyDoBiomeColors || blockCloseToCreeper instanceof BlockGrass || blockCloseToCreeper instanceof BlockTallGrass ||
                            blockCloseToCreeper instanceof BlockLeaves) {
                        BiomeGenBase biome = creeper.worldObj.getBiomeGenForCoords(new BlockPos(entityX + x0, entityY + y0, entityZ + z0));
                        currCol = biome.getGrassColorAtPos(new BlockPos(entityX + x0, entityY + y0, entityZ + z0));
                    }
                    else {
                        currCol = blockCloseToCreeper.getMapColor(iBlockState).colorValue;
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

        return new int[]{r, g, b};
    }
}