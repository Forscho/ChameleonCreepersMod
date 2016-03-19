package com.vel0cityx.chameleoncreepers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import com.vel0cityx.chameleoncreepers.ClientProxy;

/**
 * Created by Nikos on 6/3/2016.
 */

public class RenderChameleonCreeper extends RenderLiving<EntityCreeper> {
    private ModelCreeper creeperModel;
    protected ResourceLocation npcTexture = new ResourceLocation(ChameleonCreepersMod.MODID+":"+"textures/entity/creeper/chameleoncreeper.png");

    private static final ResourceLocation creeperTextures = new ResourceLocation(ChameleonCreepersMod.MODID+":"+"textures/entity/creeper/chameleoncreeper.png");

    public RenderChameleonCreeper(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelCreeper(), 0.5F);
        this.addLayer(new LayerCreeperCharge(new RenderCreeper(renderManager)));
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityCreeper entitylivingbaseIn, float partialTickTime)
    {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityCreeper entitylivingbaseIn, float lightBrightness, float partialTickTime)
    {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);

        if ((int)(f * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int i = (int)(f * 0.2F * 255.0F);
            i = MathHelper.clamp_int(i, 0, 255);
            return i << 24 | 16777215;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityCreeper entity)
    {
        return creeperTextures;
    }

    @Override
    protected boolean canRenderName(EntityCreeper entity) {
        return super.canRenderName(entity);
    }

    @Override
    public boolean shouldRender(EntityCreeper livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *
     * @param entity
     * @param x
     * @param y
     * @param z
     * @param entityYaw
     * @param partialTicks
     */
    @Override
    public void doRender(EntityCreeper entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Renders the model in RenderLiving
     */
    @Override
    protected void renderModel(EntityCreeper entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
        boolean flag = !entitylivingbaseIn.isInvisible();
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);

        if (flag || flag1)
        {
            if (!this.bindEntityTexture(entitylivingbaseIn))
            {
                return;
            }

            if (flag1)
            {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }

            // THIS IS WHERE THE MAGIC HAPPENS
            //==========================================================================================================
            if(entitylivingbaseIn.hurtTime <= 0 || entitylivingbaseIn.deathTime > 0) {

                int[] colorTint = shouldOnlyUseGrassColors() ? BiomeColors.getBiomeColors(entitylivingbaseIn) : BiomeColors.getBlockColors(entitylivingbaseIn);
                GL11.glColor4f(colorTint[0] / 255.f, colorTint[1] / 255.f, colorTint[2] / 255.f, 1.0F);
            }
            //==========================================================================================================
            // END OF MAGIC

            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

            if (flag1)
            {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }

    @Override
    protected void renderLeash(EntityCreeper entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks) {
        super.renderLeash(entityLivingIn, x, y, z, entityYaw, partialTicks);
    }

    private boolean shouldOnlyUseGrassColors() {
        return ClientProxy.onlyUseGrassColors;
    }
}
