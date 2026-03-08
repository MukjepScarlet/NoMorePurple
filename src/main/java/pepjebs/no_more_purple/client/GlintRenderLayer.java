package pepjebs.no_more_purple.client;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.*;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.Function;

// This class lovingly yoinked (& updated to 1.18-1.20) from
// https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/content/tools/client/GlintRenderType.java
// Published under the "CC BY-NC-SA 3.0" Creative Commons License
@Environment(EnvType.CLIENT)
public class GlintRenderLayer {

    public static final List<RenderType> glintColor = newRenderList(GlintRenderLayer::buildGlintRenderLayer);
    public static final List<RenderType> entityGlintColor = newRenderList(GlintRenderLayer::buildEntityGlintRenderLayer);

    public static final List<RenderType> armorGlintColor = newRenderList(GlintRenderLayer::buildArmorGlintRenderLayer);

    public static final List<RenderType> translucentGlintColor = newRenderList(GlintRenderLayer::buildTranslucentGlint);

    public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> map) {
        addGlintTypes(map, glintColor);
        addGlintTypes(map, entityGlintColor);
        addGlintTypes(map, armorGlintColor);
        addGlintTypes(map, translucentGlintColor);
    }

    private static List<RenderType> newRenderList(Function<String, RenderType> func) {
        final var colorNames = NoMorePurpleClientMod.ALL_GLINT_COLORS_WITHOUT_OFF;
        final var array = new RenderType[colorNames.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = func.apply(colorNames.get(i));
        }

        return new ObjectImmutableList<>(array);
    }

    public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> map, List<RenderType> typeList) {
        for (RenderType renderType : typeList) {
            map.computeIfAbsent(renderType, (Function<RenderType, ByteBufferBuilder>) type -> new ByteBufferBuilder(type.bufferSize()));
        }
    }

    private static RenderType buildGlintRenderLayer(String name) {
        final Identifier res = Identifier.fromNamespaceAndPath(
                NoMorePurpleClientMod.MOD_ID,
                "textures/misc/glint_" + name + ".png"
        );

        var renderSetup = RenderSetup.builder(RenderPipelines.GLINT)
                .withTexture("Sampler0", res)
                .setTextureTransform(TextureTransform.GLINT_TEXTURING)
                .createRenderSetup();
        return RenderType.create("glint_" + name, renderSetup);
    }

    private static RenderType buildEntityGlintRenderLayer(String name) {
        final Identifier res = Identifier.fromNamespaceAndPath(
                NoMorePurpleClientMod.MOD_ID,
                "textures/misc/glint_" + name + ".png"
        );

        var renderSetup = RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT)
                .withTexture("Sampler0", res)
                .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
                .createRenderSetup();
        return RenderType.create("entity_glint_" + name, renderSetup);
    }

    private static RenderType buildArmorGlintRenderLayer(String name) {
        final Identifier res = Identifier.fromNamespaceAndPath(
                NoMorePurpleClientMod.MOD_ID,
                "textures/misc/glint_" + name + ".png"
        );

        var renderSetup = RenderSetup.builder(RenderPipelines.GLINT)
                .withTexture("Sampler0", res)
                .setTextureTransform(TextureTransform.ARMOR_ENTITY_GLINT_TEXTURING)
                .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .createRenderSetup();
        return RenderType.create("armor_glint_" + name, renderSetup);
    }

    private static RenderType buildTranslucentGlint(String name) {
        final Identifier res = Identifier.fromNamespaceAndPath(
                NoMorePurpleClientMod.MOD_ID,
                "textures/misc/glint_" + name + ".png"
        );

        var renderSetup = RenderSetup.builder(RenderPipelines.GLINT)
                .withTexture("Sampler0", res)
                .setTextureTransform(TextureTransform.GLINT_TEXTURING)
                .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .createRenderSetup();
        return RenderType.create("glint_translucent_" + name, renderSetup);
    }
}
