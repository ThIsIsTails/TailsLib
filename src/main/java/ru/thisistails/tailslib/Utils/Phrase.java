package ru.thisistails.tailslib.Utils;

import ru.thisistails.tailslib.Tools.YAMLManager;

/**
 * Create locale.yml or assign file with {@link #setPath(String)} for your plugin to work with this class.
 * By default file with phrases is locale.yml.
 * <p>Basicly helps you with setting up your phrases.</p>
 */
public class Phrase {

    private String plugin, filePath;
    
    public Phrase(String pluginName) {
        this.plugin = pluginName;
        this.filePath = "locale.yml";
    }

    public String getPhrase(String path) {
        return YAMLManager.getAndTranslateString(plugin, filePath, path);
    }

    public void setPath(String path) {
        this.filePath = path;
    }

}
