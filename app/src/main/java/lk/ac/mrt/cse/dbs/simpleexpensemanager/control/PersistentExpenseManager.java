package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseTrasactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager{
     Context context;
    public PersistentExpenseManager(Context context) {
        this.context = context;
        setup();

    }
    @Override
    public void setup() {
        Context context = this.context;
        TransactionDAO databaseTransactionDAO = new DatabaseTrasactionDAO(context);
        setTransactionsDAO(databaseTransactionDAO);

        AccountDAO databaseAccountDAO = new DatabaseAccountDAO(context);
        setAccountsDAO(databaseAccountDAO);

        Account dummyAcct1 = new Account("180003L", "Bank of Ceylon", "Nuwan Madushanka", 10000.0);
        Account dummyAcct2 = new Account("18000XX", "Peoples Bank", "Supun Madushan", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

    }



}
