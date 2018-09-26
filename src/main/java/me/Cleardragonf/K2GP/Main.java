package me.Cleardragonf.K2GP;

import java.io.File;
import java.util.logging.Logger;

import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.inject.Inject;

@Plugin (id="kill2gpaid", name="Kill2GPaid", version="Beta 1.0.0")
public class Main {

    /**/
    public static final String FILE_LOCATION = "mods/HellOnSpoangee/portals.dat";
    public static final String NAME = "[HOS]";

    @Inject
    private Logger logger;

    @Inject
    Game game;

    @Inject
    private void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    public Logger getLogger()
    {
        return this.logger;
    }

    private static Main instance;

    public static Main getInstance(){
        return instance;
    }
    @Inject public  PluginContainer pluginContainer;
    public  PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;



    @Listener
    public void allhands(GamePreInitializationEvent event){
        ConfigurationManager.getInstance().ConfigurationManager2(configDir);
        ConfigurationManager.getInstance().enable();
        instance = this;
    }

    @Listener
    public void trialbyFire(GameInitializationEvent event){
        Sponge.getEventManager().registerListeners(this, new EcoRewards());
    }

    @Listener
    public void reload(GameReloadEvent event){

    }

    private static EconomyService economyService;
    @Listener
    public void onChangeServiceProvider(ChangeServiceProviderEvent event){
        if(event.getService().equals(EconomyService.class)){
            economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
        }
        Sponge.getGame().getServer().getBroadcastChannel().send(Text.of(TextColors.GOLD, "Change Service is working"));

    }
    public static EconomyService getEcon(){
        return economyService;
    }

}