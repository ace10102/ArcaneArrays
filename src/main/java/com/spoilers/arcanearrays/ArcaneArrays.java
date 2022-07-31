package com.spoilers.arcanearrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mna.api.guidebook.RegisterGuidebooksEvent;
import com.spoilers.arcanearrays.arrays.ArrayModuleRegistry;
import com.spoilers.arcanearrays.arrays.ArrayModulesInit;
import com.spoilers.arcanearrays.blocks.AABlockRegistry;
import com.spoilers.arcanearrays.blocks.tiles.AABlockEntityRegistry;
import com.spoilers.arcanearrays.capabilities.CapabilityRegistry;
import com.spoilers.arcanearrays.handlers.ArrayExecutionHandlers;
import com.spoilers.arcanearrays.rituals.AARitualRegistry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("arcanearrays")
public class ArcaneArrays {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "arcanearrays";
    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public ArcaneArrays() {

        //ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, AFConfigInit.SERVER_CONFIG);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AFConfigInit.CLIENT_CONFIG);

//        AFConfigInit.loadConfig(AFConfigInit.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve("ArcaneArrays-server.toml"));
//        AFConfigInit.loadConfig(AFConfigInit.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("ArcaneArrays-client.toml"));
//
//        AFItems.ITEMS.register(this.modEventBus);
        AABlockRegistry.BLOCKS.register(this.modEventBus);
        AABlockEntityRegistry.TILE_ENTITIES.register(this.modEventBus);

        MinecraftForge.EVENT_BUS.register(new ArrayExecutionHandlers());
        //MinecraftForge.EVENT_BUS.register(CommandInit.class);
        modEventBus.register(new CapabilityRegistry());
        modEventBus.register(ArrayModuleRegistry.class);
        modEventBus.register(ArrayModulesInit.class);
        modEventBus.register(AARitualRegistry.class);
        MinecraftForge.EVENT_BUS.register(this);
        
        //ArrayModuleRegistry.Functions.get().forEach(function -> MinecraftForge.EVENT_BUS.register(function.getClass()));

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

            //MinecraftForge.EVENT_BUS.register(new HUDRenderer());
            //MinecraftForge.EVENT_BUS.register(new CandleRenderer());
            //MinecraftForge.EVENT_BUS.register(new MonocleRenderer());
            // MinecraftForge.EVENT_BUS.register(new TooltipHandler());

            modEventBus.addListener(this::clientSetupStuff);
            //modEventBus.register(AFEntityRenderers.class);
        });
    }

    @SubscribeEvent
    public void onRegisterGuidebooks(RegisterGuidebooksEvent event) {
        event.getRegistry().addGuidebookPath(new ResourceLocation(ArcaneArrays.MOD_ID, "guide"));
        ArcaneArrays.LOGGER.info("Arcane Arrays: guide registered");
    }

    private void clientSetupStuff(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(AABlockRegistry.ARRAY_CORE.get(), RenderType.cutout());
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("ArcaneArraysTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.EMERALD);
        }
    };
}
