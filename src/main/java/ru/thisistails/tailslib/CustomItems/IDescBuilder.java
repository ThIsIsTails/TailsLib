package ru.thisistails.tailslib.CustomItems;

import java.util.List;

import net.kyori.adventure.text.Component;

/**
 * Билдер описаний предметов.
 * Каждый предмет может иметь разный IDescBuilder.
 */
public interface IDescBuilder {
    
    public List<Component> build();

}
