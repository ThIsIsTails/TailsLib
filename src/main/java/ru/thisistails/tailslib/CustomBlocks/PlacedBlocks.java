package ru.thisistails.tailslib.CustomBlocks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.ToString;
import ru.thisistails.tailslib.CustomBlocks.Data.PlacedBlockData;
import ru.thisistails.tailslib.CustomBlocks.GsonAdapters.PlacedBlocksSer;

@ToString
public class PlacedBlocks {

    private transient static final @Getter String saveFilePath = Bukkit.getPluginManager().getPlugin("TailsLib").getDataFolder().getAbsolutePath() + "/placedBlocks.json";

    public PlacedBlocks(List<PlacedBlockData> data) {
        placedBlocks = data;
    }

    public PlacedBlocks() {}

    private @Getter List<PlacedBlockData> placedBlocks = new ArrayList<>();

    protected void save() throws IOException {
        PlacedBlocks data = this;
        Gson gson = new GsonBuilder().setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .registerTypeAdapter(PlacedBlocks.class, new PlacedBlocksSer())
        .create();

        String json = gson.toJson(data);

        File file = new File(saveFilePath);

        if (!file.exists()) file.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(json);
        writer.close();
    }

    protected static PlacedBlocks load() {
        PlacedBlocks object = null;
        Gson gson = new GsonBuilder().setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .registerTypeAdapter(PlacedBlocks.class, new PlacedBlocksSer())
        .create();

        try {
            String json = readFromInputStream(new FileInputStream(new File(saveFilePath)));
            object = gson.fromJson(json, PlacedBlocks.class);
            Bukkit.getLogger().info(json);
            Bukkit.getLogger().info(object.toString());
        } catch (IOException error) {
            error.printStackTrace();
        }

        return object;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
        = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
    return resultStringBuilder.toString();
    }

    public void addPlacedBlock(PlacedBlockData data) {
        placedBlocks.add(data);
    }

    public void removePlacedBlock(PlacedBlockData data) {
        placedBlocks.remove(data);
    }

    public boolean containsPlacedBlock(PlacedBlockData data) {
        return placedBlocks.contains(data);
    }

    public @Nullable PlacedBlockData searchPlacedBlockData(Location location) {
        for (PlacedBlockData data : placedBlocks) {
            if (data.getLocation().equals(location))
                return data;
        }

        return null;
    }
}
