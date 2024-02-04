package ru.thisistails.tailslib.CustomItems;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

/**
 * Простой строитель описаний.
 */
public class SimpleDescBuilder implements IDescBuilder {

    private List<String> desc, buffs, debuffs;

    public SimpleDescBuilder() {
        this.desc = new ArrayList<>();
        this.buffs = new ArrayList<>();
        this.debuffs = new ArrayList<>();
    }

    private String other;

    public SimpleDescBuilder setOther(String o) {
        other = ChatColor.translateAlternateColorCodes('&', "&f" + o);
        return this;
    }

    public SimpleDescBuilder addDesc(String description) {
        desc.add(ChatColor.translateAlternateColorCodes('&', "&f" + description));
        return this;
    }

    public SimpleDescBuilder addBuff(String buff) {
        buffs.add(ChatColor.translateAlternateColorCodes('&', "&2&l*" + buff));
        return this;
    }

    public SimpleDescBuilder addDebuff(String debuff) {
        debuffs.add(ChatColor.translateAlternateColorCodes('&', "&c&l*" + debuff));
        return this;
    }

    @Override
    public List<String> build() {
        List<String> components = new ArrayList<>();

        if (!desc.isEmpty())
            components.addAll(desc);
        if (!buffs.isEmpty() || !debuffs.isEmpty())
            components.add(" ");
        if (!buffs.isEmpty()) {
            components.addAll(buffs);
        }
        if (!debuffs.isEmpty())
            components.addAll(debuffs);
        
        if (!(other == null)){
            components.add(" ");
            components.add(other);
        }

        return components;
    }

}
