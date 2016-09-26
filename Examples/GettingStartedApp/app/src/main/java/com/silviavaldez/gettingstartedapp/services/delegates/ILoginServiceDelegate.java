package com.silviavaldez.gettingstartedapp.services.delegates;

/**
 * Created by Silvia Valdez on 9/21/16.
 */
public interface ILoginServiceDelegate {

    void onLoginSuccess(boolean isLoggedIn);

    void onLoginFail(String message);

}
