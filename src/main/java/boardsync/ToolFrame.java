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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class ToolFrame extends JFrame {
  /** */
  private static final long serialVersionUID = 1L;

  public boolean isKeepSyncing = false;
  public boolean keepSyncThreadInterrupted = false;
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
  public final JCheckBox chkBothSync = new JCheckBox("双向同步");
  private final JCheckBox chkAutoPlay = new JCheckBox("自动落子");
  private final JRadioButton rdoPlayBlack = new JRadioButton("执黑");
  private final JRadioButton rdoPlayWhite = new JRadioButton("执白");
  private final JTextField txtTotalVisits = new JTextField();
  private final JLabel lblTotalVisits = new JLabel("总计算量:");
  private final JTextField txtTotalTime = new JTextField();
  private final JLabel lblTotalTime = new JLabel("每手用时:");
  private final JPanel boardPane = new JPanel();
  private final JLabel lblBoard = new JLabel("棋盘:");
  private final JTextField txtBoardWidth = new JTextField();
  private final JLabel lblTimes = new JLabel("X");
  private final JTextField txtBoardHeight = new JTextField();
  private final JButton btnPass = new JButton("交换顺序");
  private final JButton btnClear = new JButton("清空棋盘");
  private final JButton btnToggleAnalyze = new JButton("停止/分析");
  private JFrame thisFrame = this;
  private final JLabel lblFirstVisits = new JLabel("首位:");
  private final JTextField txtFirstVisits = new JTextField();

  public ToolFrame() {
    txtFirstVisits.setColumns(6);
    txtBoardHeight.setColumns(2);
    txtBoardWidth.setColumns(2);
    txtTotalTime.setColumns(6);
    txtTotalVisits.setColumns(6);
    ButtonGroup rdoGroup = new ButtonGroup();
    rdoGroup.add(this.rdoPlayBlack);
    rdoGroup.add(this.rdoPlayWhite);

    this.chkBothSync.setSelected(BoardSyncTool.config.lastTimeBothSync);

    this.txtBoardWidth.setText(BoardSyncTool.boardWidth + "");
    this.txtBoardHeight.setText(BoardSyncTool.boardHeight + "");

    if (chkBothSync.isSelected()) {
      Utils.send("bothSync");
    } else {
      Utils.send("nobothSync");
      this.chkAutoPlay.setEnabled(false);
      this.rdoPlayBlack.setEnabled(false);
      this.rdoPlayWhite.setEnabled(false);
      this.txtFirstVisits.setEnabled(false);
      this.txtTotalTime.setEnabled(false);
      this.txtTotalVisits.setEnabled(false);
    }
    this.chkBothSync.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            if (chkBothSync.isSelected()) {
              chkAutoPlay.setEnabled(true);
              Utils.send("bothSync");
            } else {
              Utils.send("nobothSync");
              rdoGroup.remove(rdoPlayBlack);
              rdoGroup.remove(rdoPlayWhite);
              chkAutoPlay.setEnabled(false);
              rdoPlayBlack.setSelected(false);
              rdoPlayWhite.setSelected(false);
              chkAutoPlay.setSelected(false);
              rdoPlayBlack.setEnabled(false);
              rdoPlayWhite.setEnabled(false);
              txtFirstVisits.setEnabled(false);
              txtTotalTime.setEnabled(false);
              txtTotalVisits.setEnabled(false);
              rdoGroup.add(rdoPlayBlack);
              rdoGroup.add(rdoPlayWhite);
            }
          }
        });
    this.chkAutoPlay.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            if (chkAutoPlay.isSelected()) {
              rdoPlayBlack.setEnabled(true);
              rdoPlayWhite.setEnabled(true);
              txtFirstVisits.setEnabled(true);
              txtTotalTime.setEnabled(true);
              txtTotalVisits.setEnabled(true);
            } else {
              rdoGroup.remove(rdoPlayBlack);
              rdoGroup.remove(rdoPlayWhite);
              rdoPlayBlack.setSelected(false);
              rdoPlayWhite.setSelected(false);
              rdoPlayBlack.setEnabled(false);
              rdoPlayWhite.setEnabled(false);
              txtFirstVisits.setEnabled(false);
              txtTotalTime.setEnabled(false);
              txtTotalVisits.setEnabled(false);
              rdoGroup.add(rdoPlayBlack);
              rdoGroup.add(rdoPlayWhite);
            }
          }
        });

    this.txtBoardHeight.setDocument(new IntDocument());
    this.txtBoardWidth.setDocument(new IntDocument());
    this.txtFirstVisits.setDocument(new IntDocument());
    this.txtTotalTime.setDocument(new IntDocument());
    this.txtTotalVisits.setDocument(new IntDocument());
    Document dtTxtBoardHeight = txtBoardHeight.getDocument();
    dtTxtBoardHeight.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            updateBoardHeight();
          }

          public void removeUpdate(DocumentEvent e) {
            updateBoardHeight();
          }

          public void changedUpdate(DocumentEvent e) {
            updateBoardHeight();
          }
        });

    Document dtTxtBoardWidth = txtBoardWidth.getDocument();
    dtTxtBoardWidth.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            updateBoardWidth();
          }

          public void removeUpdate(DocumentEvent e) {
            updateBoardWidth();
          }

          public void changedUpdate(DocumentEvent e) {
            updateBoardWidth();
          }
        });

    Document dtTxtFirstVisits = txtFirstVisits.getDocument();
    dtTxtFirstVisits.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }

          public void removeUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }

          public void changedUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }
        });
    Document dtTxtTotalTime = txtTotalTime.getDocument();
    dtTxtTotalTime.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }

          public void removeUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }

          public void changedUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }
        });
    Document DTtxtTotalVisits = txtTotalVisits.getDocument();
    DTtxtTotalVisits.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }

          public void removeUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }

          public void changedUpdate(DocumentEvent e) {
            sendAutoPlayInfo();
          }
        });
    this.rdoPlayBlack.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            sendAutoPlayInfo();
          }
        });
    this.rdoPlayWhite.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            sendAutoPlayInfo();
          }
        });
    initComponents();
    this.setResizable(false);
    this.setAlwaysOnTop(true);
    this.setLocation(BoardSyncTool.config.locationX, BoardSyncTool.config.locationY);
    this.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            try {
              BoardSyncTool.config.saveAndWriteConfig();
            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
            System.exit(0);
          }
        });
  }

  private void updateBoardHeight() {
    // TODO Auto-generated method stub
    BoardSyncTool.boardHeight = Integer.parseInt(txtBoardHeight.getText());
  }

  private void updateBoardWidth() {
    // TODO Auto-generated method stub
    BoardSyncTool.boardWidth = Integer.parseInt(txtBoardWidth.getText());
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
    dialogPane.setBorder(new EmptyBorder(3, 5, 3, 5));
    dialogPane.setLayout(new BorderLayout());

    initNorth();
    initCenter();
    initSouth();

    contentPane.add(dialogPane, BorderLayout.CENTER);
  }

  private void initSouth() {
    dialogPane.add(southPane, BorderLayout.SOUTH);
    southPane.setBorder(new EmptyBorder(3, 3, 3, 3));
    southPane.setLayout(new GridLayout(1, 3, 0, 0));
    btnToggleAnalyze.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Utils.send("noponder");
          }
        });
    southPane.add(btnToggleAnalyze);
    btnPass.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Utils.send("pass");
          }
        });
    southPane.add(btnPass);
    btnClear.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Utils.send("clear");
          }
        });
    southPane.add(btnClear);
  }

  private void initNorth() {
    dialogPane.add(northPane, BorderLayout.NORTH);
    northPane.setBorder(new EmptyBorder(3, 3, 3, 3));
    northPane.setLayout(new GridLayout(2, 4, 0, 0));
    btnSelectBoard.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            SwingUtilities.invokeLater(
                new Runnable() {
                  public void run() {
                    selectBoard(false);
                  }
                });
          }
        });
    northPane.add(btnSelectBoard);
    btnSelectRow1.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            SwingUtilities.invokeLater(
                new Runnable() {
                  public void run() {
                    selectBoard(true);
                  }
                });
          }
        });
    northPane.add(btnSelectRow1);
    northPane.add(btnHelp);
    btnKeepSync.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (isKeepSyncing) stopKeepSync();
            else startKeepSync();
          }
        });
    northPane.add(btnKeepSync);
    btnOneTimeSync.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            BoardOCR boardOCR = new BoardOCR();
            try {
              setExtendedState(JFrame.ICONIFIED);
              if (BoardSyncTool.boardPosition == null) {
                Utils.showMssage(BoardSyncTool.toolFrame, "未选择棋盘", "消息提醒");
                return;
              }
              Utils.send(
                  "start " + BoardSyncTool.boardWidth + " " + BoardSyncTool.boardHeight + " ");
              boardOCR.oneTimeSync();
              setExtendedState(JFrame.NORMAL);
            } catch (AWTException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        });
    northPane.add(btnOneTimeSync);
    northPane.add(btnSettings);
  }

  private void initCenter() {

    GridBagLayout gbl_buttonBar = new GridBagLayout();
    //  gbl_buttonBar.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0};
    centerPane.setLayout(gbl_buttonBar);
    // buttonBar.setBorder(new EmptyBorder(12, 12, 12, 12));

    centerPane.setBorder(BorderFactory.createEtchedBorder());
    dialogPane.add(centerPane, BorderLayout.CENTER);

    GridBagConstraints gbc_chkBothSync = new GridBagConstraints();
    gbc_chkBothSync.anchor = GridBagConstraints.EAST;
    gbc_chkBothSync.insets = new Insets(2, 0, 5, 5);
    gbc_chkBothSync.gridx = 0;
    gbc_chkBothSync.gridy = 0;
    centerPane.add(chkBothSync, gbc_chkBothSync);

    GridBagConstraints gbc_rdoPlayBlack = new GridBagConstraints();
    gbc_rdoPlayBlack.fill = GridBagConstraints.HORIZONTAL;
    gbc_rdoPlayBlack.insets = new Insets(2, 0, 5, 5);
    gbc_rdoPlayBlack.gridx = 1;
    gbc_rdoPlayBlack.gridy = 0;
    centerPane.add(rdoPlayBlack, gbc_rdoPlayBlack);

    GridBagConstraints gbc_lblTotalTime = new GridBagConstraints();
    gbc_lblTotalTime.anchor = GridBagConstraints.EAST;
    gbc_lblTotalTime.insets = new Insets(2, 0, 5, 5);
    gbc_lblTotalTime.gridx = 2;
    gbc_lblTotalTime.gridy = 0;
    centerPane.add(lblTotalTime, gbc_lblTotalTime);

    GridBagConstraints gbc_txtTotalTime = new GridBagConstraints();
    gbc_txtTotalTime.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtTotalTime.insets = new Insets(2, 0, 5, 5);
    gbc_txtTotalTime.gridx = 3;
    gbc_txtTotalTime.gridy = 0;
    centerPane.add(txtTotalTime, gbc_txtTotalTime);
    GridBagConstraints gbc_lblBoard = new GridBagConstraints();
    gbc_lblBoard.anchor = GridBagConstraints.EAST;
    gbc_lblBoard.insets = new Insets(0, 0, 5, 5);
    gbc_lblBoard.gridx = 4;
    gbc_lblBoard.gridy = 0;
    centerPane.add(lblBoard, gbc_lblBoard);
    GridBagConstraints gbc_panel = new GridBagConstraints();
    gbc_panel.fill = GridBagConstraints.HORIZONTAL;
    gbc_panel.insets = new Insets(0, 0, 5, 5);
    gbc_panel.gridx = 5;
    gbc_panel.gridy = 0;
    boardPane.add(txtBoardWidth);
    boardPane.add(lblTimes);
    boardPane.add(txtBoardHeight);
    centerPane.add(boardPane, gbc_panel);
    boardPane.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));

    GridBagConstraints gbc_chkAutoPlay = new GridBagConstraints();
    gbc_chkAutoPlay.anchor = GridBagConstraints.EAST;
    gbc_chkAutoPlay.insets = new Insets(0, 0, 2, 5);
    gbc_chkAutoPlay.gridx = 0;
    gbc_chkAutoPlay.gridy = 1;
    centerPane.add(chkAutoPlay, gbc_chkAutoPlay);

    GridBagConstraints gbc_rdoPlayWhite = new GridBagConstraints();
    gbc_rdoPlayWhite.fill = GridBagConstraints.HORIZONTAL;
    gbc_rdoPlayWhite.insets = new Insets(0, 0, 2, 5);
    gbc_rdoPlayWhite.gridx = 1;
    gbc_rdoPlayWhite.gridy = 1;
    centerPane.add(rdoPlayWhite, gbc_rdoPlayWhite);

    GridBagConstraints gbc_lblTotalVisits = new GridBagConstraints();
    gbc_lblTotalVisits.anchor = GridBagConstraints.EAST;
    gbc_lblTotalVisits.insets = new Insets(0, 0, 2, 5);
    gbc_lblTotalVisits.gridx = 2;
    gbc_lblTotalVisits.gridy = 1;
    centerPane.add(lblTotalVisits, gbc_lblTotalVisits);

    GridBagConstraints gbc_txtTotalVisits = new GridBagConstraints();
    gbc_txtTotalVisits.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtTotalVisits.insets = new Insets(0, 0, 2, 5);
    gbc_txtTotalVisits.gridx = 3;
    gbc_txtTotalVisits.gridy = 1;
    centerPane.add(txtTotalVisits, gbc_txtTotalVisits);

    GridBagConstraints gbc_lblFirstVisits = new GridBagConstraints();
    gbc_lblFirstVisits.insets = new Insets(0, 0, 0, 5);
    gbc_lblFirstVisits.anchor = GridBagConstraints.EAST;
    gbc_lblFirstVisits.gridx = 4;
    gbc_lblFirstVisits.gridy = 1;
    centerPane.add(lblFirstVisits, gbc_lblFirstVisits);

    GridBagConstraints gbc_txtFirstVisits = new GridBagConstraints();
    gbc_txtFirstVisits.insets = new Insets(0, 0, 0, 5);
    gbc_txtFirstVisits.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtFirstVisits.gridx = 5;
    gbc_txtFirstVisits.gridy = 1;
    centerPane.add(txtFirstVisits, gbc_txtFirstVisits);
  }

  private void sendAutoPlayInfo() {
    if (isKeepSyncing && !keepSyncThreadInterrupted)
      if (this.rdoPlayBlack.isSelected() || this.rdoPlayWhite.isSelected())
        Utils.send(
            "play>"
                + (this.rdoPlayBlack.isSelected() ? "black" : "white")
                + ">"
                + (this.txtTotalTime.getText().trim().equals("")
                    ? "0"
                    : this.txtTotalTime.getText().trim())
                + " "
                + (this.txtTotalVisits.getText().trim().equals("")
                    ? "0"
                    : this.txtTotalVisits.getText().trim())
                + " "
                + (this.txtFirstVisits.getText().trim().equals("")
                    ? "0"
                    : this.txtFirstVisits.getText().trim()));
  }

  private void setKeepSyncStatus(boolean isSyncing) {
    if (isSyncing) {
      if (BoardSyncTool.config.autoMinimize) setExtendedState(JFrame.ICONIFIED);
      this.btnKeepSync.setText("停止同步");
      this.btnSelectBoard.setEnabled(false);
      this.btnSelectRow1.setEnabled(false);
      Utils.send("sync");
      Utils.send("start " + BoardSyncTool.boardWidth + " " + BoardSyncTool.boardHeight + " ");
      sendAutoPlayInfo();
    } else {
      this.btnKeepSync.setText("持续同步(" + BoardSyncTool.config.keepSyncIntervalMillseconds + "ms)");
      this.btnSelectBoard.setEnabled(true);
      this.btnSelectRow1.setEnabled(true);
      Utils.send("stopsync");
    }
    btnKeepSync.setEnabled(true);
    btnKeepSync.requestFocus();
  }

  private void stopKeepSync() {
    btnKeepSync.setEnabled(false);
    keepSyncThreadInterrupted = true;
  }

  private void startKeepSync() {
    if (isKeepSyncing) {
      Utils.showMssage(thisFrame, "正在持续同步中,请先停止", "消息提醒");
      return;
    }
    if (BoardSyncTool.boardPosition == null) {
      Utils.showMssage(BoardSyncTool.toolFrame, "未选择棋盘", "消息提醒");
      return;
    }
    isKeepSyncing = true;
    setKeepSyncStatus(true);
    new Thread() {
      public void run() {
        BoardOCR boardOCR = new BoardOCR();
        while (!keepSyncThreadInterrupted) {
          try {
            boardOCR.oneTimeSync();
          } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          try {
            Thread.sleep(BoardSyncTool.config.keepSyncIntervalMillseconds);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        isKeepSyncing = false;
        keepSyncThreadInterrupted = false;
        setKeepSyncStatus(false);
      }
    }.start();
  }

  private void selectBoard(boolean selectRow1) {
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
          if (selectRow1) {
            int vGap =
                Math.round(BoardSyncTool.screenImage.getWidth() / (float) BoardSyncTool.boardWidth);
            int hGap =
                Math.round(
                    BoardSyncTool.screenImage.getHeight() / (float) BoardSyncTool.boardHeight);
            BoardPosition position = new BoardPosition();
            position.x = BoardSyncTool.screenImageStartX - (vGap + 1) / 2;
            position.y = BoardSyncTool.screenImageStartY - (hGap + 1) / 2;
            position.width = BoardSyncTool.screenImage.getWidth() + ((vGap + 1) / 2) * 2;
            position.height = BoardSyncTool.screenImage.getHeight() + ((hGap + 1) / 2) * 2;
            BoardSyncTool.boardPosition = position;
          } else {
            LineDetection lineDetection = new LineDetection();
            BoardSyncTool.boardPosition = lineDetection.getBoardPosition(BoardSyncTool.screenImage);
          }
        }
      }
    }.start();
  }
}
