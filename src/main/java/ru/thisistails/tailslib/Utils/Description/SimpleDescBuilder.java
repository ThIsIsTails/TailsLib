package ru.thisistails.tailslib.Utils.Description;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;

/**
 * Simple description builder.
 */
public class SimpleDescBuilder implements IDescBuilder {

    private final List<String> desc;
    private final List<String> buffs;
    private final List<String> debuffs;
    private String other;

    public SimpleDescBuilder() {
        this.desc = new ArrayList<>();
        this.buffs = new ArrayList<>();
        this.debuffs = new ArrayList<>();
        this.other = "";
    }

    public SimpleDescBuilder setOther(@NotNull String o) {
        other = ChatColor.translateAlternateColorCodes('&', "&f" + o);
        return this;
    }

    public SimpleDescBuilder addDesc(@NotNull String description) {
        desc.add(ChatColor.translateAlternateColorCodes('&', "&f" + description));
        return this;
    }

    public SimpleDescBuilder addDescs(@NotNull List<String> descriptions) {
        descriptions.forEach(this::addDesc);
        return this;
    }

    public SimpleDescBuilder addBuff(@NotNull String buff) {
        buffs.add(ChatColor.translateAlternateColorCodes('&', "&2&l*" + buff));
        return this;
    }

    public SimpleDescBuilder addBuffs(@NotNull List<String> buffs) {
        buffs.forEach(this::addBuff);
        return this;
    }

    public SimpleDescBuilder addDebuff(@NotNull String debuff) {
        debuffs.add(ChatColor.translateAlternateColorCodes('&', "&c&l*" + debuff));
        return this;
    }

    public SimpleDescBuilder addDebuffs(@NotNull List<String> debuffs) {
        debuffs.forEach(this::addDebuff);
        return this;
    }

    @Override
    public List<String> build() {
        List<String> components = new ArrayList<>();

        if (!desc.isEmpty()) {
            components.addAll(desc);
        }
        if (!buffs.isEmpty() || !debuffs.isEmpty()) {
            if (!components.isEmpty()) {
                components.add(" ");
            }
            components.addAll(buffs);
            components.addAll(debuffs);
        }
        if (!other.isEmpty()) {
            if (!components.isEmpty()) {
                components.add(" ");
            }
            components.add(other);
        }

        return components;
    }

}
