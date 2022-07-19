package fr.kohei.bot.commands;

import fr.kohei.bot.commands.impl.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandManager extends ListenerAdapter {

    HashMap<String, Command> commands = new HashMap<>();

    public CommandManager(JDA jda) {
        this.commands.put("link", new LinkCommand());
        this.commands.put("infos", new InfosCommand());
        this.commands.put("close", new CloseCommand());
        this.commands.put("confirm", new ConfirmCommand());
        this.commands.put("cancel", new CancelCommand());

        jda.addEventListener(this);
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        for (String cmd : commands.keySet()) {
            if (event.getName().equalsIgnoreCase(cmd)) {
                commands.get(cmd).execute(event);
            }
        }

    }

}
