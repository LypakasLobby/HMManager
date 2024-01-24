package com.lypaka.hmmanager.Commands;

import com.lypaka.hmmanager.Commands.HMCommands.DefogCommand;
import com.lypaka.hmmanager.Commands.HMCommands.DiveCommand;
import com.lypaka.hmmanager.HMManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = HMManager.MOD_ID)
public class HMManagerCommand {

    public static final List<String> ALIASES = Arrays.asList("hmmanager", "hms", "hmanager");

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new DefogCommand(event.getDispatcher());
        new DiveCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
