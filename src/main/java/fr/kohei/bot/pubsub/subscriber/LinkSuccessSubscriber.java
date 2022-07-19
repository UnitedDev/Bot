package fr.kohei.bot.pubsub.subscriber;

import fr.kohei.bot.Main;
import fr.kohei.bot.pubsub.packets.LinkSuccessPacket;
import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.utils.messaging.pigdin.PacketListener;
import net.dv8tion.jda.api.entities.User;

public class LinkSuccessSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(LinkSuccessPacket packet) {
        User user = Main.getInstance().getJda().getUserById(packet.getDiscordId());
        if (user == null) return;

        ProfileData profile = packet.getData();
        profile.setLink(user.getId());
        Main.getInstance().getCommonAPI().saveProfile(Main.getUUIDFromName(profile.getDisplayName()), profile);

        user.openPrivateChannel().queue(channel -> {
            channel.sendMessage(
                    "Votre compte Discord a été lié avec le joueur minecraft **" + profile.getDisplayName() + "**."
            ).queue();
        });
    }

}
