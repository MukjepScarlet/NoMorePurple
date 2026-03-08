package pepjebs.no_more_purple.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntRBTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntSortedMaps;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.item.DyeColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pepjebs.no_more_purple.config.NoMorePurpleConfig;

import java.util.List;

public class NoMorePurpleClientMod implements ClientModInitializer {

    public static final String MOD_ID = "no_more_purple";
    public static final String COMMAND_ID = "glint_color";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static NoMorePurpleConfig CONFIG = null;

    static final List<String> ALL_GLINT_COLORS_WITHOUT_OFF;
    private static final List<String> ALL_GLINT_COLORS;

    static {
        final var colors = DyeColor.values();
        final var array = new String[colors.length + 4];
        int i = 0;
        for (; i < colors.length; i++) {
            array[i] = colors[i].getName();
        }
        array[i++] = "rainbow";
        array[i++] = "light";
        array[i++] = "none";
        array[i] = "off";
        ALL_GLINT_COLORS_WITHOUT_OFF = new ObjectImmutableList<>(array, 0, array.length - 1);
        ALL_GLINT_COLORS = new ObjectImmutableList<>(array);
    }

    private static final Object2IntMap<String> COLOR_NAME_TO_ID;

    static {
        final var map = new Object2IntRBTreeMap<>(String.CASE_INSENSITIVE_ORDER);
        map.defaultReturnValue(-1);
        final var colors = DyeColor.values();
        map.put("rainbow", colors.length);
        map.put("light", colors.length + 1);
        map.put("none", colors.length + 2);
        map.put("off", -1);
        for (final var color : colors) {
            map.put(color.getName(), color.getId());
        }
        COLOR_NAME_TO_ID = Object2IntSortedMaps.unmodifiable(map);
    }

    @Override
    public void onInitializeClient() {
        // Register config
        AutoConfig.register(NoMorePurpleConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(NoMorePurpleConfig.class).getConfig();

        // Register the "/glintcolor <color>" command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommandManager.literal(COMMAND_ID)
                            .then(ClientCommandManager.argument(COMMAND_ID, StringArgumentType.string())
                                .suggests(new GlintColorSuggestionProvider())
                                .executes(context -> {
                                    CONFIG.glintColor = context.getArgument(COMMAND_ID, String.class);
                                    AutoConfig.getConfigHolder(NoMorePurpleConfig.class).save();
                                    return 0;
                                })
                            )
            );
        });
    }

    public static List<String> listGlintColors() {
        return ALL_GLINT_COLORS;
    }

    private static int changeColor() {
        String confColor = CONFIG.glintColor.toLowerCase();
        return COLOR_NAME_TO_ID.getOrDefault(confColor, -1);
    }

    @Environment(EnvType.CLIENT)
    public static RenderType getGlint() {
        int color = changeColor();
        if (color == -1)
            return RenderTypes.glint();
        return GlintRenderLayer.glintColor.get(color);
    }

    @Environment(EnvType.CLIENT)
    public static RenderType getEntityGlint() {
        int color = changeColor();
        if (color == -1)
            return RenderTypes.entityGlint();
        return GlintRenderLayer.entityGlintColor.get(color);
    }

    @Environment(EnvType.CLIENT)
    public static RenderType getTranslucentGlint() {
        int color = changeColor();
        if (color == -1)
            return RenderTypes.glintTranslucent();
        return GlintRenderLayer.translucentGlintColor.get(color);
    }

    @Environment(EnvType.CLIENT)
    public static RenderType getArmorEntityGlint() {
        int color = changeColor();
        if (color == -1)
            return RenderTypes.armorEntityGlint();
        return GlintRenderLayer.armorGlintColor.get(color);
    }
}
