package fr.uniteduhc.bot.pubsub.subscriber;

import fr.uniteduhc.bot.Main;
import fr.uniteduhc.bot.pubsub.packets.LinkSuccessPacket;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.uniteduhc.common.utils.messaging.pigdin.PacketListener;
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
