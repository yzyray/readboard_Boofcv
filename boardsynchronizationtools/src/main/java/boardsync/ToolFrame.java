package boardsync;

import java.awt.BorderLayout;
import java.awt.Container;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ToolFrame extends JFrame {
  private JPanel dialogPane = new JPanel();
  private JPanel contentPanel = new JPanel();
  private JPanel buttonBar = new JPanel();
  private final JButton btnNewButton_1 = new JButton("test1");
  private final JButton btnNewButton_2 = new JButton("New button");
  private final JButton btnNewButton_3 = new JButton("New button");
  private final JButton btnNewButton_4 = new JButton("New button");
  private final JButton btnNewButton_5 = new JButton("New button");
  private final JButton btnNewButton_6 = new JButton("New button");
  private final JButton btnNewButton_7 = new JButton("New button");
  private final JButton btnNewButton_8 = new JButton("New button");

  public ToolFrame() {
    initComponents();
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
    dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
    dialogPane.setLayout(new BorderLayout());

    initContentPanel();
    initButtonBar();

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

  private void initContentPanel() {

    dialogPane.add(contentPanel);
    contentPanel.setLayout(new GridLayout(2, 3, 0, 0));
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

    contentPanel.add(btnNewButton_3);

    contentPanel.add(btnNewButton_4);

    contentPanel.add(btnNewButton_5);

    contentPanel.add(btnNewButton_6);
  }

  private void initButtonBar() {

    buttonBar.setLayout(new GridBagLayout());
    buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));

    GridBagConstraints gbc_button = new GridBagConstraints();
    gbc_button.anchor = GridBagConstraints.EAST;
    gbc_button.insets = new Insets(0, 0, 0, 5);
    gbc_button.gridx = 0;
    gbc_button.gridy = 0;
    buttonBar.add(btnNewButton_7, gbc_button);

    GridBagConstraints gbc_button_1 = new GridBagConstraints();
    gbc_button_1.anchor = GridBagConstraints.EAST;
    gbc_button_1.gridx = 1;
    gbc_button_1.gridy = 0;
    buttonBar.add(btnNewButton_8, gbc_button_1);
    dialogPane.add(buttonBar, BorderLayout.SOUTH);
  }
}
