package com.group7.recommenderapp.ui.help;

public interface HelpContract {
    interface View {
        void displayHelpContent(String content);
    }

    interface Presenter {
        void loadHelpContent();
    }
}
