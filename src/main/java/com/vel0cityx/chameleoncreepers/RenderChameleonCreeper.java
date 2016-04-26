package com.vel0cityx.chameleoncreepers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * Created by Nikos on 1/3/2016.
 */


@SideOnly(Side.CLIENT)
public class RenderChameleonCreeper extends RendererLivingEntity
{

    private static final ResourceLocation vanillaCreeperTexture = new ResourceLocation("textures/entity/creeper/creeper.png");
    /** The creeper model. */
    public static ResourceLocation grayscaleCreeperTexture;

    //From RendererLivingEntity
    //******************************************************************************************************************
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final BiomeColors BiomeColorsObj = new BiomeColors();
    private static int[] colorTint = new int[3];
   /** The model to be used during the render passes. */
    protected ModelBase renderPassModel;
    //private static final String __OBFID = "CL_00001012";

    public static float NAME_TAG_RANGE = 64.0f;
    public static float NAME_TAG_RANGE_SNEAK = 32.0f;

    public RenderChameleonCreeper()
    {
        super(new ModelCreeper(), 0.5F);
    }

    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float partialTickTime)
    {
        this.doRender((EntityLivingBase)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, partialTickTime);
    }

    public static void convertTextureToGrayScale() throws IOException
    {
        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        try
        {
            BufferedImage vanillaCreeperTextureData = ImageIO.read(resourceManager.getResource(vanillaCreeperTexture).getInputStream());

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

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityCreeper p_110775_1_)
    {
        return grayscaleCreeperTexture;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityCreeper)p_110775_1_);
    }

    // From RenderLiving
    //******************************************************************************************************************

    // ShouldRenderNameTag
    protected boolean func_110813_b(EntityLiving p_110813_1_)
    {
        return super.func_110813_b(p_110813_1_) && (p_110813_1_.getAlwaysRenderNameTagForRender() || p_110813_1_.hasCustomNameTag() && p_110813_1_ == this.renderManager.field_147941_i);
    }

    // ShouldRenderNameTag
    @Override
    protected boolean func_110813_b(EntityLivingBase p_110813_1_)
    {
        return this.func_110813_b((EntityLiving)p_110813_1_);
    }


    // From RenderLivingEntity
    //******************************************************************************************************************

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    private float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_)
    {
        float f3;

        for (f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F)
        {
            ;
        }

        while (f3 >= 180.0F)
        {
            f3 -= 360.0F;
        }

        return p_77034_1_ + p_77034_3_ * f3;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase entityLivingBase, double entityLivingX, double entityLivingY, double entityLivingZ,
                         float unused, float partialTickTime)
    {
        if (MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(entityLivingBase, this, entityLivingX, entityLivingY, entityLivingZ))) return;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.mainModel.onGround = this.renderSwingProgress(entityLivingBase, partialTickTime);

        if (this.renderPassModel != null)
        {
            this.renderPassModel.onGround = mainModel.onGround;
        }

        this.mainModel.isRiding = entityLivingBase.isRiding();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }

        this.mainModel.isChild = entityLivingBase.isChild();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }

        try
        {
            float f2 = interpolateRotation(entityLivingBase.prevRenderYawOffset, entityLivingBase.renderYawOffset, partialTickTime);
            float f3 = this.interpolateRotation(entityLivingBase.prevRotationYawHead, entityLivingBase.rotationYawHead, partialTickTime);
            float f4;

            if (entityLivingBase.isRiding() && entityLivingBase.ridingEntity instanceof EntityLivingBase)
            {
                EntityLivingBase entitylivingbase1 = (EntityLivingBase)entityLivingBase.ridingEntity;
                f2 = this.interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset, partialTickTime);
                f4 = MathHelper.wrapAngleTo180_float(f3 - f2);

                if (f4 < -85.0F)
                {
                    f4 = -85.0F;
                }

                if (f4 >= 85.0F)
                {
                    f4 = 85.0F;
                }

                f2 = f3 - f4;

                if (f4 * f4 > 2500.0F)
                {
                    f2 += f4 * 0.2F;
                }
            }

            float f13 = entityLivingBase.prevRotationPitch + (entityLivingBase.rotationPitch - entityLivingBase.prevRotationPitch) * partialTickTime;

            // THIS IS WHERE THE MAGIC HAPPENS
            //==========================================================================================================
            if(entityLivingBase.hurtTime <= 0 || entityLivingBase.deathTime > 0) {

                colorTint = BiomeColorsObj.getBiomeColors(entityLivingBase);
                GL11.glColor4f(colorTint[0] / 255.f, colorTint[1] / 255.f, colorTint[2] / 255.f, 1.0F);
            }
            //==========================================================================================================
            // END OF MAGIC

            this.renderLivingAt(entityLivingBase, entityLivingX, entityLivingY, entityLivingZ);
            f4 = this.handleRotationFloat(entityLivingBase, partialTickTime);
            this.rotateCorpse(entityLivingBase, f4, f2, partialTickTime);
            float f5 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(entityLivingBase, partialTickTime);
            GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
            float f6 = entityLivingBase.prevLimbSwingAmount + (entityLivingBase.limbSwingAmount - entityLivingBase.prevLimbSwingAmount) * partialTickTime;
            float f7 = entityLivingBase.limbSwing - entityLivingBase.limbSwingAmount * (1.0F - partialTickTime);

            if (entityLivingBase.isChild())
            {
                f7 *= 3.0F;
            }

            if (f6 > 1.0F)
            {
                f6 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.setLivingAnimations(entityLivingBase, f7, f6, partialTickTime);
            this.renderModel(entityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
            int j;
            float f8;
            float f9;
            float f10;

            for (int i = 0; i < 4; ++i)
            {
                j = this.shouldRenderPass(entityLivingBase, i, partialTickTime);

                if (j > 0)
                {
                    this.renderPassModel.setLivingAnimations(entityLivingBase, f7, f6, partialTickTime);
                    this.renderPassModel.render(entityLivingBase, f7, f6, f4, f3 - f2, f13, f5);

                    if ((j & 240) == 16)
                    {
                        this.func_82408_c(entityLivingBase, i, partialTickTime);
                        this.renderPassModel.render(entityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
                    }

                    if ((j & 15) == 15)
                    {
                        f8 = (float)entityLivingBase.ticksExisted + partialTickTime;
                        this.bindTexture(RES_ITEM_GLINT);
                        GL11.glEnable(GL11.GL_BLEND);
                        f9 = 0.5F;
                        GL11.glColor4f(f9, f9, f9, 1.0F);
                        GL11.glDepthFunc(GL11.GL_EQUAL);
                        GL11.glDepthMask(false);

                        for (int k = 0; k < 2; ++k)
                        {
                            GL11.glDisable(GL11.GL_LIGHTING);
                            f10 = 0.76F;
                            GL11.glColor4f(0.5F * f10, 0.25F * f10, 0.8F * f10, 1.0F);
                            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                            GL11.glMatrixMode(GL11.GL_TEXTURE);
                            GL11.glLoadIdentity();
                            float f11 = f8 * (0.001F + (float)k * 0.003F) * 20.0F;
                            float f12 = 0.33333334F;
                            GL11.glScalef(f12, f12, f12);
                            GL11.glRotatef(30.0F - (float)k * 60.0F, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslatef(0.0F, f11, 0.0F);
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            this.renderPassModel.render(entityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glDepthMask(true);
                        GL11.glLoadIdentity();
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glDepthFunc(GL11.GL_LEQUAL);
                    }

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            GL11.glDepthMask(true);
            this.renderEquippedItems(entityLivingBase, partialTickTime);
            float lightBrightness = entityLivingBase.getBrightness(partialTickTime);
            j = this.getColorMultiplier(entityLivingBase, lightBrightness, partialTickTime);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if ((j >> 24 & 255) > 0 || entityLivingBase.hurtTime > 0 || entityLivingBase.deathTime > 0)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (entityLivingBase.hurtTime > 0 || entityLivingBase.deathTime > 0)
                {
                    GL11.glColor4f(lightBrightness, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(entityLivingBase, f7, f6, f4, f3 - f2, f13, f5);

                    for (int l = 0; l < 4; ++l)
                    {
                        if (this.inheritRenderPass(entityLivingBase, l, partialTickTime) >= 0)
                        {
                            GL11.glColor4f(lightBrightness, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(entityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
                        }
                    }
                }

                if ((j >> 24 & 255) > 0)
                {
                    f8 = (float)(j >> 16 & 255) / 255.0F;
                    f9 = (float)(j >> 8 & 255) / 255.0F;
                    float f15 = (float)(j & 255) / 255.0F;
                    f10 = (float)(j >> 24 & 255) / 255.0F;
                    GL11.glColor4f(f8, f9, f15, f10);
                    this.mainModel.render(entityLivingBase, f7, f6, f4, f3 - f2, f13, f5);

                    for (int i1 = 0; i1 < 4; ++i1)
                    {
                        if (this.inheritRenderPass(entityLivingBase, i1, partialTickTime) >= 0)
                        {
                            GL11.glColor4f(f8, f9, f15, f10);
                            this.renderPassModel.render(entityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
                        }
                    }
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        catch (Exception exception)
        {
            logger.error("Couldn\'t render entity", exception);
        }

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        this.passSpecialRender(entityLivingBase, entityLivingX, entityLivingY, entityLivingZ);
        MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(entityLivingBase, this, entityLivingX, entityLivingY, entityLivingZ));
    }
}
