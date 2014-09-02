package com.toyz.MyTokens.Events;

import com.toyz.MyTokens.MyTokens;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Created by Travis on 9/2/2014.
 */
public class SignChange implements Listener {
    @EventHandler
    public void on(SignChangeEvent e) {
        if (e.isCancelled())
            return;

        if(e.getPlayer().hasPermission("mytokens.sign.place")){
            int max = e.getLines().length;
            if(max >= 4){
                 if(e.getLine(0).equalsIgnoreCase("[MyTokens]") || e.getLine(0).equalsIgnoreCase("[MYT]")){
                     String last = e.getLine(3).replace(" ", "");
                     e.setLine(3, last);
                     e.getPlayer().sendMessage(MyTokens.getAPI().getMessageHelper().format(e.getPlayer(), "Placed Tokens shop at"));
                 }
            }
        }else{
            e.setCancelled(true);
            return;
        }
    }
}
