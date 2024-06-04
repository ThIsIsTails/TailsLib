package ru.thisistails.tailslib.Utils.Description;

import java.util.List;

import ru.thisistails.tailslib.CustomItems.Data.CustomItemData;

/**
 * Builder for description used in some classes like {@link CustomItemData#getLore()}
 */
public interface IDescBuilder {
    
    public List<String> build();

}
