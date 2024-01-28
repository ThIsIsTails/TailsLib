package ru.thisistails.tailslib.CustomItems;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

/**
 * Простой строитель описаний.
 */
public class SimpleDescBuilder implements IDescBuilder {

    private List<Component> desc, buffs, debuffs;

    public SimpleDescBuilder() {
        this.desc = new ArrayList<>();
        this.buffs = new ArrayList<>();
        this.debuffs = new ArrayList<>();
    }

    private Component other;

    public SimpleDescBuilder setOther(String o) {
        other = Component.text(ChatColor.translateAlternateColorCodes('&', "&f" + o));
        return this;
    }

    public SimpleDescBuilder addDesc(String description) {
        desc.add(Component.text(ChatColor.translateAlternateColorCodes('&', "&f" + description)));
        return this;
    }

    public SimpleDescBuilder addBuff(String buff) {
        buffs.add(Component.text(ChatColor.translateAlternateColorCodes('&', "&2&l*" + buff)));
        return this;
    }

    public SimpleDescBuilder addDebuff(String debuff) {
        debuffs.add(Component.text(ChatColor.translateAlternateColorCodes('&', "&c&l*" + debuff)));
        return this;
    }

    @Override
    public List<Component> build() {
        List<Component> components = new ArrayList<>();

        if (!desc.isEmpty())
            components.addAll(desc);
        if (!buffs.isEmpty() || !debuffs.isEmpty())
            components.add(Component.text(" "));
        if (!buffs.isEmpty()) {
            components.addAll(buffs);
        }
        if (!debuffs.isEmpty())
            components.addAll(debuffs);
        
        if (!(other == null)){
            components.add(Component.text(" "));
            components.add(other);
        }

        return components;
    }

}
