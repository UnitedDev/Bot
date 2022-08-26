package fr.uniteduhc.bot.pubsub.packets;

import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.utils.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttemptLinkPacket implements Packet {

    private final ProfileData profileData;
    private final String discordId;
    private final String discordDisplay;

}
