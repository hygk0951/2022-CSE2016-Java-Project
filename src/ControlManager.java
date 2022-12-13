// Controller of MVC

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlManager implements ActionListener {
    IntroView introView;

    public ControlManager(IntroView frame) {
        introView = frame;
    }

    public void actionPerformed(ActionEvent e) {
        // ActionEvent의 종류 구분
        switch (e.getActionCommand()) {
            // Timer 체크박스를 누른 경우
            case "Timer":
                boolean isChecked = introView.timerCheckBox.isSelected();
                introView.timerDecreaseButton.setEnabled(isChecked);
                introView.timerIncreaseButton.setEnabled(isChecked);
                introView.timerTextField.setEnabled(isChecked);
                break;
            // -5초를 누른경우
            case "-5":
                int timer = Integer.parseInt(introView.timerTextField.getText());
                if (timer > 10) introView.timerTextField.setText(String.valueOf(timer - 5));
                break;
            // +5초를 누른경우
            case "+5":
                timer = Integer.parseInt(introView.timerTextField.getText());
                if (timer < 60) introView.timerTextField.setText(String.valueOf(timer + 5));
                break;
            default:
                break;
        }
    }
}
