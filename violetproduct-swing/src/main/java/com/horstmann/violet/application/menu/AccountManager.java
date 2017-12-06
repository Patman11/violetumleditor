/*
    Made for COMP 354
 */
package com.horstmann.violet.application.menu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class manages the "database" of usernames and passwords It manages who
 * is logged in
 *
 * @author Bruno
 */
public class AccountManager {

    private String[][] accountArr;// [i][0] => username; [i][1] => password; 
    private String readFile;//stores the location of the userdatabase
    private boolean loggedIn;
    private String currentUsr;

    /**
     * Constructor
     *
     * @param userfile accounts database to be read and written to
     */
    AccountManager(String userfile) {
        readFile = userfile;
        readFile(readFile);
        loggedIn = false;
        currentUsr = "";
    }

    /**
     * converts two lists into one 2 dimensional array
     *
     * @param names list of usernames
     * @param passwords list of passwords
     * @return 2 dimensional array of usernames and passwords
     */
    static String[][] listToArr(List<String> names, List<String> passwords) {
        String[][] accounts = new String[names.size()][2];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i][0] = names.get(i);
            accounts[i][1] = passwords.get(i);
        }

        return accounts;
    }

    /**
     * Reads data from the accounts "database"
     *
     * @param inputFile file to read
     */
    public void readFile(String inputFile) {
        BufferedReader bReader = null;
        List<String> userList = new ArrayList<String>();
        List<String> passwordList = new ArrayList<String>();
        
        //create the file if it doesn't exist
        File f = new File(inputFile);
        if (!f.exists() || f.isDirectory()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            // .txt File from Class model Diagram containing information
            bReader = new BufferedReader(new FileReader(inputFile));
            String str;

            // read lines and put information into the 3 different Lists
            while ((str = bReader.readLine()) != null) {
                String user = str.substring(0, str.indexOf(":"));
                userList.add(user);

                String password = str.substring(str.indexOf(":") + 1);
                passwordList.add(password);
            }

            // Convert lists to arrays a writes to class attributes
            accountArr = listToArr(userList, passwordList);

        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Writes to the accounts "database"
     *
     * @param outputFile file to write to
     */
    public void writeFile(String outputFile) {
        BufferedWriter bWriter = null;

        try {
            bWriter = new BufferedWriter(new FileWriter(outputFile));
            for (int i = 0; i < accountArr.length; i++) {
                String str = accountArr[i][0] + ":" + accountArr[i][1] + "\n";
                bWriter.write(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks weather or not a given username is in the database
     *
     * @param username username to check for
     * @return true if the username is found, false otherwise
     */
    public boolean doesUserExist(String username) {
        for (int i = 0; i < accountArr.length; i++) {
            if (accountArr[i][0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Registers a new user to the database, if the username already exists
     * returns false if the username is unique registers it, updates the file
     * and returns true
     *
     * @param username username to register
     * @param password password associated with username
     * @return true if the user has been registered, false otherwise
     */
    public boolean registerUser(String username, String password) {
        if (doesUserExist(username)) {
            return false;
        } else {
            String[][] temp = new String[accountArr.length + 1][2];
            for (int i = 0; i < accountArr.length; i++) {
                temp[i][0] = accountArr[i][0];
                temp[i][1] = accountArr[i][1];
            }
            temp[accountArr.length][0] = username;
            temp[accountArr.length][1] = password;
            accountArr = temp;
            writeFile(readFile);
            return true;
        }
    }

    /**
     * Logs user in if the username and password have a match in the "database"
     *
     * @param username username to login
     * @param password associated password
     * @return true if user has been logged in, false otherwise
     */
    public boolean login(String username, String password) {
        for (int i = 0; i < accountArr.length; i++) {
            //System.out.println(accountArr[i][0] + username + accountArr[i][1] + password);
            if (accountArr[i][0].equals(username) && accountArr[i][1].equals(password)) {
                currentUsr = username;
                loggedIn = true;
                return true;
            }
        }
        return false;

    }

    /**
     * Logs current user out (works if no user is logged in)
     */
    public void logout() {
        currentUsr = "";
        loggedIn = false;
    }

}
