package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DatabaseAccountDAO extends SQLiteOpenHelper implements AccountDAO {


    public DatabaseAccountDAO(Context context) {
        super(context, "Appdbms.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Account(accountNo TEXT primary key, bankName TEXT, accountHolderName TEXT, balance TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Account");
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers =new ArrayList<String>();
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select accountNo from Account",null);
        while(cursor.moveToNext()){
           accountNumbers.add(cursor.getString(0));
        }

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts =new ArrayList<Account>();
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Account",null);
        while(cursor.moveToNext()){
            accounts.add(new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            ));
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Account where accountNo = ?", new String[] {accountNo});
        if(cursor.getCount() > 0) {
            return new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase DB = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", account.getAccountNo());
        contentValues.put("bankName", account.getBankName());
        contentValues.put("accountHolderName", account.getAccountHolderName());
        contentValues.put("balance", Double. toString(account.getBalance()));
        long result = DB.insert("Account", null, contentValues);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Account where accountNo = ?", new String[] {accountNo});

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                long result = DB.delete("Account", "accountNo=?", new String[]{accountNo});
            }
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase DB = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = DB.rawQuery("Select * from Account where accountNo = ?", new String[] {accountNo});

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                String balance = "";
                //System.out.println(cursor.getString(-1));
                switch (expenseType) {
                    case EXPENSE:
                        balance = Double.toString(Double.parseDouble(cursor.getString(3)) - amount);
                        break;
                    case INCOME:
                        balance = Double.toString(Double.parseDouble(cursor.getString(3)) + amount);
                        break;
                }
                contentValues.put("bankName", cursor.getString(1));
                contentValues.put("accountHolderName", cursor.getString(2));
                contentValues.put("balance", balance);
                long result = DB.update("Account", contentValues, "accountNo=?", new String[]{accountNo});
            }
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
}
