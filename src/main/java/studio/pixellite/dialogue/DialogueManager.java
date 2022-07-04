package studio.pixellite.dialogue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages {@link Dialogue} objects.
 */
public class DialogueManager {
  /** A map containing all the loaded dialogue pieces. */
  private final Map<String, Dialogue> dialogue = new ConcurrentHashMap<>();

  /** The primary plugin instance. */
  private final DialoguePlugin plugin;

  /** The loader for loading dialogue from the configuration. */
  private final DialogueLoader loader;

  public DialogueManager(DialoguePlugin plugin) {
    this.plugin = plugin;
    this.loader = new DialogueLoader(this);
  }

  protected DialoguePlugin getPlugin() {
    return plugin;
  }

  /** Initializes this manager by loading all of the dialogue. */
  public void init() {
    loader.loadDialogue();
  }

  /**
   * Registers a piece of dialogue with the manager. If a piece of dialogue with the same Id already
   * exists, then the behavior of this method is similar to that of undefined.
   *
   * @param d the dialogue to register
   */
  public void registerDialogue(Dialogue d) {
    dialogue.putIfAbsent(d.getDialogueId(), d);
  }

  /**
   * Gets a piece of dialogue from the manager.
   *
   * @param dialogueId the dialogue id
   * @return the dialogue, null if nothing was found
   */
  public Dialogue getOrNull(String dialogueId) {
    return dialogue.get(dialogueId);
  }
}
