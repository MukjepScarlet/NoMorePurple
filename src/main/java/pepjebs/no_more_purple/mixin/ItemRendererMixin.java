package pepjebs.no_more_purple.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pepjebs.no_more_purple.client.NoMorePurpleClientMod;

// This class lovingly yoinked (& updated to 1.18) from
// https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/mixin/client/ItemRendererMixin.java
// Published under the "CC BY-NC-SA 3.0" Creative Commons License
@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class ItemRendererMixin {

    @Redirect(method = "getSpecialFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;glint()Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private static RenderType getSpecialGlint() {
        return NoMorePurpleClientMod.getGlint();
    }

    @Redirect(method = "getSpecialFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;glintTranslucent()Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private static RenderType getSpecialGlintTranslucent() {
        return NoMorePurpleClientMod.getTranslucentGlint();
    }

    @Redirect(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;glintTranslucent()Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private static RenderType getGlintTranslucent() {
        return NoMorePurpleClientMod.getTranslucentGlint();
    }

    @Redirect(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;glint()Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private static RenderType getGlint() {
        return NoMorePurpleClientMod.getGlint();
    }

    @Redirect(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;entityGlint()Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private static RenderType getEntityGlint() {
        return NoMorePurpleClientMod.getEntityGlint();
    }

}
