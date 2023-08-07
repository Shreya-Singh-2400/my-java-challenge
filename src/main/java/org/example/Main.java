package org.example;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.*;
import java.util.logging.Logger;
public class Main {
    private static Scanner read = new Scanner(System.in);
    private static ArrayList<BankAccount> accounts;


    public static void addAccount() {
        System.out.println("Please choose the Account type: \n(a) BankAccount\n(b) SavingsAccount\n");
        char input = read.next().charAt(0);

        System.out.println("Please enter the name: ");
        String name = read.next();

        System.out.println("Please enter the balance: ");
        double balance = read.nextDouble();

        BankAccount newAccount;
        String accountType = "";

        switch (input) {
            case 'a':
                accountType = "BankAccount";
                newAccount = new BankAccount(name, balance);
                break;


            case 'b':
                accountType = "SavingAccount";
                newAccount = new SavingsAccount(name, balance);
                break;


            default:
                System.out.println("Wrong Input!!");
                return;
        }
        System.out.println("Account Created: \n" + newAccount);
        accounts.add(newAccount);
    }

    public static void displayAccounts() {
        if (accounts.size() > 0) {
            for (int i = 0; i < accounts.size(); i++)
                System.out.println("Account " + (i + 1) + ":\n" + accounts.get(i) + "\n");
        } else {
            System.out.println("No Accounts created!!");
        }
    }


    public static void saveToDB(ArrayList<BankAccount> accounts, MongoCollection<Document> accountsCollection) {



        for (BankAccount account : accounts) {
            Document doc = new Document()
                    .append("Account Type", account.accountType)
                    .append("Name", account.getAccountName())
                    .append("Balance", account.getBalance());
            accountsCollection.insertOne(doc);
        }
    }


    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("BankAccounts");
        MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
            Logger logger = Logger.getLogger("BankAccount.class");
            accounts = new ArrayList<>();
            char input = 'w';
            while (input != 'q') {
                System.out.println("Enter the option: \n(a) Add Account\n(l) Display Accounts\n(s) Save to DataBase\n(q) Quit\n");
                input = read.next().charAt(0);


                switch (input) {
                    case 'a':
                        addAccount();
                        break;
                    case 'l':
                        displayAccounts();
                        break;
                    case 's':
                        saveToDB(accounts, accountsCollection);
                        break;
                    case 'q':
                        System.out.println("Quitting!!");
                        break;
                    default:
                        System.out.println("Wrong Input!!");
                        break;
                }
            }
        }

    }
