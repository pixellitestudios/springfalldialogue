package studio.pixellite.dialogue;

import com.google.common.collect.ImmutableList;
import me.lucko.helper.random.RandomSelector;

import java.util.List;

/**
 * Represents an NPC's dialogue.
 */
public class Dialogue {
  /** A simple builder instance for building separate dialogue instances. */
  public static final class Builder {
    private final String dialogueId;
    private final String name;
    private final ImmutableList.Builder<List<String>> messagesBuilder = ImmutableList.builder();

    private Builder(String dialogueId, String name) {
      this.dialogueId = dialogueId;
      this.name = name;
    }

    public Builder addDialogue(List<String> message) {
      messagesBuilder.add(ImmutableList.copyOf(message));
      return this;
    }

    public Dialogue build() {
      return new Dialogue(dialogueId, name, messagesBuilder.build());
    }
  }

  public static Builder builder(String dialogueId, String name) {
    return new Builder(dialogueId, name);
  }

  /** The name/id for this piece of dialogue. This is typically associated with the NPC. */
  private final String dialogueId;

  /** The name of the associated NPC. */
  private final String name;

  /** The messages that this dialogue can randomly send. */
  private final List<List<String>> messages;

  /** A random selector for selecting random {@link #messages}. */
  private final RandomSelector<List<String>> selector;

  private Dialogue(String dialogueId, String name, List<List<String>> messages) {
    this.dialogueId = dialogueId;
    this.name = name;
    this.messages = messages;
    this.selector = RandomSelector.uniform(this.messages);
  }

  public String getDialogueId() {
    return dialogueId;
  }

  public String getName() {
    return name;
  }

  public List<List<String>> getMessages() {
    return messages;
  }

  /**
   * Gets a random message from the pool of messages.
   *
   * @return a random message
   */
  public List<String> getRandomMessage() {
    return selector.pick();
  }
}
