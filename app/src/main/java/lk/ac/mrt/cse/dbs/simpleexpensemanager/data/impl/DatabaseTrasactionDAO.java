package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseTrasactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    public DatabaseTrasactionDAO(Context context) {
        super(context, "Appdbmst.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Transdata(date TEXT, accountNo TEXT, expenseType TEXT, amount TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Transdata");
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase DB = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        contentValues.put("date", df.format(date));
        contentValues.put("accountNo", accountNo);
        contentValues.put("expenseType", String.valueOf(expenseType));
        contentValues.put("amount", Double. toString(amount));
        long result = DB.insert("Transdata", null, contentValues);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions =new ArrayList<Transaction>();
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Transdata",null);
        while(cursor.moveToNext()){
            Date date = new Date();
            try{
                date = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(0));
            }catch (Exception e){
                System.out.println(e);
            }

            transactions.add(new Transaction(
                    date,
                    cursor.getString(1),
                    ExpenseType.valueOf(cursor.getString(2)) ,
                    Double.parseDouble(cursor.getString(3))
            ));
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions =new ArrayList<Transaction>();
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Transdata",null);
        while(cursor.moveToNext()){
            Date date = new Date();
            try{
                date = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(0));
            }catch (Exception e){
                System.out.println(e);
            }

            transactions.add(new Transaction(
                    date,
                    cursor.getString(1),
                    ExpenseType.valueOf(cursor.getString(2)) ,
                    Double.parseDouble(cursor.getString(3))
            ));
        }

        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }

        return transactions.subList(size - limit, size);
    }
}
