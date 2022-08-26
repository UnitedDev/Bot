package fr.uniteduhc.bot.commands.impl;

import fr.uniteduhc.bot.Main;
import fr.uniteduhc.bot.commands.Command;
import fr.uniteduhc.bot.utils.TimeUtil;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.cache.data.PunishmentData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Date;
import java.util.UUID;

public class InfosCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getOptions().size() > 0) {
            String username = event.getOptions().get(0).getAsString();
            sendInformations(event, username);
            return;
        }

        ProfileData data = Main.getFromLink(event.getUser());

        if (data == null) {
            event.reply(":x: Votre compte Discord n'est pas lié à un compte Minecraft.").setEphemeral(true).queue();
            return;
        }

        String username = Main.getFromLink(event.getUser()).getDisplayName();
        sendInformations(event, username);
    }

    private void sendInformations(SlashCommandEvent event, String username) {
        ProfileData data = Main.fromString(username);

        if (data == null) {
            event.reply(":x: Votre compte Discord n'est pas lié à un compte Minecraft.").setEphemeral(true).queue();
            return;
        }

        UUID uuid = Main.getUUIDFromName(data.getDisplayName());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(data.getDisplayName());
        builder.setDescription("Informations sur votre compte Minecraft.");
        builder.addField("Pseudo", data.getDisplayName(), true);
        if (data.getRank().getToken().equalsIgnoreCase("default")) {
            builder.addField("Grade", "Aucun", true);
        } else {
            String str = data.getRank().getChatPrefix().substring(4);
            // remove the last 2 caracters of the string str
            str = str.substring(0, str.length() - 2);

            builder.addField("Grade", str, true);
        }
        builder.addField("Coins", data.getCoins() + "", true);
        builder.addField("Boxs", data.getBox() + "", true);
        builder.addField("Hosts", "" + (data.getHosts() < 0 ? "Illimité" : data.getHosts()), true);
        builder.addField("Play Time", TimeUtil.getReallyNiceTime2(data.getPlayTime()), true);
        builder.addField("Dernière connexion", TimeUtil.formatDate(data.getLastLogin().getTime()), true);
        builder.setFooter("United'Bot", Main.getInstance().getGuild().getIconUrl());
        int mutes = (int) Main.getInstance().getCommonAPI().getPunishments(uuid).stream()
                .filter(punishmentData -> punishmentData.getPunishmentType() == PunishmentData.PunishmentType.MUTE)
                .count();
        int bans = (int) Main.getInstance().getCommonAPI().getPunishments(uuid).stream()
                .filter(punishmentData -> punishmentData.getPunishmentType() == PunishmentData.PunishmentType.BAN)
                .count();
        int blacklists = (int) Main.getInstance().getCommonAPI().getPunishments(uuid).stream()
                .filter(punishmentData -> punishmentData.getPunishmentType() == PunishmentData.PunishmentType.BLACKLIST)
                .count();

        int warns = Main.getInstance().getCommonAPI().getWarns(uuid).size();
        int reports = (int) Main.getInstance().getCommonAPI().getReports().stream()
                .filter(report -> report.getUuid().equals(uuid)).count();
        builder.setThumbnail("https://crafatar.com/avatars/" + uuid.toString() + "?overlay");
        builder.addField("Mutes", mutes + "", true);
        builder.addField("Bans", bans + "", true);
        builder.addField("Blacklists", blacklists + "", true);
        builder.addField("Warns", warns + "", true);
        builder.addField("Reports", reports + "", true);
        builder.setTimestamp(new Date().toInstant());

        event.replyEmbeds(builder.build()).queue();
    }
}
