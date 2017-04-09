package com.knowroaming.store;

import com.knowroaming.model.UserDTO;
import com.knowroaming.model.UserDataHistoryDTO;

import java.util.List;

/**
 * Created by cerokuo on 03/04/2017.
 */
public interface UserStoreManager {

    String addUser(UserDTO user);
    void addService(UserDataHistoryDTO userDataHistory);
    List<UserDataHistoryDTO> checkHistory(UserDataHistoryDTO userDataHistory);

    boolean searchUser(String userID);
    boolean checkType(String typeOfUsage);
    boolean searchEmail(String email);
}
