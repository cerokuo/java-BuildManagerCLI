package com.knowroaming.rest.got;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.knowroaming.service.Config.KnowRoamingModule;
import com.knowroaming.service.UserServiceManager;

import java.io.Console;
import java.util.List;


/**
 * Created by cerokuo on 02/04/2017.
 */
public class KnowRoamingManagerCLI{

    public static void main(String[] args) {

        UserServiceManager userServiceManager = setUpInjectors();

        Console c =  System.console();
        if(c == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        welcomeMessage();
        String prompInfo = c.readLine("What is it going to be?");

        String[] params = prompInfo.split(" ");

        if(prompInfo.contains("addUser")){
            //addUser -- name email country phoneNumber

            if(params.length != 5){
                System.err.println("Then number of params are not correct, it must be 5");
                System.exit(1);
            }
            try {
                System.out.println(String.format("The userID is: %s",
                        userServiceManager.commandOne(params[1], params[2], params[3], params[4])));
            } catch (Exception e) {
                System.err.println("Something went wrong.. : " + e);
            }


        } else if(prompInfo.contains("addServices")) {
            //add services of a user -- userID usage time
            if(prompInfo.split(" ").length != 4) {
                System.err.println("Then number of params are not correct, it must be 3");
                System.exit(1);
            }
            try {
                userServiceManager.commandTwo(params[1], params[2], params[3]);
                System.out.println("info inserted correctly");
            } catch (Exception e) {
               System.err.println("Something went wrong.. : " + e);
            }

        } else if(prompInfo.contains("history")) {
            //get the history of a user -- userId startDate endDate data

            if(prompInfo.split(" ").length != 5) {
                System.err.println("Then number of params are not correct, it must be 4");
                System.exit(1);
            }

            try {
                List<String> userHist = userServiceManager.commandThree(params[1], params[2], params[3], params[4]);
                if(userHist.size() == 0) {
                    System.out.println(String.format("There is not any record with the userID: %s", params[1]));
                } else {
                    for (String user : userHist) {
                        System.out.println(user);
                    }
                }
            } catch (Exception e) {
                System.err.println("Something went wrong.. : " + e);
            }
        } else {
            //no correct option.
             System.out.println("You didn't write any correct option, please try again.");
        }

    }

    private static void welcomeMessage() {
        System.out.println("");
        System.out.println("");
        System.out.println("Wellcome to the knowRoaming app platform, please choose one option:");
        System.out.println("");
        System.out.println("");

        System.out.println("Add a new user: If you want to add a new user you should write -> " +
                        " 'addUser username email country phone'");
        System.out.println("");
        System.out.println("Add a new usage: If you want to add a new usage you should write -> " +
                " 'addServices userID usage date('year-month-day')'");
        System.out.println("");
        System.out.println("Check user history: If you want to see the history of a user you should write -> " +
                " 'history userID startDate('year-month-day') endDate('year-month-day') usage ");
        System.out.println("");
    }

    private static UserServiceManager setUpInjectors() {
        Injector injector = Guice.createInjector(new KnowRoamingModule());
        return injector.getInstance(UserServiceManager.class);
    }
}
