package fr.uniteduhc.bot.manager;

import fr.uniteduhc.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class TicketManager {

    private final Category ticketsCategory;
    private final List<TextChannel> waitingConfirmation;
    private final HashMap<String, CreatingTicket> ticketCreation;

    public TicketManager(Main main) {
        this.ticketsCategory = main.getGuild().getCategoryById("991429977398591569");
        this.waitingConfirmation = new ArrayList<>();
        this.ticketCreation = new HashMap<>();
    }

    public Category getTicketsCategory() {
        return ticketsCategory;
    }

    public void createTicket(TicketCategories ticketCategories, Member member, String issue, String username) {

        TextChannel channel = getTicketsCategory().createTextChannel(ticketCategories.getCustomName() + "-" + member.getUser().getName()).complete();
        if (!ticketCategories.isAdmin()) {
            Guild guild = Main.getInstance().getGuild();
            for (Role role : new Role[]{
                    guild.getRoleById("991446453698433084"),
                    guild.getRoleById("991447259495547050"),
                    guild.getRoleById("991447266177060905"),
                    guild.getRoleById("991448956158615683"),
            }) {
                if (role != null) {
                    try {
                        channel.createPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL).complete();
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        channel.createPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).complete();

        StringBuilder builder = new StringBuilder();

        Guild guild = Main.getInstance().getGuild();
        for (Role role : new Role[]{
                guild.getRoleById("991446453698433084"),
                guild.getRoleById("991447259495547050"),
                guild.getRoleById("991447266177060905"),
                guild.getRoleById("991448956158615683"),
        }) {
            builder.append((builder.toString().equals("") ? "" : " ")).append(role.getAsMention());
        }
        channel.sendMessage(builder.toString()).queue();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.orange);
        embedBuilder.setTitle("Bienvenue sur votre ticket " + member.getUser().getName());
        embedBuilder.setTimestamp(new Date().toInstant());
        embedBuilder.setDescription("Un modérateur va vous répondre sous peu.");
        embedBuilder.addField("Pseudo Minecraft :", username, false);
        embedBuilder.addField("Explication :", issue, false);

        channel.sendMessageEmbeds(embedBuilder.build()).queue();

    }

    public List<TextChannel> getWaitingConfirmation() {
        return waitingConfirmation;
    }

    public enum TicketCategories {
        ASSISTANCE_MODERATION(false),
        BOUTIQUE_PAIEMENTS(true),
        REPORT_PLAINTES(false),
        BUGS_AUTRE(false);

        private final boolean admin;

        TicketCategories(boolean admin) {
            this.admin = admin;
        }

        public boolean isAdmin() {
            return admin;
        }

        public String getCustomName() {
            return name().toLowerCase(Locale.ROOT).replace('_', '-');
        }

    }

    public HashMap<String, CreatingTicket> getTicketCreation() {
        return ticketCreation;
    }

    public static class CreatingTicket {
        private final Member member;
        private TicketCreationType type;
        private final TicketCategories category;
        private String userName;
        private String issue;

        public CreatingTicket(Member member, TicketCreationType type, TicketCategories category) {
            this.member = member;
            this.type = type;
            this.category = category;
        }

        public Member getMember() {
            return member;
        }

        public TicketCreationType getType() {
            return type;
        }

        public void setType(TicketCreationType type) {
            this.type = type;
        }

        public TicketCategories getCategory() {
            return category;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getIssue() {
            return issue;
        }

        public void setIssue(String issue) {
            this.issue = issue;
        }
    }

    public enum TicketCreationType {
        USERNAME,
        MESSAGE
    }
}
