package fr.uniteduhc.bot.events.impl;

import fr.uniteduhc.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.util.Date;

public class GuildMemberJoinListener {
    public GuildMemberJoinListener(GuildMemberJoinEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setThumbnail(event.getMember().getEffectiveAvatarUrl());
        embedBuilder.setTitle("Un nouveau membre !");
        embedBuilder.setDescription("Bienvenue à **" + event.getMember().getEffectiveName() +
                "** sur United. Pour suivre l'actualité du serveur rendez-vous sur le channel " +
                Main.getInstance()
                        .getGuild()
                        .getNewsChannelById("992917852166307931")
                        .getAsMention() +
                ". Il y a maintenant un total de **" + Main.getInstance().getGuild().getMembers().size() +
                " **membres sur le discord.");
        embedBuilder.setFooter("United'Bot", Main.getInstance().getGuild().getIconUrl());
        embedBuilder.setTimestamp(new Date().toInstant());

        Main.getInstance().getGuild().getTextChannelById("991425992826564698").sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
