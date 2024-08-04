package com.group7.recommenderapp.ui.help;

public interface HelpContract {
    interface View {
        void displayHelpContent(String content);
        void showHelpDialog(String authorNames, String version, String instructions);
    }

    interface Presenter {
        void loadHelpContent();
        void showHelpDialog();
    }
}