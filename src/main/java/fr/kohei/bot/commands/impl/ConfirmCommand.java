package fr.kohei.bot.commands.impl;

import fr.kohei.bot.Main;
import fr.kohei.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class ConfirmCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getTextChannel().getParentCategory() != null && event.getTextChannel().getParentCategory().getId().equals(Main.getInstance().getTicketManager().getTicketsCategory().getId())) {
            if (event.getMember() == null) return;

            if (!Main.getInstance().getTicketManager().getWaitingConfirmation().contains(event.getTextChannel())) {
                event.replyEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Ce ticket n'est pas en attente de confirmation").build()).setEphemeral(true).queue();
                return;
            }

            if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("Fermeture du ticket en cours").queue();
                Main.getInstance().getTicketManager().getWaitingConfirmation().remove(event.getTextChannel());
                event.getTextChannel().delete().queueAfter(3, TimeUnit.SECONDS);
            } else {
                event.replyEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Vous ne pouvez pas confirmer la suppression d'un ticket").build()).setEphemeral(true).queue();
            }

        }
    }
}