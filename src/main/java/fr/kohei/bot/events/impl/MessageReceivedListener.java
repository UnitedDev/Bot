package fr.kohei.bot.events.impl;

import fr.kohei.bot.Main;
import fr.kohei.bot.events.PrivateMessageListener;
import fr.kohei.bot.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MessageReceivedListener {
    public MessageReceivedListener(MessageReceivedEvent event) {
        if (event.getMessage().getContentDisplay().equalsIgnoreCase("setuptickets")) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Créer un ticket");
                builder.setDescription("Vous avez besoin d'aide ? Cliquez sur la réaction ci-dessous pour créer un ticket afin d'obtenir de l'aide d'un de nos modérateurs.");
                builder.setColor(Color.orange);
                builder.setTimestamp(new Date().toInstant());
                MessageAction messageAction = event.getChannel().sendMessageEmbeds(builder.build());
                messageAction.setActionRow(SelectionMenu
                        .create("ticket_manager")
                        .addOption("Assistance et Modération", "ASSISTANCE_MODERATION", "Si vous avez rencontré un problème en jeu que vous souhaitez signaler", Emoji.fromMarkdown("\uD83D\uDEE1"))
                        .addOption("Boutique et Paiements", "BOUTIQUE_PAIEMENTS", "Si vous avez rencontré un problème sur notre site et notre boutique", Emoji.fromMarkdown("\uD83D\uDCBC"))
                        .addOption("Report & Plainte", "REPORT_PLAINTES", "Si vous avez un problème avec un joueur sur le serveur minecraft ou discord", Emoji.fromMarkdown("\uD83C\uDFA8"))
                        .addOption("Bugs & Autre", "BUGS_AUTRE", "Si vous avez trouvé un bug sur le serveur ou tout autre raison non citée ci-dessus", Emoji.fromMarkdown("\uD83C\uDFF7"))
                        .build()).queue();
            }
        }

        try {
            event.getTextChannel();
        } catch (IllegalStateException ignored) {
            PrivateMessageListener.onPrivateMessageReceivedListener(event);
            return;
        }

        if (event.getTextChannel().getParentCategory() != null && event.getTextChannel().getParentCategory().getId().equals(Main.getInstance().getTicketManager().getTicketsCategory().getId())) {
            if (event.getMember() == null) return;

            if (Main.getInstance().getTicketManager().getWaitingConfirmation().contains(event.getTextChannel()) && !event.getAuthor().isBot()) {
                event.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
                event.getTextChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Vous ne pouvez pas envoyer de messages si une attente de confirmation est en cours").build()).queue(message -> message.delete().queueAfter(3, TimeUnit.SECONDS));
                return;
            }
        }

        if (event.getChannel().getId().equals(Constants.SUGGESTIONS_ID)) {
            if (event.getAuthor().isBot()) return;

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("**Nouvelle Suggestion**");
            embedBuilder.addField("**Membre**", event.getMember().getAsMention(), false);
            embedBuilder.addField("**Suggestion**", event.getMessage().getContentDisplay(), false);
            embedBuilder.setTimestamp(new Date().toInstant());
            embedBuilder.setFooter(event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                message.addReaction("✅").queue();
                message.addReaction("❌").queue();
            });
            event.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
        }


    }
}
