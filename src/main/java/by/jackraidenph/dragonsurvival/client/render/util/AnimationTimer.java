package by.jackraidenph.dragonsurvival.client.render.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@EventBusSubscriber
public class AnimationTimer {
    protected ConcurrentHashMap<String, Double> animationTimes = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<AnimationTimer> timers = new CopyOnWriteArrayList<>();

    @OnlyIn( Dist.CLIENT)
    @SubscribeEvent
    public static void renderTick(ClientTickEvent event){
        if(event.phase == Phase.START) return;
        
        for(AnimationTimer timer : timers){
            timer.animationTimes.keySet().forEach((key) -> {
                timer.animationTimes.computeIfPresent(key, (s, d) -> d -= 1);
                
                if(timer.animationTimes.get(key) <= 0){
                    timer.animationTimes.remove(key);
                }
            });
            
            if(timer.animationTimes.size() <= 0){
                timers.remove(timer);
            }
        }
    }
    
    protected void putDuration(String animation, Double ticks) {
        animationTimes.put(animation, ticks);
    }
    
    public double getDuration(String animation) {
        return animationTimes.getOrDefault(animation, 0.0);
    }

    public void putAnimation(String animation, Double ticks, AnimationBuilder builder) {
        builder.addAnimation(animation);
        putDuration(animation, ticks);
        
        if(!timers.contains(this)){
            timers.add(this);
        }
    }
}
