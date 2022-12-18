public class BlackJackWorld {
    public static void main(String[] args) throws Exception {
        ControlManager controlManager = new ControlManager();

        IntroView introView = new IntroView(controlManager,"Intro", 550, 200);
        controlManager.setIntroView(introView);
        introView.setVisible(true);
    }
}