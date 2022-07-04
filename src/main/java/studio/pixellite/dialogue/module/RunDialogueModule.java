package studio.pixellite.dialogue.module;

import me.lucko.helper.Commands;
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
  private final DialoguePlugin plugin;

  public RunDialogueModule(DialoguePlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertConsole()
            .assertUsage("<player> <dialogueid> <npcname>")
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
                      dialogue,
                      String.join(" ", c.args().subList(2, c.args().size())));
            })
            .registerAndBind(consumer, "rundialogue");
  }

  private void runDialogue(Player player, Dialogue dialogue, String npcName) {
    List<String> message = dialogue.getRandomMessage();

    Players.msg(player, " &f&l" + npcName);
    for(String string : message) {
      Players.msg(player, " &7" + string);
    }

    // play an interaction sound for the player
    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
  }
}
