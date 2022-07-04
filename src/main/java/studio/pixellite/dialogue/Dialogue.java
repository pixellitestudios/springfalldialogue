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
    private final ImmutableList.Builder<List<String>> messagesBuilder = ImmutableList.builder();

    private Builder(String dialogueId) {
      this.dialogueId = dialogueId;
    }

    public Builder addDialogue(List<String> message) {
      messagesBuilder.add(ImmutableList.copyOf(message));
      return this;
    }

    public Dialogue build() {
      return new Dialogue(dialogueId, messagesBuilder.build());
    }
  }

  public static Builder builder(String dialogueId) {
    return new Builder(dialogueId);
  }

  /** The name/id for this piece of dialogue. This is typically associated with the NPC. */
  private final String dialogueId;

  /** The messages that this dialogue can randomly send. */
  private final List<List<String>> messages;

  /** A random selector for selecting random {@link #messages}. */
  private final RandomSelector<List<String>> selector;

  private Dialogue(String dialogueId, List<List<String>> messages) {
    this.dialogueId = dialogueId;
    this.messages = messages;
    this.selector = RandomSelector.uniform(this.messages);
  }

  public String getDialogueId() {
    return dialogueId;
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
