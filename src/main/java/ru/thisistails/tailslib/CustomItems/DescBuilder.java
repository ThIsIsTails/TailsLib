package ru.thisistails.tailslib.CustomItems;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class DescBuilder implements IDescBuilder {

    private List<Component> desc, buffs, debuffs;
    public DescBuilder() {
        this.desc = new ArrayList<>();
        this.buffs = new ArrayList<>();
        this.debuffs = new ArrayList<>();
    }

    private Component other;

    public void setOther(String o) {
        other = Component.text(ChatColor.translateAlternateColorCodes('&', o));
    }

    public void addDesc(String description) {
        desc.add(Component.text(description));
    }

    public void addBuff(String buff) {
        buffs.add(Component.text(ChatColor.translateAlternateColorCodes('&', "&2&l*" + buff)));
    }

    public void addDebuff(String debuff) {
        debuffs.add(Component.text(ChatColor.translateAlternateColorCodes('&', "&2&l*" + debuff)));
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
