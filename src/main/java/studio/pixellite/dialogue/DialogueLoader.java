package studio.pixellite.dialogue;

import com.google.common.reflect.TypeToken;
import me.lucko.helper.config.ConfigurationNode;
import me.lucko.helper.config.objectmapping.ObjectMappingException;
import studio.pixellite.dialogue.configurate.ConfigurateResolver;

/**
 * Loads all the dialogue pieces from the {@link ConfigurationNode}.
 */
public class DialogueLoader {
  private final DialogueManager parent;
  private final ConfigurationNode node;

  /** A flag to determine if dialogue has been loaded yet. */
  private boolean loaded;

  public DialogueLoader(DialogueManager parent) {
    this.parent = parent;
    this.node = ConfigurateResolver.resolveNode(parent.getPlugin(), "config.yml");
  }

  /**
   * Loads all the configured dialogue into the {@link DialogueManager}.
   */
  @SuppressWarnings("UnstableApiUsage")
  public void loadDialogue() {
    if(loaded) {
      return; // noop
    }

    // build dialogue objects
    for(ConfigurationNode child : node.getChildrenMap().values()) {
      String dialogueId = child.getNode("id").getString("def").toLowerCase();
      String dialogueName = child.getNode("name").getString("def");
      String dialogueCommand = child.getNode("command").getString("");
      int dialogueCommandTickDelay = child.getNode("command-delay").getInt();

      Dialogue.Builder builder = Dialogue.builder(dialogueId, dialogueName, dialogueCommand);
      builder.commandTickDelay(dialogueCommandTickDelay);

      for(ConfigurationNode message : child.getNode("messages").getChildrenMap().values()) {
        try {
          builder.addDialogue(message.getList(TypeToken.of(String.class)));
        } catch (ObjectMappingException e) {
          // ignored
        }
      }

      parent.registerDialogue(builder.build());
    }

    // set loaded flag
    loaded = true;
  }
}
