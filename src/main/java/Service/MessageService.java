package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO; 
    private AccountDAO accountDAO;
    
    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty()) {
            return null;
        }

        if (message.getMessage_text().length() > 255) {
            return null;
        }

        if (accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }

        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessageById(id);
    }

    public Message updateMessageText(int id, String newMessageText) {
        // Validate message text is not blank
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return null;
        }

        // Validate message text length
        if (newMessageText.length() > 255) {
            return null;
        }

        return messageDAO.updateMessageText(id, newMessageText);
    }
}
