package logic;

import common.ValidationException;
import dal.AccountDAL;
import entity.Account;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
//import javax.validation.ValidationException;

/**
 *
 * @author Mingjiong Liao
 */
public class AccountLogic extends GenericLogic<Account, AccountDAL> {

    public static final String DISPLAY_NAME = "displayName";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String ID = "id";

    public AccountLogic() {
        super(new AccountDAL());
    }

    @Override
    public List<Account> getAll() {
        return get(() -> dao().findAll());
    }

    public List<Account> getAllWithAnonymousClass() {
        //method get() in superclass GenericLogic needs a Supplier which is an interface.
        //Supplier interface is like below:
        //interface Supplier<T>{
        //  T get();
        //}
        //we can create an anonymous instance of Supplier
        Supplier<List<Account>> supply = new Supplier<List<Account>>() {
            @Override
            public List<Account> get() {
                return dao().findAll();
            }
        };
        //this is long and has more code, it can be replaced with lambda or method reference
        return get(supply);
    }

    public List<Account> getAllWithLambda() {
        //method get() in superclass GenericLogic needs a Supplier which is a functional interface.
        //functional interface has one method, meaning it can be initialized with lambda.
        //Supplier interface is like below:
        //interface Supplier<T>{
        //  T get();
        //}
        Supplier<List<Account>> supply = () -> {
            return dao().findAll();
        };
        //we can clean this lambda more. if your lambda has one line of code
        //the "{}" can be removed same as "return" which compiler can infer.
        supply = () -> dao().findAll();
        return get(supply);
        //all can be done in one line as well
        //return get(()->dao().findAll());
    }

    public List<Account> getAllWithMethodRefrence() {
        //method get() in superclass GenericLogic needs a Supplier which is a functional interface.
        //functional interface has one method, meaning it can be initialized with method reference.
        //Supplier interface is like below:
        //interface Supplier<T>{
        //  T get();
        //}
        Supplier<List<Account>> supply = null;// = dao()::findAll;
        //in this case we are using method refrence, we directly point to the location
        //which has the method and let the compiler deal with the rest.
        return get(supply);
        //all can be done in one line as well
        //return get(dao()::findAll);
    }

    @Override
    public Account getWithId(int id) {
        return get(() -> dao().findById(id));
    }

    public Account getAccountWithDisplayName(String displayName) {
        return get(() -> dao().findByDisplayName(displayName));
    }

    public Account getAccountWithUser(String user) {
        return get(() -> dao().findByUser(user));
    }

    public List<Account> getAccountsWithPassword(String password) {
        return get(() -> dao().findByPassword(password));
    }

    public Account getAccountWith(String username, String password) {
        return get(() -> dao().validateUser(username, password));
    }

    @Override
    public List<Account> search(String search) {
        return get(() -> dao().findContaining(search));
    }

    @Override
    public Account createEntity(Map<String, String[]> parameterMap) {
        Account account = new Account();
        //set ID
        if (parameterMap.containsKey(ID)) {
            account.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        // set Disaplayname
        if (parameterMap.containsKey(DISPLAY_NAME) && parameterMap.get(DISPLAY_NAME) != null) {
            if (parameterMap.get(DISPLAY_NAME)[0].isEmpty() || (parameterMap.get(DISPLAY_NAME)[0].length() > 45)) {
                throw new ValidationException("display name must at least one character or maximum 45");
            }
            account.setDisplayName(parameterMap.get(DISPLAY_NAME)[0]);

        } else {
            throw new ValidationException("The display name doesn't exist");
        }
        //set user
        if (parameterMap.containsKey(USER) && parameterMap.get(USER) != null) {
            if (parameterMap.get(USER)[0].isEmpty() || (parameterMap.get(USER)[0].length() > 45)) {
                throw new ValidationException("user name must at least one character or maximum 45");
            }
            account.setUser(parameterMap.get(USER)[0]);

        } else {
            throw new ValidationException("The user doesn't exist");
        }
        //set password
        if (parameterMap.containsKey(PASSWORD) && parameterMap.get(PASSWORD) != null) {
            if (parameterMap.get(PASSWORD)[0].isEmpty() || (parameterMap.get(PASSWORD)[0].length() > 45)) {
                throw new ValidationException("pass word must at least one character or maximum 45");
            }
            account.setPassword(parameterMap.get(PASSWORD)[0]);

        } else {
            throw new ValidationException("The password doesn't exist");
        }
        return account;
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Display Name", "User", "Password");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, DISPLAY_NAME, USER, PASSWORD);
    }

    @Override
    public List<?> extractDataAsList(Account e) {
        return Arrays.asList(e.getId(), e.getDisplayName(), e.getUser(), e.getPassword());
    }
}
