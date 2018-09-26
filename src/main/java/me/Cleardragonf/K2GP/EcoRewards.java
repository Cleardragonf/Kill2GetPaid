package me.Cleardragonf.K2GP;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.explosive.PrimedTNT;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EcoRewards {

    @Listener
    public void derp(DamageEntityEvent event, @First PrimedTNT src) {
        String KillerA = src.toString();
        Sponge.getGame().getServer().getBroadcastChannel().send(Text.of(KillerA + "I saw that!"));


      //Sponge.getGame().getServer().getBroadcastChannel().send(Text.of(("*****Starting Damage Event*****")));
      //int i= 0;
      //for (Object causeObject : event.getCause()) {
    	//Sponge.getGame().getServer().getBroadcastChannel().send(Text.of(" - " + i++ + causeObject.toString()));
      //}
      //for (Map.Entry<EventContextKey<?>, Object> entry : event.getContext().asMap().entrySet()) {
    	//  Sponge.getGame().getServer().getBroadcastChannel().send(Text.of("- " + entry.getKey().getId() + " : " + entry.getValue()));
      //}
    }


    @Listener
    public void MobDeath(DestructEntityEvent.Death event, @First EntityDamageSource src){
        Entity killer = src.getSource();


        // not currently used except against other players************
        //if (event.getTargetEntity() instanceof Player){
        //	String a = event.getClass().getName().toString();
        //	Sponge.getServer().getBroadcastChannel().send(Text.of("HIi " + a));
        //}

        //if the item that kills a mob is a projectile ie arrow etc.
        if (src.getSource() instanceof Projectile){
            Projectile projectile = (Projectile) src.getSource();
            Optional<UUID> arrow = projectile.getCreator();

            if(!arrow.isPresent()){
                return;
            }

            Optional<Player> optionalPlayer = Sponge.getGame().getServer().getPlayer(arrow.get());

            if(!optionalPlayer.isPresent()){
                return;
            }

            Player player = optionalPlayer.get();

            UUID player2 = player.getUniqueId();

            String entity = event.getTargetEntity().getType().getName();
            String entity2 = event.getTargetEntity().getType().getId();
            Cause cause = event.getCause();
            BigDecimal bd = new BigDecimal(ConfigurationManager.getInstance().getConfig1().getNode("=====Economy Rewards=====", entity2).getString());
            player.sendMessage(Text.of("You Killed a " + entity + "  & earned " + bd));
            Main.getEcon().getOrCreateAccount(player2).get().deposit(Main.getEcon().getDefaultCurrency(), bd, cause);

        }

        else if ((killer instanceof Player)){

            Player player = (Player)killer;
            UUID player2 = player.getUniqueId();


            String entity = event.getTargetEntity().getType().getName();
            String entity2 = event.getTargetEntity().getType().getId();
            Cause cause = event.getCause();
            BigDecimal bd = new BigDecimal(ConfigurationManager.getInstance().getConfig1().getNode("=====Economy Rewards=====", entity2).getString());
            player.sendMessage(Text.of("You Killed a " + entity + "  & earned " + bd));
            Main.getEcon().getOrCreateAccount(player2).get().deposit(Main.getEcon().getDefaultCurrency(), bd, cause);

        }else{
            //USED TO DETECT OTHERWAYS OF KILLING SOMETHING?
            //Sponge.getGame().getServer().getBroadcastChannel().send(Text.of(TextColors.DARK_BLUE, "Let's add this as a source..." + killer));
        }
        //String entity = event.getTargetEntity().getType().getName();
        //player.sendMessage(Text.of(entity));

    }
}