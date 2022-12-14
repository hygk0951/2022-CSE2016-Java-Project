import java.time.LocalDateTime;

public class BlackJackWorld {
    public static void main(String[] args) {
        ControlManager viewControlManager = new ControlManager();

        IntroView introView = new IntroView(viewControlManager,"Intro", 400, 200);
        viewControlManager.setIntroView(introView);
        introView.setVisible(true);
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("TEST");
    }
}