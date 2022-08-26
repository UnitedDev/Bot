package fr.uniteduhc.bot.events;

import fr.uniteduhc.bot.events.impl.GuildMemberJoinListener;
import fr.uniteduhc.bot.events.impl.MessageReceivedListener;
import fr.uniteduhc.bot.events.impl.SelectionMenuListener;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;
import org.jetbrains.annotations.NotNull;

public class UserEvents implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {

        if (genericEvent instanceof SelectionMenuInteraction) {
            new SelectionMenuListener((SelectionMenuInteraction) genericEvent);
        }
        if (genericEvent instanceof MessageReceivedEvent) {
            new MessageReceivedListener((MessageReceivedEvent) genericEvent);
        }
        if (genericEvent instanceof GuildMemberJoinEvent) {
            new GuildMemberJoinListener((GuildMemberJoinEvent) genericEvent);
        }
    }


}
