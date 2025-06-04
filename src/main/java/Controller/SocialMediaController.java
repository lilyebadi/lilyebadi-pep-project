package Controller;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
import java.util.ArrayList;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper mapper;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.mapper = new ObjectMapper();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    private void registerHandler(Context ctx) {
        try{
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account registeredAccount = accountService.register(account);

            if(registeredAccount != null){
                ctx.json(registeredAccount);
                ctx.status(200);
            } else {
                ctx.status(400);
                ctx.result("");
            }
        } catch(Exception e) {
            ctx.status(400);
            ctx.result("");
        }
    }    private void loginHandler(Context ctx){
        try{
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account loggedInAccount = accountService.login(account);

            if(loggedInAccount != null){
                ctx.json(loggedInAccount);
                ctx.status(200);
            } else {
                ctx.status(401); 
                ctx.result("");
            }
        } catch(Exception e) {
            ctx.status(401);
            ctx.result("");
        }
    }

    private void createMessageHandler(Context ctx) {
        try {
            Message message = mapper.readValue(ctx.body(),Message.class);
            Message createdMessage = messageService.createMessage(message);

            if(createdMessage != null){
                ctx.json(createdMessage);
                ctx.status(200);
            } else {
                ctx.status(400);
                ctx.result("");
            }
        } catch(Exception e) {
            ctx.status(400);
            ctx.result("");
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        try {
            ctx.json(messageService.getAllMessages());
            ctx.status(200);
        }  catch(Exception e) {
            ctx.status(200);
            ctx.result("[]");
        }
    }

    private void getMessageByIdHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            ctx.json(message != null ? message : "");
            ctx.status(200);
        } catch(Exception e) {
            ctx.status(200);
            ctx.result("");
        }
    }

    private void deleteMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessageById(messageId);
            ctx.json(deletedMessage != null ? deletedMessage : "");
            ctx.status(200);
        } catch(Exception e) {
            ctx.status(200);
            ctx.result("");
        }
    }

    private void updateMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message messageUpdate = mapper.readValue(ctx.body(), Message.class);
            String newMessageText = messageUpdate.getMessage_text();
            
            Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);
            
            if(updatedMessage != null) {
                ctx.json(updatedMessage);
                ctx.status(200);
            } else {
                ctx.status(400);
                ctx.result("");
            }
        } catch(Exception e) {
            ctx.status(400);
            ctx.result("");
        }
    }

    private void getMessagesByUserHandler(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByUser(accountId);
            ctx.json(messages);
            ctx.status(200);
        } catch(Exception e) {
            ctx.status(200);
            ctx.json(new ArrayList<>());
        }
    }

}