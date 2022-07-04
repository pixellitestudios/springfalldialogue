package studio.pixellite.dialogue;

import me.lucko.helper.plugin.ExtendedJavaPlugin;
import studio.pixellite.dialogue.module.RunDialogueModule;

public class DialoguePlugin extends ExtendedJavaPlugin {
  private DialogueManager dialogueManager;

  @Override
  protected void enable() {
    saveDefaultConfig();
    dialogueManager = new DialogueManager(this);
    bindModule(new RunDialogueModule(this));
  }

  public DialogueManager getDialogueManager() {
    return dialogueManager;
  }
}
