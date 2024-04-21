package ru.thisistails.tailslib.CustomItems;

import lombok.Getter;
import ru.thisistails.tailslib.Tools.ChatTools;

/**
 * Позволяет назначить доп. настройки для предмета.
 */
public enum CustomItemFlag {
    /**
     * Обозначает этот предмет как уникальный. Уникальные предметы перестают быть использованы в крафтах с таким же материалом.
     */
    AsUniqueMaterial(ChatTools.translateString("&8Уникальный материал")),
    /**
     * Отключает любые действия с предметом
     */ 
    DisableActions(ChatTools.translateString("&8Взаимодействия с миров отключены")),
    /**
     *  Отключает PVP с этим предметом
    */
    NoPVP(ChatTools.translateString("&8Не наносит урон игрокам")),
    /**
     * Отключает чары у предмета
     */
    NoEchants(ChatTools.translateString("&8Не может быть зачарован"));

    private @Getter String localized;

    CustomItemFlag(String localized) {
        this.localized = localized;
    }
}
