package Controller;

import Model.Account;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import Service.AccountService;

public class SocialMediaController {
    private AccountService accountService;
    private ObjectMapper mapper;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.mapper = new ObjectMapper();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

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

}