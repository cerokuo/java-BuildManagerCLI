package com.knowroaming.service;

import com.google.inject.Inject;
import com.knowroaming.model.Exceptions.DuplicatedEmailException;
import com.knowroaming.model.UserDTO;
import com.knowroaming.model.UserDataHistoryDTO;
import com.knowroaming.model.Exceptions.InformationNotFoundException;
import com.knowroaming.service.Utils.DataEntryUtils;
import com.knowroaming.store.UserStoreManager;
import com.knowroaming.store.UserStoreManagerImpl;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cerokuo on 02/04/2017.
 */
public class UserServiceManager {

    @Inject
    private UserStoreManager userStoreManager;

    /**
     * This method allow you to add a new user and return the ID of the new user in DDBB.
     * @param name
     * @param email
     * @param country
     * @param phone
     * @return
     */
    public String commandOne(String name, String email,String country, String phone) {
        checkParameters(name, email, phone);
        if(isEmailDuplicated(email)) {
            throw new DuplicatedEmailException("The e-mail is already in the DDBB");
        }
        return userStoreManager.addUser(generateUser(name, email, country, phone));
    }


    /**
     * This method allow you to add a new service contracted by a user.
     * @param userID
     * @param typeOfUsage
     * @param date
     */
    public void commandTwo(String userID, String typeOfUsage, String date) {
        checkUser(userID);
        checkUsages(typeOfUsage);
        CheckDateFormat(date);

        UserDataHistoryDTO userDataHistory = generateDataHistory(userID, typeOfUsage, date, null);
        userStoreManager.addService(userDataHistory);
    }


    /**
     * This method show you on screen a history of the usages that the user hired.
     * @param userID
     * @param startDate
     * @param endDate
     * @param typeOfUsage
     * @return
     */
    public List<String> commandThree(String userID, String startDate, String endDate, String typeOfUsage) {

        checkUser(userID);
        List<String> userHistory = new ArrayList<String>();
        if(!typeOfUsage.equals("ALL")){
            checkUsages(typeOfUsage);
        }
        checkDates(startDate, endDate);

        UserDataHistoryDTO userDataHistory = generateDataHistory(userID, typeOfUsage, startDate, endDate);
        for(UserDataHistoryDTO user : userStoreManager.checkHistory(userDataHistory)) {
            userHistory.add(user.toString());
        }
        return userHistory;
    }


    /**
        Methods that chech the correct format of the data.
     */
    private void checkUsages(String typeOfUsage) {
        if(!isTypeCorrect(typeOfUsage)) {
            throw new InformationNotFoundException(String.format("Type of usage: %s , doesn't exist", typeOfUsage));
        }
    }

    private void CheckDateFormat(String date) {
        DataEntryUtils.convertStringDateToTimestamp(date);
    }

    private void checkDates(String startDate, String endDate) {

        Timestamp startDateFormated = DataEntryUtils.convertStringDateToTimestamp(startDate);
        Timestamp endDateFormated = DataEntryUtils.convertStringDateToTimestamp(endDate);

        if(DataEntryUtils.isDateBiggerThan(startDateFormated,endDateFormated)) {
            throw new InvalidParameterException(String.format("StartDate:  %s , should be smaller than endDate: %s",
                    startDate,
                    endDate));
        }
    }

    /**
        Methods that validate data from UTILS
     */

    private void checkUser(String userID) {
        if(!checkUserIDExist(userID)){
            throw new InformationNotFoundException(String.format("User with userID: %s , doesn't exist", userID));
        }
    }


    private void checkParameters(String name, String email, String phone) {
        if(!DataEntryUtils.isValidUserName(name)) {
            throw new InvalidParameterException(String.format("Name: %s , should consist of chracters only", name));
        }
        if(!DataEntryUtils.isValidEmail(email)) {
            throw new InvalidParameterException(String.format("Email:  %s , format is incorrect, the email mas have first  @ and after a  .", email));
        }
        if(!DataEntryUtils.isValidPhoneNumber(phone)) {
            throw new InvalidParameterException(String.format("Phone number: %s , Phone number must has only numbers and dashes.", phone));
        }
    }

    /**
     * Methods that validate data from DDBB
     */

    private boolean checkUserIDExist(String userID) {
        return userStoreManager.searchUser(userID);
    }

    private boolean isEmailDuplicated(String email) {
        return userStoreManager.searchEmail(email);
    }

    private boolean isTypeCorrect(String typeOfUsage) {
        return userStoreManager.checkType(typeOfUsage);
    }



    /**
        Methods to set the value of the injection for the tests.
     */

    public void setUserStoreManager(UserStoreManagerImpl userStoreManager) {
        this.userStoreManager = userStoreManager;
    }

    /**
    * Methods to set data into the objects
    * */

    private UserDTO generateUser(String name, String email, String country, String phone) {
        UserDTO user = new UserDTO();
        user.setName(name);
        user.setCountry(country);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        return user;
    }

    private UserDataHistoryDTO generateDataHistory(String userID, String typeOfUsage, String startDate, String endDate) {
        UserDataHistoryDTO userDataHistory = new UserDataHistoryDTO();
        userDataHistory.setUserID(userID);
        userDataHistory.setDataTypeID(typeOfUsage);
        userDataHistory.setStarDate(startDate);
        userDataHistory.setEndDate(endDate);
        return userDataHistory;
    }



}
