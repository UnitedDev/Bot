package fr.kohei.bot;

import fr.kohei.bot.commands.CommandManager;
import fr.kohei.bot.events.UserEvents;
import fr.kohei.bot.manager.TicketManager;
import fr.kohei.bot.pubsub.packets.AttemptLinkPacket;
import fr.kohei.bot.pubsub.packets.LinkSuccessPacket;
import fr.kohei.bot.pubsub.subscriber.LinkSuccessSubscriber;
import fr.kohei.common.CommonProvider;
import fr.kohei.common.api.CommonAPI;
import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.common.utils.messaging.Pidgin;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.*;

@Getter
public class Main {
    @Getter
    private static Main instance;

    private final CommonAPI commonAPI;
    private final JDA jda;
    private final Guild guild;
    private final TicketManager ticketManager;

    public Main() throws Exception {
        instance = this;

        this.jda = JDABuilder
                .createDefault("OTk1MDY2MzYwNDkxMTU5NjMz.G5DNj4.p66Rvv9bSjx1FgXgTyw2Sex8OPJhY8pALQcM5g")
                .setActivity(Activity.watching("vos messages"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new UserEvents())
                .build()
                .awaitReady();
        this.guild = jda.getGuildById("635889060577017887");
        this.commonAPI = CommonAPI.create();
        this.ticketManager = new TicketManager(this);

        if (guild == null) {
            System.exit(1);
            return;
        }

        this.setupCommands();
        this.registerPubSub();
        this.membersTask();
    }

    private void setupCommands() {

        CommandData linkData = new CommandData(
                "link",
                "Permet de relier son compte Discord"
        ).addOption(OptionType.STRING, "pseudo", "Votre pseudo Minecraft", true);
        CommandData infosData = new CommandData(
                "infos",
                "Informations sur votre compte Minecraft"
        ).addOption(OptionType.STRING, "pseudo", "Votre pseudo Minecraft", false);
        guild.upsertCommand(linkData).queue();
        guild.upsertCommand(infosData).queue();

        guild.upsertCommand("close", "Permet de fermer un ticket").queue();
        guild.upsertCommand("confirm", "Permet de confirmer la fermeture d'un ticket").queue();
        guild.upsertCommand("cancel", "Permet d'annuler la fermeture d'un ticket").queue();

        new CommandManager(jda);
    }

    private void membersTask() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                VoiceChannel channel = guild.getVoiceChannelById("991427315655508098");
                if (channel != null) {
                    channel.getManager().setName("\uD83C\uDF88︱Membres: " + guild.getMembers().size()).queue();
                }
            }
        }, 0, 100000);
    }

    private void registerPubSub() {
        Pidgin pidgin = this.commonAPI.getMessaging();

        pidgin.registerAdapter(AttemptLinkPacket.class, null);
        pidgin.registerAdapter(LinkSuccessPacket.class, new LinkSuccessSubscriber());
    }

    public static ProfileData fromString(String name) {
        return CommonProvider.getInstance().players.values().stream()
                .filter(profileData -> profileData.getDisplayName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static UUID getUUIDFromName(String name) {
        return CommonProvider.getInstance().players.keySet().stream()
                .filter(uuid -> CommonProvider.getInstance().players.get(uuid).getDisplayName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static ProfileData getFromLink(User user) {
        return CommonProvider.getInstance().players.values().stream()
                .filter(profileData -> profileData.getLink() != null)
                .filter(profileData -> profileData.getLink().equals(user.getId()))
                .findFirst()
                .orElse(null);
    }
}
