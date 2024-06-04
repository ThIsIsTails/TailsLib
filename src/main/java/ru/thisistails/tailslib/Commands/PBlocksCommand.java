package ru.thisistails.tailslib.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomBlocks.CustomBlockManager;
import ru.thisistails.tailslib.CustomBlocks.Data.PlacedBlockData;

public class PBlocksCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        StringBuffer buffer = new StringBuffer();

        if (args.length == 0)
            buffer.append("Total blocks loaded: " + String.valueOf(CustomBlockManager.getInstance().getPlacedBlocks().getPlacedBlocks().size()) );
        else {
            int place = Integer.valueOf(args[0]) - 1;

            PlacedBlockData data;
            try {
                data = CustomBlockManager.getInstance().getPlacedBlocks().getPlacedBlocks().get(place);
            } catch (IndexOutOfBoundsException e) {
                sender.sendMessage(args[0] + " not found/dont exists.");
                return true;
            }
            buffer.append(args[0] + ": ");
            buffer.append(data.toString());
        }

        sender.sendMessage(buffer.toString());

        return true;
    }

}
