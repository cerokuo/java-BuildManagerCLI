package com.knowroaming.service;

import com.knowroaming.model.Exceptions.DuplicatedEmailException;
import com.knowroaming.model.UserDTO;
import com.knowroaming.model.UserDataHistoryDTO;
import com.knowroaming.model.Exceptions.InformationNotFoundException;
import com.knowroaming.store.UserStoreManagerImpl;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Created by cerokuo on 02/04/2017.
 */

@Test
public class UserServiceManagerTest {

    @Mock
    private UserStoreManagerImpl userStoreManagerImplMock;
    @Mock
    private UserDTO userDTOMock;
    @Mock
    private UserDataHistoryDTO userDataHistoryDTOMock;
    @Mock
    private UserServiceManager userServiceManagerMock;

    private UserServiceManager userServiceManager;
    private String name;
    private String email;
    private String phone;
    private String country;
    private String userID;
    private String typeOfUsage;
    private String date;
    private String endDate;
    private List<UserDataHistoryDTO> userDataHistoryDTOList;

    @BeforeMethod
    public void SetUp() {
        MockitoAnnotations.initMocks(this);
        userServiceManager = new UserServiceManager();
        name = "Ivan";
        email = "ivan@example.com";
        phone = "676-854-7784";
        country = "Canada";
        userID = "1234A123C";
        typeOfUsage = "DATA";
        date = "2017-01-20";
        endDate = "2017-05-20";

        userDataHistoryDTOList = new ArrayList<UserDataHistoryDTO>();

        UserDataHistoryDTO userHist = new UserDataHistoryDTO();
        userHist.setUserID(userID);
        userHist.setDataTypeID(typeOfUsage);
        userHist.setStarDate(date);

        userDataHistoryDTOList.add(userHist);
        userDataHistoryDTOList.add(userHist);
        userDataHistoryDTOList.add(userHist);


        userServiceManager.setUserStoreManager(userStoreManagerImplMock);

        when(userStoreManagerImplMock.addUser(any(UserDTO.class))).thenReturn(userID);
        when(userStoreManagerImplMock.searchEmail(email)).thenReturn(false);
        when(userStoreManagerImplMock.searchUser(userID)).thenReturn(true);
        when(userStoreManagerImplMock.checkType(typeOfUsage)).thenReturn(true);
        when(userStoreManagerImplMock.checkHistory(any(UserDataHistoryDTO.class))).thenReturn(userDataHistoryDTOList);
    }

    /**
     * Scenario for Succesful data storage for a new user.
     */
    public void getCommandOneTest() {
        assertEquals(userServiceManager.commandOne(name, email, country, phone), "1234A123C");
    }


    /**
     * Sceneario for succesful data storage of a user activity
     */
    public void getCommandTwoTest() {
        userServiceManager.commandTwo(userID, typeOfUsage, date);
        verify(userServiceManagerMock, times(0)).commandTwo(anyString(), anyString(), anyString());
    }


    /**
     * Secenario for Succesful data check of all the activity of a user.
     */
    public void getCommandThreeTest() {
        List<String> histList = new ArrayList<String>();
        for(UserDataHistoryDTO user : userDataHistoryDTOList) {
            histList.add(user.toString());
        }
        assertEquals(histList, userServiceManager.commandThree(userID, date, endDate, typeOfUsage));
    }


    /**
     * Scenario for failed UserID doesnt exist in DDBB
     */
    @Test(expectedExceptions = InformationNotFoundException.class)
    public void getCommandThreeIDDoesntExistTest() {
        when(userStoreManagerImplMock.searchUser(userID)).thenReturn(false);
        userServiceManager.commandThree(userID, date, endDate, typeOfUsage);
    }

    /**
     * Scenario for failed Start Date is bigger than End Date.
     */
    @Test(expectedExceptions = InvalidParameterException.class)
    public void getCommandThreeStartDateBiggerThanTest() {
        userServiceManager.commandThree(userID, endDate, date, typeOfUsage);
    }

    /**
     * Scenario for failed DateFormat is incorrect
     */
    @Test(expectedExceptions = InvalidParameterException.class)
    public void getCommandThreeWrongDateFormatTest() {
        userServiceManager.commandThree(userID, "20176589", "20154569", typeOfUsage);
    }


    /**
     * Scenario for failed Type not found in DDBB
     */
    @Test(expectedExceptions = InformationNotFoundException.class)
    public void getCommandTwoNonTypeFoundedTest() {
        when(userStoreManagerImplMock.checkType(typeOfUsage)).thenReturn(false);
        userServiceManager.commandTwo(userID, typeOfUsage, date);
    }

    /**
     * Scenario for failed UserID does not exist in the DDBB.
     */
    @Test(expectedExceptions = InformationNotFoundException.class)
    public void getCommandTwoNonUserIDTest() {
        when(userStoreManagerImplMock.searchUser(userID)).thenReturn(false);
        userServiceManager.commandTwo(userID, typeOfUsage, date);
    }

    /**
     * Scenario for failed DateFormat is incorrect
     */
    @Test(expectedExceptions = InvalidParameterException.class)
    public void getCommandTwoWrongDateFormatTest() {
        userServiceManager.commandTwo(userID, typeOfUsage, "20170505");
    }


    /**
     * Scenario for failed name format.
     */
    @Test(expectedExceptions = InvalidParameterException.class)
    public void getCommandOneWrongNameFormatTest() {
        userServiceManager.commandOne("---", email, country, phone);
    }

    /**
     * Scenario for failed phone number format
     */
    @Test(expectedExceptions = InvalidParameterException.class)
    public void getCommandOneWrongPhoneNumberFormatTest() {
        userServiceManager.commandOne(name, email, country, "+1655-4545-54");

    }

    /**
     * Scenario for failed email format
     */
    @Test(expectedExceptions = InvalidParameterException.class)
    public void getCommandOneWrongEmailFormatTest() {
        userServiceManager.commandOne(name, "ivan@test", country, phone);
    }

    /**
     * Scenario for failed duplicated email in DDBB
     */
    @Test(expectedExceptions = DuplicatedEmailException.class)
    public void getCommandOneEmailDuplicatedInDDBBTest() {
        when(userStoreManagerImplMock.searchEmail(email)).thenReturn(true);
        userServiceManager.commandOne(name, email, country, phone);
    }



}