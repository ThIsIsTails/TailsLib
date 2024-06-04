# TailsLib

TailsLib library for plugins. This library allows you to do custom things that cannot be done with the vanilla Bukkit API.

## TailsLib does not guarantee full API functionality if plugins use an older version of TailsLib, as the library changes dramatically with each update.

# bStats
![TailsLib bStats](https://bstats.org/signatures/bukkit/TailsLib.svg)

# What it can do?

##  Custom items
Create your items without pain and give them crafts without much pain too.

## Custom block (with saves)
Blocks that remain in your world even after a reboot and can offer basic needs in the form of eventualities with clicking on a block, breaking it, and setting it down.

## Custom effects with more functionality (Save system in WIP)
Create new effects without pain (almost)

# Maven & Gradle

Still searching for host (:

# How to use it

Simply implement tailslib interface into your class.

```java
...
public class MyItem implements CustomItem {
    @Override
    public void getItemData() {
        // Adding simple description to our item
        SimpleDescBuilder description = new SimpleDescBuilder()
            .addDesc("My awesome description!");
        
        /*
        You can also use List for description.
        Like: ArrayList<String> list = Arrays.asList("First", "second");
        */
        CustomItemData data = new CustomItemData("item_id", "Item name", description, Material.CLOCK);

        // Should have unique ID?
        // Default: true
        data.setShouldBeUnique(true);

        // Can be used in default crafts? (Like iron sword and etc...)
        // Default: true
        data.setAsUniqueMaterial(true);
        
        return data;
    }

    @Override
    public void rightClick(PlayerInteractEvent event, UUID uuid) {
        Player player = event.getPlayer();
        player.sendMessage("Right click!");
    }
}
```

TailsLib in general uses a system of data between the whole system. So data classes can be seen everywhere.

Then register it
```java
...
@Override
public void onEnable() {
    ...
    CustomItemManager cManager = CustomItemManager.getManager();
    cManager.register(new MyItem());
    ...
}
...
```
In any case, such a scheme is basically for everything. Effects and blocks work according to the same scheme and are created in the same way.
But if you want more examples visit [this site.](https://thisistails.gitbook.io/tailslib-simple-wiki/)

# Can't code?

I'm currently working on `TailsLib simplify` so that those who can't write their own plugin can do everything through the configs.

# Known issues

* Poor PAPi support (WIP)
* May some old code poor quality or not documented code
