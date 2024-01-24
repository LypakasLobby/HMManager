package com.lypaka.hmmanager.Listeners;

import com.lypaka.hmmanager.HMManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = HMManager.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) {

        MinecraftForge.EVENT_BUS.register(new AreaLeaveListener());
        MinecraftForge.EVENT_BUS.register(new InteractionListener());
        MinecraftForge.EVENT_BUS.register(new MoveListener());

    }

}
