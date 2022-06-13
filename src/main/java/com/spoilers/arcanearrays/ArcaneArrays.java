package com.spoilers.arcanearrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.Spoilers.ArcaneArrays.ArcaneArrays;
//import com.Spoilers.ArcaneArrays.blocks.AFBlocks;
//import com.Spoilers.ArcaneArrays.commands.CommandArgumentInit;
//import com.Spoilers.ArcaneArrays.commands.CommandInit;
//import com.Spoilers.ArcaneArrays.config.AFConfigInit;
//import com.Spoilers.ArcaneArrays.entities.AFEntities;
//import com.Spoilers.ArcaneArrays.entities.AFEntityRenderers;
//import com.Spoilers.ArcaneArrays.eventhandlers.CatchThrownRune;
//import com.Spoilers.ArcaneArrays.eventhandlers.GetIfCandleArea;
//import com.Spoilers.ArcaneArrays.eventhandlers.MonocleCraftingHandler;
//import com.Spoilers.ArcaneArrays.eventhandlers.WandCodexAlternate;
//import com.Spoilers.ArcaneArrays.gui.CandleRenderer;
//import com.Spoilers.ArcaneArrays.gui.MonocleRenderer;
//import com.Spoilers.ArcaneArrays.items.AFItems;
//import com.Spoilers.ArcaneArrays.rituals.RitualEffectCatharsis;
//import com.Spoilers.ArcaneArrays.rituals.RitualEffectDistension;
//import com.Spoilers.ArcaneArrays.rituals.RitualEffectTreason;
//import com.Spoilers.ArcaneArrays.rituals.RitualEffectUnspelling;
import com.mna.api.guidebook.RegisterGuidebooksEvent;
import com.mna.api.rituals.RitualEffect;
import com.spoilers.arcanearrays.blocks.AABlocks;
import com.spoilers.arcanearrays.capabilities.CapabilityRegistry;
import com.spoilers.arcanearrays.handlers.TestHandler;
import com.spoilers.arcanearrays.rituals.RitualEffectTesting;

//import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.client.renderer.RenderTypeLookup;
//import net.minecraft.world.item.ItemGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
//import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import net.minecraftforge.fml.loading.FMLPaths;

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
        AABlocks.BLOCKS.register(this.modEventBus);
//        AFEntities.ENTITY_TYPES.register(this.modEventBus);

        //MinecraftForge.EVENT_BUS.register(new WandCodexAlternate());
        //MinecraftForge.EVENT_BUS.register(new MonocleCraftingHandler());
        //MinecraftForge.EVENT_BUS.register(new CatchThrownRune());
        MinecraftForge.EVENT_BUS.register(new TestHandler());
        //MinecraftForge.EVENT_BUS.register(CommandInit.class);
        modEventBus.register(new CapabilityRegistry());
        MinecraftForge.EVENT_BUS.register(this);

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
//        RenderTypeLookup.setRenderLayer(AFBlocks.DESERT_NOVA_CROP.get(), RenderType.cutout());
//        RenderTypeLookup.setRenderLayer(AFBlocks.TARMA_ROOT_CROP.get(), RenderType.cutout());
//        RenderTypeLookup.setRenderLayer(AFBlocks.WAKEBLOOM_CROP.get(), RenderType.cutout());
//        RenderTypeLookup.setRenderLayer(AFBlocks.AUM_CROP.get(), RenderType.cutout());
//        RenderTypeLookup.setRenderLayer(AFBlocks.CERUBLOSSOM_CROP.get(), RenderType.cutout());
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("ArcaneArraysTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.EMERALD);
        }
    };

    @Mod.EventBusSubscriber(modid = ArcaneArrays.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onRegisterRituals(RegistryEvent.Register<RitualEffect> event) {
            event.getRegistry().register(new RitualEffectTesting(new ResourceLocation(ArcaneArrays.MOD_ID, "rituals/test"))
                    .setRegistryName(new ResourceLocation(ArcaneArrays.MOD_ID, "ritual-effect-test")));
            ArcaneArrays.LOGGER.info("Arcane Arrays: rituals registered");
        }
    }
}
