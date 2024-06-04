package ru.thisistails.tailslib.CustomBlocks.GsonAdapters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import ru.thisistails.tailslib.CustomBlocks.CustomBlock;
import ru.thisistails.tailslib.CustomBlocks.CustomBlockManager;
import ru.thisistails.tailslib.CustomBlocks.PlacedBlocks;
import ru.thisistails.tailslib.CustomBlocks.Data.PlacedBlockData;

public class PlacedBlocksSer implements JsonDeserializer<PlacedBlocks>, JsonSerializer<PlacedBlocks> {

    @Override
    public PlacedBlocks deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

       List<PlacedBlockData> data = new ArrayList<>();

       /*
        {
            "PlacedBlocks": [
                {
                    "uuid": "uuid"
                    "location": [x,y,z],
                    "locationWorld": "world",
                    "ownerUuid": "uuid",
                    "customblock": "customblockid"
                    "extra": {...}
                }
            ]
        }
        */

       json.getAsJsonObject().get("placedBlocks").getAsJsonArray().iterator().forEachRemaining((element) -> {
            JsonObject obj = element.getAsJsonObject();
            JsonArray loc = obj.get("location").getAsJsonArray();
            UUID uuid = UUID.fromString(obj.get("uuid").getAsString());

            CustomBlock placedBlock = CustomBlockManager.getInstance().getBlocks().get(obj.get("customblock").getAsString());

            if (placedBlock == null) {
                Bukkit.getLogger().severe("Block named " + obj.get("customblock").getAsString() + " does not exists.");
            }

            //JsonElement extra = obj.get("extra");

            // if (extra != null) {
            //     placedBlock.onLoad(element);
            // }

            Location location = new Location(Bukkit.getWorld(obj.get("locationWorld").getAsString()), loc.get(0).getAsInt(), loc.get(1).getAsInt(), loc.get(2).getAsInt());
            PlacedBlockData pl = new PlacedBlockData(
            uuid,
            location,
            placedBlock);

            pl.setOwnerUuid(UUID.fromString(obj.get("ownerUuid").getAsString()));

            data.add(pl);
       });

        return new PlacedBlocks(data);
    }

    @Override
    public JsonElement serialize(PlacedBlocks src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject element = new JsonObject();
        JsonArray array = new JsonArray();

        src.getPlacedBlocks().iterator().forEachRemaining((block) -> {
            JsonObject obj = new JsonObject();
            JsonArray loc = new JsonArray();
            obj.addProperty("uuid", block.getUuid().toString());
            loc.add(block.getLocation().getX());
            loc.add(block.getLocation().getY());
            loc.add(block.getLocation().getZ());

            JsonElement extra = block.getPlacedBlock().onSave();

            obj.addProperty("ownerUuid", block.getOwnerUuid().toString());
            obj.addProperty("customblock", block.getPlacedBlock().getData().getBlockId());
            obj.addProperty("locationWorld", block.getLocation().getWorld().getName());
            obj.add("location", loc);

            if (extra != null) {
                obj.add("extra", extra);
            }

            array.add(obj);
        });

        element.add("placedBlocks", array);

        return element;
    }
    
}
