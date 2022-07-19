package fr.kohei.bot.commands.impl;

import fr.kohei.bot.Main;
import fr.kohei.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;
import java.util.Date;

public class CloseCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getTextChannel().getParentCategory() != null && event.getTextChannel().getParentCategory().getId().equals(Main.getInstance().getTicketManager().getTicketsCategory().getId())) {
            if (Main.getInstance().getTicketManager().getWaitingConfirmation().contains(event.getTextChannel())) {
                event.replyEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Une attente de confirmation est déjà en cours sur ce ticket").build()).setEphemeral(true).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.green);
            builder.setTitle("En attente de confirmation");
            builder.setDescription(event.getMember().getUser().getName() + " a demandé une fermeture du ticket. Utilisez **/cancel** pour refuser et **/confirm** pour accepter la fermeture.");
            builder.setTimestamp(new Date().toInstant());

            event.replyEmbeds(builder.build()).queue();
            Main.getInstance().getTicketManager().getWaitingConfirmation().add(event.getTextChannel());
        }
    }
}
