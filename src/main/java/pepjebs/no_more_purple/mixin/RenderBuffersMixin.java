package pepjebs.no_more_purple.mixin;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.no_more_purple.client.GlintRenderLayer;

@Mixin(RenderBuffers.class)
@Environment(EnvType.CLIENT)
public class RenderBuffersMixin {

    @Inject(method = "put", at = @At("HEAD"))
    private static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> mapBuildersIn, RenderType renderTypeIn, CallbackInfo callbackInfo) {
        GlintRenderLayer.addGlintTypes(mapBuildersIn);
    }
}
