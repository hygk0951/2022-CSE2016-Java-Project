import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame{
    public Frame(){
        setTitle("TEST Frame");
        setSize(500, 250);
        // 화면 중앙에 배치
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
