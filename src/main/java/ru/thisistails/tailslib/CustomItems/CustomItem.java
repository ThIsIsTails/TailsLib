package ru.thisistails.tailslib.CustomItems;

import java.util.List;

import net.kyori.adventure.text.Component;
import ru.thisistails.tailslib.Tools.DamageType;

public interface CustomItem {
    
    // Айди предмета
    public String getId();
    // Имя предмета в игре
    public String getName();
    // Описание предмета в игре
    public List<Component> getLore();
    // Имеет ли рандомные криты
    public boolean hasRandomCrits();
    // Урон предмета
    public int getDamage();
    // Тип урона
    public DamageType getDamageType();

}
