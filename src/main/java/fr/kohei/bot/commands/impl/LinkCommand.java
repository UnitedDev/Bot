package fr.kohei.bot.commands.impl;

import fr.kohei.bot.Main;
import fr.kohei.bot.commands.Command;
import fr.kohei.bot.pubsub.packets.AttemptLinkPacket;
import fr.kohei.common.cache.data.ProfileData;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class LinkCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {
        String pseudo = event.getOptions().get(0).getAsString();
        ProfileData profile = Main.fromString(pseudo);

        if(Main.getFromLink(event.getUser()) != null) {
            event.reply(":x: Votre compte Discord est déjà lié à un compte Minecraft.").setEphemeral(true).queue();
            return;
        }

        if (profile == null) {
            event.reply(":x: Ce joueur n'existe pas.").setEphemeral(true).queue();
            return;
        }

        if (!profile.getLink().equals("")) {
            event.reply(":x: Il semblerait que ce joueur soit déjà link. Si voulez vous retirer le link, utilisez la commande **/unlink** sur le serveur.").setEphemeral(true).queue();
            return;
        }

        Main.getInstance().getCommonAPI().getMessaging().sendPacket(new AttemptLinkPacket(
                profile,
                event.getUser().getId(),
                event.getUser().getName() + "#" + event.getUser().getDiscriminator()
        ));
        event.reply("Une demande de link a été envoyée à **" + pseudo + "**. Regardez votre chat").setEphemeral(true).queue();
    }
}
