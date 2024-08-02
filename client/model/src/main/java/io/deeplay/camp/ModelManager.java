package io.deeplay.camp;

import action.Action;
import chat.ChatModel;
import login.LoginModel;

import java.io.IOException;

public class ModelManager {

    private LoginModel loginModel;
    private ChatModel chatModel;
    private Action action;
    private String loginAndPassword;
    private String chatMessages;

    public ModelManager() throws IOException {
        action = new Action();
    }

    public void loginModelMethod(String loginAndPassword) {
        this.loginAndPassword = loginAndPassword;
        loginModel = new LoginModel(action, loginAndPassword);
    }

    public void chatModelMethod(String chatMessages) {
        this.chatMessages = chatMessages;
        chatModel = new ChatModel(action, chatMessages);
    }
}