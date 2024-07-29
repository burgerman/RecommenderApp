package com.group7.recommenderapp.ui.help;

public class HelpPresenter implements HelpContract.Presenter {
    private HelpContract.View view;

    public HelpPresenter(HelpContract.View view) {
        this.view = view;
    }

    @Override
    public void loadHelpContent() {
        String helpContent = "Welcome to the Recommender App!\n\n" +
                "Here are some tips to get started:\n\n" +
                "1. Home: View your top recommended content.\n" +
                "2. Movies: Explore movie recommendations.\n" +
                "3. Music: Discover new music based on your preferences.\n" +
                "4. User Profile: Update your profile and preferences.\n\n" +
                "Enjoy your personalized recommendations!";
        view.displayHelpContent(helpContent);
    }
}
