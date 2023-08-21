package com.lypaka.hmmanager.Listeners;

import com.lypaka.areamanager.Regions.Region;
import com.lypaka.areamanager.Regions.RegionHandler;
import com.lypaka.hmmanager.HMs.Cut.CutSettings;
import com.lypaka.hmmanager.HMs.Cut.CutTree;
import com.lypaka.hmmanager.HMs.HMHandler;
import com.lypaka.hmmanager.HMs.HMSettings;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class InteractionListener {

    @SubscribeEvent
    public void onBlockInteract (PlayerInteractEvent.RightClickBlock event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        String worldName = ((ServerWorldInfo)event.getWorld().getWorldInfo()).getWorldName();
        int x = event.getPos().getX();
        int y = event.getPos().getY();
        int z = event.getPos().getZ();
        String location = worldName + "," + x + "," + y + "," + z;
        String blockID = event.getWorld().getBlockState(event.getPos()).getBlock().getRegistryName().toString();

        Region region = RegionHandler.getRegionAtLocation(worldName, x, y, z);
        if (region == null) return;

        /** CUT */
        if (HMHandler.cutTreesByRegion.containsKey(region.getName())) {

            CutSettings cutSettings = (CutSettings) HMHandler.settingsMap.get(region.getName());
            List<CutTree> trees = HMHandler.cutTreesByRegion.get(region.getName());
            for (CutTree tree : trees) {

                if (tree.getLocations().contains(location) && cutSettings.getBlockIDs().contains(blockID)) {

                    event.setCanceled(true);
                    HMHandler.useCut(player, tree, region.getName());
                    return;

                }

            }

        }

    }

}
