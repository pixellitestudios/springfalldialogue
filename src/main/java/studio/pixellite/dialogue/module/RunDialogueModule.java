package studio.pixellite.dialogue.module;

import me.lucko.helper.Commands;
import me.lucko.helper.Schedulers;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.metadata.MetadataKey;
import me.lucko.helper.metadata.MetadataMap;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.pixellite.dialogue.Dialogue;
import studio.pixellite.dialogue.DialoguePlugin;

import java.util.List;

public class RunDialogueModule implements TerminableModule {
  private static final MetadataKey<Boolean> WAITING_ON_COMMAND =
          MetadataKey.create("dialogue-wait", Boolean.class);

  private final DialoguePlugin plugin;

  public RunDialogueModule(DialoguePlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPermission("pixellite.dialogue.run")
            .assertConsole()
            .assertUsage("<player> <dialogueid>")
            .handler(c -> {
              Player target = Bukkit.getPlayer(c.arg(0).parseOrFail(String.class));
              if(target == null) {
                Players.msg(c.sender(), "Target is not online.");
                return;
              }

              Dialogue dialogue = plugin.getDialogueManager()
                      .getOrNull(c.arg(1).parseOrFail(String.class));

              if(dialogue == null) {
                Players.msg(c.sender(), "Dialogue does not exist.");
                return;
              }

              // send the dialogue to the target
              runDialogue(target,
                      dialogue);
            })
            .registerAndBind(consumer, "rundialogue");
  }

  private void runDialogue(Player player, Dialogue dialogue) {
    List<String> message = dialogue.getRandomMessage();

    Players.msg(player, " ");
    Players.msg(player, "  &f&l" + dialogue.getName());
    for(String string : message) {
      Players.msg(player, "  &7" + string);
    }
    Players.msg(player, " ");

    // play an interaction sound for the player
    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);

    // run dialogue command after X ticks
    MetadataMap playerMetadata = Metadata.provideForPlayer(player);
    String command = dialogue.getCommand().replace("{player}", player.getName());


    if(!dialogue.getCommand().equals("") && !playerMetadata.has(WAITING_ON_COMMAND)) {
      playerMetadata.put(WAITING_ON_COMMAND, true);

      Schedulers.sync().runLater(() -> {
        Bukkit.dispatchCommand(plugin.getServer().getConsoleSender(), command);
        playerMetadata.remove(WAITING_ON_COMMAND);
      }, dialogue.getCommandTickDelay());
    }
  }
}
