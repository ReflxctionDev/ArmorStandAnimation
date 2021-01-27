# ArmorStandAnimation
[![](https://jitpack.io/v/ReflxctionDev/ArmorStandAnimation.svg)](https://jitpack.io/#ReflxctionDev/ArmorStandAnimation)

A small framework for playing armor stand animations generated from Blender

# Example usage
```java

    // animations are immutable, hence are thread-safe, so we can store them long term.
    private static final ArmorStandAnimation ANIMATION = AnimationFileParser
               .parse(new File(("somewhere/animation.mcfunction")));

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ArmorStand armorStand = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), ArmorStand.class);
        armorStand.setBasePlate(false);
        armorStand.setArms(true);

        AnimationController controller = ANIMATION.play(plugin, armorStand);
        // or
        AnimationController controller = ANIMATION.playAsync(plugin, armorStand);
        
        controller.pause();
        controller.resume();
        ...
    }
    
```
