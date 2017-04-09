package com.knowroaming.store;

import com.knowroaming.model.UserDTO;
import com.knowroaming.model.UserDataHistoryDTO;
import com.knowroaming.store.Exceptions.DataBaseConnectionException;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Created by cerokuo on 02/04/2017.
 */
@Test
public class UserStoreManagerImplTest {

    private UserStoreManagerImpl userStoreManager;
    private UserDTO user;
    private UserDataHistoryDTO userDataHistoryDTO;

    @BeforeMethod
    public void SetUp() {
        MockitoAnnotations.initMocks(this);
        userStoreManager = new UserStoreManagerImpl();

        user = new UserDTO();
        user.setName("Ivan");
        user.setEmail("ivan@example.com");
        user.setPhoneNumber("956-856-9745");
        user.setCountry("Canada");
        user.setId("3efa803d92");


        userDataHistoryDTO = new UserDataHistoryDTO();
        userDataHistoryDTO.setUserID("1234567890");
        userDataHistoryDTO.setDataTypeID("DATA");
        userDataHistoryDTO.setStarDate("2017-04-04");
        userDataHistoryDTO.setEndDate("2017-05-05");

    }

    /**
     * Scenario for Succesful to add a user in the DDBB
     */
    public void addUserTest() {
        assertNotNull(userStoreManager.addUser(user));
    }

    /**
     * Scenario for failed, error connecting with the DDBB
     */
    @Test(expectedExceptions = DataBaseConnectionException.class)
    public void addUserConnectionErrorTest() {
        userStoreManager.setDdbb_url("jdbc:errorDDBB");
        userStoreManager.addUser(user);
    }


    /**
     * Scenario for Succesful to add a usage into the DDBB
     */
    public void addUsageInformationTest() {
        userStoreManager.addService(userDataHistoryDTO);
    }

    /**
     * Scenario for failed, error connecting with the DDBB
     */
    @Test(expectedExceptions = DataBaseConnectionException.class)
    public void addUsageInformationConnectionErrorTest() {
        userStoreManager.setDdbb_url("jdbc:errorDDBB");
        userStoreManager.addService(userDataHistoryDTO);
    }


    /**
     * Scenario for Succesful to check and get the userList.
     */
    public void checkUsageHistTest() {
        assertNotNull(userStoreManager.checkHistory(userDataHistoryDTO));
        userDataHistoryDTO.setDataTypeID("ALL");
        assertNotNull(userStoreManager.checkHistory(userDataHistoryDTO));

    }

    /**
     * Scenario for failed, error connecting with the DDBB
     */
    @Test(expectedExceptions = DataBaseConnectionException.class)
    public void checkUsageHistConnectionErrorTest() {
        userStoreManager.setDdbb_url("jdbc:errorDDBB");
        userStoreManager.checkHistory(userDataHistoryDTO);
    }



    /**
     * Scenario for Succesful checking if an email exists and if not.
     */
    public void checkEmailExistTest() {
        assertFalse(userStoreManager.searchEmail("noexisted@email.com"));
        assertTrue(userStoreManager.searchEmail(user.getEmail()));

    }

    /**
     * Scenario for failed, error connecting with the DDBB
     */
    @Test(expectedExceptions = DataBaseConnectionException.class)
    public void checkEmailExistConnectionErrorTest() {
        userStoreManager.setDdbb_url("jdbc:errorDDBB");
        userStoreManager.searchEmail(user.getEmail());
    }


    /**
     * Scenario for Succesful checking if a usage exists and if not.
     */
    public void checkUsagesExistTest() {
        assertFalse(userStoreManager.checkType("VOICEMAIL"));
        assertTrue(userStoreManager.checkType("DATA"));

        assertFalse(userStoreManager.checkType(null));
    }

    /**
     * Scenario for failed, error connecting with the DDBB
     */
    @Test(expectedExceptions = DataBaseConnectionException.class)
    public void checkUsagesExistConnectionErrorTest() {
        userStoreManager.setDdbb_url("jdbc:errorDDBB");
        userStoreManager.checkType("DATA");
    }


    /***
     * Secenario for Succesful checking if a user exists and if not
     */
    public void searchUserTest(){

        String uuid = UUID.randomUUID().toString();
        String userID = uuid.replaceAll("-","").substring(0,10);

        assertTrue(userStoreManager.searchUser(user.getId()));
        assertFalse(userStoreManager.searchUser(userID));
    }

    /**
     * Scenario for failed, error connecting with the DDBB
     */
    @Test(expectedExceptions = DataBaseConnectionException.class)
    public void searchUserConnectionErrorTest() {
        userStoreManager.setDdbb_url("jdbc:errorDDBB");
        userStoreManager.searchUser(user.getId());    }





}