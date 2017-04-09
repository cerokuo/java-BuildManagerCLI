package com.knowroaming.service.Config;

import com.google.inject.AbstractModule;
import com.knowroaming.store.UserStoreManager;
import com.knowroaming.store.UserStoreManagerImpl;

/**
 * Created by cerokuo on 03/04/2017.
 */
public class KnowRoamingModule extends AbstractModule {
    protected void configure() {
        bind(UserStoreManager.class).to(UserStoreManagerImpl.class);
    }
}
