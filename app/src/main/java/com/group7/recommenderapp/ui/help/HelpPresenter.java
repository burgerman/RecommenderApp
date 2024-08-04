package com.group7.recommenderapp.ui.help;

public class HelpPresenter implements HelpContract.Presenter {
    private static final String AUTHOR_NAMES = "Saurab S. Sahoo, David Dai, Yash R. Ahuja, Sayan Banerjee, and Menghao Wu";
    private static final String VERSION = "1.0.0";
    private static final String INSTRUCTIONS = "1. Home: View your top recommended content.\n" +
            "2. Movies: Explore movie recommendations.\n" +
            "3. Music: Discover new music based on your preferences.\n" +
            "4. User Profile: Update your profile and preferences.\n\n" +
            "Enjoy your personalized recommendations!";

    private HelpContract.View view;

    public HelpPresenter(HelpContract.View view) {
        this.view = view;
    }

    @Override
    public void loadHelpContent() {
        String helpContent = "Welcome to the Recommender App!\n\n" +
                "Authors: " + AUTHOR_NAMES + "\n" +
                "\nVersion: " + VERSION + "\n\n" +
                "Instructions:\n" + INSTRUCTIONS;
        view.displayHelpContent(helpContent);
    }

    @Override
    public void showHelpDialog() {
        view.showHelpDialog(AUTHOR_NAMES, VERSION, INSTRUCTIONS);
    }
}