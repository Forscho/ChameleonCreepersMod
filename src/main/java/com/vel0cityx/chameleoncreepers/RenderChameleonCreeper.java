package com.vel0cityx.chameleoncreepers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Created by Nikos on 6/3/2016.
 */

public class RenderChameleonCreeper extends RenderLiving<EntityCreeper> {
    static ResourceLocation grayscaleCreeperTexture;

    private FloatBuffer currentGLColor = BufferUtils.createFloatBuffer(16);
    private static final ResourceLocation vanillaCreeperTexture = new ResourceLocation("textures/entity/creeper/creeper.png");

    public static void convertTextureToGrayScale() throws IOException
    {
        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        try
        {
            BufferedImage vanillaCreeperTextureData = TextureUtil.readBufferedImage(resourceManager.getResource(vanillaCreeperTexture).getInputStream());

            // Do the conversion to grayscale
            BufferedImage creeperTextureData = new BufferedImage(vanillaCreeperTextureData.getWidth(), vanillaCreeperTextureData.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
            Graphics g = creeperTextureData.getGraphics();
            g.drawImage(vanillaCreeperTextureData, 0, 0, null);
            g.dispose();

            DynamicTexture dynamicGrayscaleCreeperTexture = new DynamicTexture(creeperTextureData);

            grayscaleCreeperTexture = new ResourceLocation(ChameleonCreepersMod.MODID, "textures/entity/creeper/chameleoncreeper.png");
            Minecraft.getMinecraft().getTextureManager().loadTexture(grayscaleCreeperTexture, dynamicGrayscaleCreeperTexture);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public RenderChameleonCreeper(RenderManager renderManagerIn) throws IOException
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
        f = MathHelper.clamp(f, 0.0F, 1.0F);
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
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 16777215;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityCreeper entity)
    {
        return grayscaleCreeperTexture;
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
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().player);

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

                // Save the current OpenGL color to re-set it later
                GL11.glGetFloat(GL11.GL_CURRENT_COLOR, currentGLColor);

                int[] colorTint = BiomeColors.getBlockColors(entitylivingbaseIn, shouldOnlyUseGrassColors());
                GL11.glColor4f(colorTint[0] / 255.f, colorTint[1] / 255.f, colorTint[2] / 255.f, 1.0F);
            }
            //==========================================================================================================
            // END OF MAGIC

            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

            // Re-set the GL color
            GL11.glColor4f(currentGLColor.get(0),currentGLColor.get(1),currentGLColor.get(2),currentGLColor.get(3));

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
