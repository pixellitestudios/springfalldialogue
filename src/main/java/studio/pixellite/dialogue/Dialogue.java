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
    private final String command;
    private int commandTickDelay = 0;

    private Builder(String dialogueId, String name, String command) {
      this.dialogueId = dialogueId;
      this.name = name;
      this.command = command;
    }

    public Builder addDialogue(List<String> message) {
      messagesBuilder.add(ImmutableList.copyOf(message));
      return this;
    }

    public Builder commandTickDelay(int ticks) {
      this.commandTickDelay = ticks;
      return this;
    }

    public Dialogue build() {
      return new Dialogue(dialogueId, name, command, commandTickDelay, messagesBuilder.build());
    }
  }

  public static Builder builder(String dialogueId, String name, String command) {
    return new Builder(dialogueId, name, command);
  }

  /** The name/id for this piece of dialogue. This is typically associated with the NPC. */
  private final String dialogueId;

  /** The name of the associated NPC. */
  private final String name;

  /** The messages that this dialogue can randomly send. */
  private final List<List<String>> messages;

  /** A command that will be executed after an NPC message is sent. */
  private final String command;

  /** How many ticks should we wait before executing the command? */
  private final int commandTickDelay;

  /** A random selector for selecting random {@link #messages}. */
  private final RandomSelector<List<String>> selector;

  private Dialogue(String dialogueId,
                   String name,
                   String command,
                   int commandTickDelay,
                   List<List<String>> messages) {
    this.dialogueId = dialogueId;
    this.name = name;
    this.messages = messages;
    this.command = command;
    this.commandTickDelay = commandTickDelay;
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

  public String getCommand() {
    return command;
  }

  public int getCommandTickDelay() {
    return commandTickDelay;
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
