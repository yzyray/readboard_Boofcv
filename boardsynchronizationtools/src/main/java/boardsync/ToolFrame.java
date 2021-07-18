package boardsync;

import java.awt.AWTException;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ToolFrame extends JFrame {
  /** */
  private static final long serialVersionUID = 1L;

  private JPanel dialogPane = new JPanel();
  private JPanel northPane = new JPanel();
  private JPanel centerPane = new JPanel();
  private JPanel southPane = new JPanel();
  private final JButton btnSelectBoard = new JButton("框选棋盘");
  private final JButton btnSelectRow1 = new JButton("框选1路线");
  private final JButton btnHelp = new JButton("帮助");
  private final JButton btnKeepSync = new JButton("持续同步(200ms)");
  private final JButton btnOneTimeSync = new JButton("单次同步");
  private final JButton btnSettings = new JButton("参数设置");
  private final JCheckBox chkBothSync = new JCheckBox("双向同步");
  private final JCheckBox chkAutoPlay = new JCheckBox("自动落子");
  private final JRadioButton rdoPlayBlack = new JRadioButton("执黑");
  private final JRadioButton rdoPlayWhite = new JRadioButton("执白");
  private final JTextField txtTotalVisits = new JTextField();
  private final JLabel lblTotalVisits = new JLabel("总计算量:");
  private final JTextField txtTotalTime = new JTextField();
  private final JLabel lblTotalTime = new JLabel("每手用时:");
  private final JButton btnSet65Komi = new JButton("6.5目设置方法");
  private final JPanel boardPane = new JPanel();
  private final JLabel lblBoard = new JLabel("棋盘:");
  private final JTextField txtBoardWidth = new JTextField();
  private final JLabel lblTimes = new JLabel("*");
  private final JTextField txtBoardHeight = new JTextField();
  private final JButton btnPass = new JButton("交换顺序");
  private final JButton btnClear = new JButton("清空棋盘");
  private final JButton btnToggleAnalyze = new JButton("停止/分析");
  private JFrame thisFrame = this;

  public ToolFrame() {
    txtBoardHeight.setColumns(2);
    txtBoardWidth.setColumns(2);
    txtTotalTime.setColumns(6);
    txtTotalVisits.setColumns(6);
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

  private void initSouth() {
    dialogPane.add(southPane, BorderLayout.SOUTH);
    southPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    southPane.setLayout(new GridLayout(1, 3, 0, 0));
    southPane.add(btnToggleAnalyze);
    southPane.add(btnPass);
    southPane.add(btnClear);
  }

  private void initNorth() {
    dialogPane.add(northPane, BorderLayout.NORTH);
    northPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    northPane.setLayout(new GridLayout(2, 4, 0, 0));
    btnSelectBoard.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            SwingUtilities.invokeLater(
                new Runnable() {
                  public void run() {
                    selectBoard();
                  }
                });
          }
        });
    northPane.add(btnSelectBoard);
    northPane.add(btnSelectRow1);
    northPane.add(btnHelp);
    northPane.add(btnKeepSync);
    btnOneTimeSync.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            SwingUtilities.invokeLater(
                new Runnable() {
                  public void run() {
                    if (BoardSyncTool.boardPosition == null) {
                      JOptionPane.showMessageDialog(
                          thisFrame, "未选择棋盘", "消息提醒", JOptionPane.WARNING_MESSAGE);
                    } else {
                      BoardOCR boardOCR = new BoardOCR();
                      try {
                        boardOCR.oneTimeSync();
                      } catch (AWTException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                      }
                    }
                  }
                });
          }
        });
    northPane.add(btnOneTimeSync);
    northPane.add(btnSettings);
  }

  private void initCenter() {

    GridBagLayout gbl_buttonBar = new GridBagLayout();
    gbl_buttonBar.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0};
    centerPane.setLayout(gbl_buttonBar);
    // buttonBar.setBorder(new EmptyBorder(12, 12, 12, 12));

    centerPane.setBorder(BorderFactory.createEtchedBorder());
    dialogPane.add(centerPane, BorderLayout.CENTER);
    GridBagConstraints gbc_btnSet65Komi = new GridBagConstraints();
    gbc_btnSet65Komi.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnSet65Komi.insets = new Insets(2, 0, 2, 2);
    gbc_btnSet65Komi.gridx = 0;
    gbc_btnSet65Komi.gridy = 0;
    centerPane.add(btnSet65Komi, gbc_btnSet65Komi);

    GridBagConstraints gbc_chkBothSync = new GridBagConstraints();
    gbc_chkBothSync.fill = GridBagConstraints.HORIZONTAL;
    gbc_chkBothSync.insets = new Insets(2, 0, 2, 2);
    gbc_chkBothSync.gridx = 1;
    gbc_chkBothSync.gridy = 0;
    centerPane.add(chkBothSync, gbc_chkBothSync);

    GridBagConstraints gbc_rdoPlayBlack = new GridBagConstraints();
    gbc_rdoPlayBlack.fill = GridBagConstraints.HORIZONTAL;
    gbc_rdoPlayBlack.insets = new Insets(2, 0, 2, 2);
    gbc_rdoPlayBlack.gridx = 2;
    gbc_rdoPlayBlack.gridy = 0;
    centerPane.add(rdoPlayBlack, gbc_rdoPlayBlack);

    GridBagConstraints gbc_lblTotalTime = new GridBagConstraints();
    gbc_lblTotalTime.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblTotalTime.insets = new Insets(2, 0, 2, 2);
    gbc_lblTotalTime.gridx = 3;
    gbc_lblTotalTime.gridy = 0;
    centerPane.add(lblTotalTime, gbc_lblTotalTime);

    GridBagConstraints gbc_txtTotalTime = new GridBagConstraints();
    gbc_txtTotalTime.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtTotalTime.insets = new Insets(2, 0, 2, 0);
    gbc_txtTotalTime.gridx = 4;
    gbc_txtTotalTime.gridy = 0;
    centerPane.add(txtTotalTime, gbc_txtTotalTime);
    GridBagConstraints gbc_panel = new GridBagConstraints();
    gbc_panel.fill = GridBagConstraints.HORIZONTAL;
    gbc_panel.insets = new Insets(0, 0, 2, 2);
    gbc_panel.gridx = 0;
    gbc_panel.gridy = 1;
    boardPane.add(lblBoard);
    boardPane.add(txtBoardWidth);
    boardPane.add(lblTimes);
    boardPane.add(txtBoardHeight);
    centerPane.add(boardPane, gbc_panel);
    boardPane.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));

    GridBagConstraints gbc_chkAutoPlay = new GridBagConstraints();
    gbc_chkAutoPlay.fill = GridBagConstraints.HORIZONTAL;
    gbc_chkAutoPlay.insets = new Insets(0, 0, 2, 2);
    gbc_chkAutoPlay.gridx = 1;
    gbc_chkAutoPlay.gridy = 1;
    centerPane.add(chkAutoPlay, gbc_chkAutoPlay);

    GridBagConstraints gbc_rdoPlayWhite = new GridBagConstraints();
    gbc_rdoPlayWhite.fill = GridBagConstraints.HORIZONTAL;
    gbc_rdoPlayWhite.insets = new Insets(0, 0, 2, 2);
    gbc_rdoPlayWhite.gridx = 2;
    gbc_rdoPlayWhite.gridy = 1;
    centerPane.add(rdoPlayWhite, gbc_rdoPlayWhite);

    GridBagConstraints gbc_lblTotalVisits = new GridBagConstraints();
    gbc_lblTotalVisits.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblTotalVisits.insets = new Insets(0, 0, 2, 2);
    gbc_lblTotalVisits.gridx = 3;
    gbc_lblTotalVisits.gridy = 1;
    centerPane.add(lblTotalVisits, gbc_lblTotalVisits);

    GridBagConstraints gbc_txtTotalVisits = new GridBagConstraints();
    gbc_txtTotalVisits.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtTotalVisits.insets = new Insets(0, 0, 2, 0);
    gbc_txtTotalVisits.gridx = 4;
    gbc_txtTotalVisits.gridy = 1;
    centerPane.add(txtTotalVisits, gbc_txtTotalVisits);
  }

  private void selectBoard() {
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
          BoardSyncTool.boardPosition = lineDetection.getBoardPosition(BoardSyncTool.screenImage);
        }
      }
    }.start();
  }
}
