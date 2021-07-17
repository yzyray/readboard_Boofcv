package boardsync;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ToolFrame extends JFrame {
  private JPanel dialogPane = new JPanel();
  private JPanel contentPanel = new JPanel();
  private JPanel buttonBar = new JPanel();
  private JPanel southPane = new JPanel();
  private final JButton btnNewButton_1 = new JButton("框选棋盘");
  private final JButton btnNewButton_2 = new JButton("框选1路线");
  private final JButton btnNewButton_3 = new JButton("帮助");
  private final JButton btnNewButton_4 = new JButton("持续同步(200ms)");
  private final JButton btnNewButton_5 = new JButton("单次同步");
  private final JButton btnNewButton_6 = new JButton("参数设置");
  private final JCheckBox checkBox = new JCheckBox("双向同步");
  private final JCheckBox checkBox_1 = new JCheckBox("自动落子");
  private final JRadioButton radioButton = new JRadioButton("执黑");
  private final JRadioButton radioButton_1 = new JRadioButton("执白");
  private final JTextField textField = new JTextField();
  private final JLabel label_1 = new JLabel("总计算量:");
  private final JTextField textField_1 = new JTextField();
  private final JLabel label = new JLabel("每手用时:");
  private final JButton button = new JButton("6.5目设置方法");
  private final JPanel panel = new JPanel();
  private final JLabel label_2 = new JLabel("棋盘:");
  private final JTextField textField_2 = new JTextField();
  private final JLabel label_3 = new JLabel("*");
  private final JTextField textField_3 = new JTextField();
  private final JButton btnNewButton = new JButton("交换顺序");
  private final JButton btnNewButton_7 = new JButton("清空棋盘");

  public ToolFrame() {
    textField_3.setColumns(2);
    textField_2.setColumns(2);
    textField_1.setColumns(6);
    textField.setColumns(6);
    initComponents();
    this.setResizable(false);
    this.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        });
  }

  private void initComponents() {

    setTitle(BoardSyncTool.resourceBundle.getString("ToolFrame.title"));
    try {
      setIconImage(ImageIO.read(ToolFrame.class.getResourceAsStream("/assets/logo.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    initDialogPane(contentPane);

    pack();
    setLocationRelativeTo(getOwner());
  }

  private void initDialogPane(Container contentPane) {
    dialogPane.setBorder(new EmptyBorder(6, 10, 6, 10));
    dialogPane.setLayout(new BorderLayout());

    initNorth();
    initCenter();
    initSouth();

    contentPane.add(dialogPane, BorderLayout.CENTER);
  }

  private void test() {
    BoardSyncTool.isGettingScreen = true;
    this.setExtendedState(JFrame.ICONIFIED);
    SwingUtilities.invokeLater(
        new Runnable() {
          public void run() {
            ScreenShotDialog screenShotDialog = new ScreenShotDialog();
            screenShotDialog.start();
          }
        });
    new Thread() {
      public void run() {
        while (BoardSyncTool.isGettingScreen) {
          try {
            Thread.sleep(20);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        setExtendedState(JFrame.NORMAL);
        if (BoardSyncTool.screenImage != null) {
          LineDetection lineDetection = new LineDetection();
          lineDetection.getBoardPosition(BoardSyncTool.screenImage);
        }
      }
    }.start();
  }

  private void initSouth() {
    dialogPane.add(southPane, BorderLayout.SOUTH);
    southPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    southPane.setLayout(new GridLayout(1, 3, 0, 0));

    southPane.add(new JButton("停止/分析"));

    southPane.add(btnNewButton);

    southPane.add(btnNewButton_7);
  }

  private void initNorth() {

    dialogPane.add(contentPanel, BorderLayout.NORTH);
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPanel.setLayout(new GridLayout(2, 4, 0, 0));
    btnNewButton_1.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            SwingUtilities.invokeLater(
                new Runnable() {
                  public void run() {
                    test();
                  }
                });
          }
        });
    contentPanel.add(btnNewButton_1);

    contentPanel.add(btnNewButton_2);

    panel.add(label_2);

    panel.add(textField_2);

    panel.add(label_3);

    panel.add(textField_3);

    contentPanel.add(btnNewButton_3);

    contentPanel.add(btnNewButton_4);

    contentPanel.add(btnNewButton_5);

    contentPanel.add(btnNewButton_6);
  }

  private void initCenter() {

    GridBagLayout gbl_buttonBar = new GridBagLayout();
    gbl_buttonBar.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0};
    buttonBar.setLayout(gbl_buttonBar);
    // buttonBar.setBorder(new EmptyBorder(12, 12, 12, 12));

    buttonBar.setBorder(BorderFactory.createEtchedBorder());
    dialogPane.add(buttonBar, BorderLayout.CENTER);
    GridBagConstraints gbc_button = new GridBagConstraints();
    gbc_button.fill = GridBagConstraints.HORIZONTAL;
    gbc_button.insets = new Insets(3, 1, 5, 5);
    gbc_button.gridx = 0;
    gbc_button.gridy = 0;
    buttonBar.add(button, gbc_button);

    GridBagConstraints gbc_checkBox = new GridBagConstraints();
    gbc_checkBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_checkBox.insets = new Insets(3, 0, 5, 5);
    gbc_checkBox.gridx = 1;
    gbc_checkBox.gridy = 0;
    buttonBar.add(checkBox, gbc_checkBox);

    GridBagConstraints gbc_radioButton = new GridBagConstraints();
    gbc_radioButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_radioButton.insets = new Insets(3, 0, 5, 5);
    gbc_radioButton.gridx = 2;
    gbc_radioButton.gridy = 0;
    buttonBar.add(radioButton, gbc_radioButton);

    GridBagConstraints gbc_label = new GridBagConstraints();
    gbc_label.fill = GridBagConstraints.HORIZONTAL;
    gbc_label.insets = new Insets(3, 0, 5, 5);
    gbc_label.gridx = 3;
    gbc_label.gridy = 0;
    buttonBar.add(label, gbc_label);

    GridBagConstraints gbc_textField_1 = new GridBagConstraints();
    gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField_1.insets = new Insets(3, 0, 5, 1);
    gbc_textField_1.gridx = 4;
    gbc_textField_1.gridy = 0;
    buttonBar.add(textField_1, gbc_textField_1);
    GridBagConstraints gbc_panel = new GridBagConstraints();
    gbc_panel.fill = GridBagConstraints.HORIZONTAL;
    gbc_panel.insets = new Insets(0, 1, 3, 5);
    gbc_panel.gridx = 0;
    gbc_panel.gridy = 1;
    buttonBar.add(panel, gbc_panel);
    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    GridBagConstraints gbc_checkBox_1 = new GridBagConstraints();
    gbc_checkBox_1.fill = GridBagConstraints.HORIZONTAL;
    gbc_checkBox_1.insets = new Insets(0, 0, 3, 5);
    gbc_checkBox_1.gridx = 1;
    gbc_checkBox_1.gridy = 1;
    buttonBar.add(checkBox_1, gbc_checkBox_1);

    GridBagConstraints gbc_radioButton_1 = new GridBagConstraints();
    gbc_radioButton_1.fill = GridBagConstraints.HORIZONTAL;
    gbc_radioButton_1.insets = new Insets(0, 0, 3, 5);
    gbc_radioButton_1.gridx = 2;
    gbc_radioButton_1.gridy = 1;
    buttonBar.add(radioButton_1, gbc_radioButton_1);

    GridBagConstraints gbc_label_1 = new GridBagConstraints();
    gbc_label_1.fill = GridBagConstraints.HORIZONTAL;
    gbc_label_1.insets = new Insets(0, 0, 3, 5);
    gbc_label_1.gridx = 3;
    gbc_label_1.gridy = 1;
    buttonBar.add(label_1, gbc_label_1);

    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.insets = new Insets(0, 0, 3, 1);
    gbc_textField.gridx = 4;
    gbc_textField.gridy = 1;
    buttonBar.add(textField, gbc_textField);
  }
}
