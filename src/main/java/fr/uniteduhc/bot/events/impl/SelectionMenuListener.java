package fr.uniteduhc.bot.events.impl;

import fr.uniteduhc.bot.Main;
import fr.uniteduhc.bot.manager.TicketManager;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;

public class SelectionMenuListener {

    public SelectionMenuListener(SelectionMenuInteraction event) {
        if (event.getMember() == null) return;
        if (event.getSelectedOptions() == null) return;

        SelectOption option = event.getSelectedOptions().get(0);
        TicketManager.TicketCategories ticketTypes = TicketManager.TicketCategories.valueOf(option.getValue());

        if (Main.getFromLink(event.getUser()) == null) {
            event.getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("Quel est votre pseudo Minecraft ?")).queue();
            Main.getInstance().getTicketManager().getTicketCreation().put(event.getMember().getId(),
                    new TicketManager.CreatingTicket(event.getMember(), TicketManager.TicketCreationType.USERNAME, ticketTypes));
            event.reply("Regardez vos messages privés").setEphemeral(true).queue();
        } else {
            event.getUser().openPrivateChannel().flatMap(privateChannel ->
                    privateChannel.sendMessage("Etant donné que vous êtes link, votre ticket est automatiquement enregistré avec le pseudo **" + Main.getFromLink(event.getUser()).getDisplayName() + "**.")).queue();
            event.getUser().openPrivateChannel().flatMap(privateChannel ->
                    privateChannel.sendMessage("Merci d'expliquer votre problème le plus clairement possible.")).queue();
            Main.getInstance().getTicketManager().getTicketCreation().put(event.getMember().getId(),
                    new TicketManager.CreatingTicket(event.getMember(), TicketManager.TicketCreationType.MESSAGE, ticketTypes));
            Main.getInstance().getTicketManager().getTicketCreation().get(event.getUser().getId()).setUserName(Main.getFromLink(event.getUser()).getDisplayName());
            event.reply("Regardez vos messages privés").setEphemeral(true).queue();
        }
    }
}
