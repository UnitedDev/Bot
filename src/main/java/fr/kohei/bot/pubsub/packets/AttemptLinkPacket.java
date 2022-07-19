package fr.kohei.bot.pubsub.packets;

import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.common.utils.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttemptLinkPacket implements Packet {

    private final ProfileData profileData;
    private final String discordId;
    private final String discordDisplay;

}
