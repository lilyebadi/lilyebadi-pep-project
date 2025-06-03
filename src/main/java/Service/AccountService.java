package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty()){
            return null;
        }
        if (account.getPassword().length() < 4){
            return null;
        }

        if(accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }

        return accountDAO.createAccount(account);
    }

    public Account login(Account account){
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if(existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }
        return null;
    }
}