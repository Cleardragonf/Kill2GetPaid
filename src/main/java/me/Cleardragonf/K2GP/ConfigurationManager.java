package me.Cleardragonf.K2GP;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Hostile;
import org.spongepowered.api.entity.living.animal.Animal;
import org.spongepowered.api.entity.living.monster.Monster;
import com.google.common.collect.ImmutableList;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigurationManager {

    private static ConfigurationManager instance = new ConfigurationManager();

    public static ConfigurationManager getInstance(){
        return instance ;
    }



    private File configDir;



    //The configuration folder for this plugi
    public void ConfigurationManager2(File configDir) {
        this.configDir = configDir;
    }


    // The config Manager for the mail storage file
    private ConfigurationLoader<CommentedConfigurationNode>configLoader1;
    private ConfigurationLoader<CommentedConfigurationNode>TimeTrackLoader;

    // the in-memory version of the mail storage file
    private CommentedConfigurationNode config1;
    private CommentedConfigurationNode TimeTracker;


    public void enable()
    {
        //setting the name of the file
        File PayDay = new File(this.configDir, "PayDay.conf");
        File TimeTrackerTime = new File(this.configDir, "TimeTracking.conf");

        this.configLoader1 = HoconConfigurationLoader.builder().setFile(PayDay).build();
        this.TimeTrackLoader = HoconConfigurationLoader.builder().setFile(TimeTrackerTime).build();

        try{
            //create a new folder if it does not exist
            if(!this.configDir.isDirectory()){
                this.configDir.mkdirs();
            }
            //create a new one if the file does not exist

            if(!TimeTrackerTime.isFile()){
                try{
                    TimeTrackerTime.createNewFile();
                    loadTime();
                    TimeTracker.getNode("========Time Tracking========").setComment("The Point of this config is to keep track of the Time and Date");
                    TimeTracker.getNode("========Time Tracking========", "Day: ").setComment("Day number in Game. Between 1-30").setValue("1");
                    TimeTracker.getNode("========Time Tracking========", "Time: ").setComment("Set the Time in Game. Between 0 - 24000").setValue("0");
                    saveTime();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(!PayDay.isFile()){
                try{
                    PayDay.createNewFile();
                    load1();

                    //create a list of Entities in Memory.
                    List<Class<? extends Entity>> classes = ImmutableList.of(
                            Animal.class, Monster.class, Hostile.class //add any interface classes I want as a part of the PayDay Config.
                    );

                    //set a collection of Entity Types from the above List of Entities using Sponge's Registry type for EntityType.
                    Collection<EntityType> cet = Sponge.getRegistry().getAllOf(EntityType.class).stream()
                            .filter(x -> classes.stream().anyMatch(y -> y.isAssignableFrom(x.getEntityClass())))
                            .collect(Collectors.toList());

                    //start building the config PayDay.conf
                    config1.getNode("=====Economy Rewards=====").setComment("This Porion is Used to Reward Players with Economy Money");

                    //for Each Entity Type found in the Registry do the following
                    for (EntityType et : cet) {
                        config1.getNode("=====Economy Rewards=====", et.getId()).setComment("How much is Killing a " + et.getName() + " worth?").setValue("5");
                    }

                    save1();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            //load the stored mails
            this.config1 = this.configLoader1.load();
            this.TimeTracker = this.TimeTrackLoader.load();
        }catch (IOException e){
            return;
        }
    }
    private void loadTime() {
        try{
            TimeTracker = TimeTrackLoader.load();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void load1(){
        try{
            config1 = configLoader1.load();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void saveTime() {
        try{
            TimeTrackLoader.save(TimeTracker);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void save1(){
        try{
            configLoader1.save(config1);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public CommentedConfigurationNode getConfig1(){
        return config1;
    }

    public CommentedConfigurationNode getTimeTrack(){
        return TimeTracker;
    }
}