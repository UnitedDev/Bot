package fr.uniteduhc.bot.commands.impl;

import fr.uniteduhc.bot.Main;
import fr.uniteduhc.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;

public class CancelCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getTextChannel().getParentCategory() != null && event.getTextChannel().getParentCategory().getId().equals(Main.getInstance().getTicketManager().getTicketsCategory().getId())) {
            if (event.getMember() == null) return;

            if (!Main.getInstance().getTicketManager().getWaitingConfirmation().contains(event.getTextChannel())) {
                event.replyEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Ce ticket n'est pas en attente de confirmation").build()).setEphemeral(true).queue();
                return;
            }

            if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.replyEmbeds(new EmbedBuilder().setDescription("La fermeture de ce ticket a été annulée").setColor(Color.green).build()).queue();
                Main.getInstance().getTicketManager().getWaitingConfirmation().remove(event.getTextChannel());
            } else {
                event.replyEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Vous n'avez pas la permission d'executer cette commande").build()).setEphemeral(true).queue();
            }

        }
    }
}
