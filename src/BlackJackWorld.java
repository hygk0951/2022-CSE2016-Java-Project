public class BlackJackWorld {
    public static void main(String[] args) throws Exception {
        ControlManager viewControlManager = new ControlManager();

        IntroView introView = new IntroView(viewControlManager,"Intro", 500, 200);
        viewControlManager.setIntroView(introView);
        introView.setVisible(true);
    }
}