package fr.kohei.bot.events;

import fr.kohei.bot.Main;
import fr.kohei.bot.manager.TicketManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrivateMessageListener {

    public static void onPrivateMessageReceivedListener(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TicketManager ticketManager = Main.getInstance().getTicketManager();

        if(user.isBot()) return;
        if(ticketManager.getTicketCreation().get(user.getId()) == null) return;


        if(ticketManager.getTicketCreation().get(user.getId()).getType() == TicketManager.TicketCreationType.USERNAME) {
            ticketManager.getTicketCreation().get(user.getId()).setUserName(event.getMessage().getContentDisplay());
            ticketManager.getTicketCreation().get(user.getId()).setType(TicketManager.TicketCreationType.MESSAGE);
            event.getMessage().reply("Merci d'expliquer votre problème le plus clairement possible.").queue();
            return;
        }

        if(ticketManager.getTicketCreation().get(user.getId()).getType() == TicketManager.TicketCreationType.MESSAGE) {
            ticketManager.getTicketCreation().get(user.getId()).setIssue(event.getMessage().getContentDisplay());

            TicketManager.CreatingTicket ticket = ticketManager.getTicketCreation().get(user.getId());
            ticketManager.createTicket(ticket.getCategory(), ticket.getMember(), ticket.getIssue(), ticket.getUserName());
            event.getMessage().reply("Votre ticket a été créé avec succès.").queue();

            ticketManager.getTicketCreation().put(user.getId(), null);
        }
    }
}
