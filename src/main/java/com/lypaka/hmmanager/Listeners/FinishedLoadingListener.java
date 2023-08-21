package com.lypaka.hmmanager.Listeners;

import com.lypaka.areamanager.API.FinishedLoadingEvent;
import com.lypaka.hmmanager.HMManager;
import com.lypaka.hmmanager.HMs.HMHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@Mod.EventBusSubscriber(modid = HMManager.MOD_ID)
public class FinishedLoadingListener {

    @SubscribeEvent
    public static void onAreaManagerLoaded (FinishedLoadingEvent event) throws ObjectMappingException {

        HMHandler.loadHMs();

    }

}
