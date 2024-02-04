package ru.thisistails.tailslib.CustomItems;

import java.util.List;

/**
 * Билдер описаний предметов.
 * Каждый предмет может иметь разный IDescBuilder.
 */
public interface IDescBuilder {
    
    public List<String> build();

}
