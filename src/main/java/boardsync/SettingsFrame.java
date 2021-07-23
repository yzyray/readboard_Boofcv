package boardsync;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SettingsFrame extends JDialog {
  /** */
  private static final long serialVersionUID = 3L;

  private JTextField txtBlackOffset;
  private JTextField txtBlackPercent;
  private JTextField txtWhiteOffset;
  private JTextField txtWhitePercent;
  private JTextField txtSyncInterval;
  private JTextField txtGrayOffset;
  private JCheckBox chkAutoMin;
  private JCheckBox chkVerifyPlacedMove;
  private JPanel contentPane = new JPanel();
  private JPanel southPane = new JPanel();
  private JPanel buttonPane = new JPanel();
  private JPanel noticePane = new JPanel();

  public SettingsFrame(Window owner) {
    super(owner);
    setResizable(false);
    setTitle(BoardSyncTool.resourceBundle.getString("SettingsFrame.title"));
    try {
      setIconImage(ImageIO.read(ToolFrame.class.getResourceAsStream("/assets/logo.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(contentPane, BorderLayout.CENTER);
    getContentPane().add(southPane, BorderLayout.SOUTH);

    GridBagLayout gridBagLayout = new GridBagLayout();
    contentPane.setLayout(gridBagLayout);
    contentPane.setBorder(new EmptyBorder(12, 12, 0, 12));
    noticePane.setBorder(new EmptyBorder(6, 12, 3, 12));
    southPane.setBorder(new EmptyBorder(0, 0, 3, 0));
    JLabel lblBlackOffset =
        new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.lblBlackOffset"));
    GridBagConstraints gbc_lblBlackOffset = new GridBagConstraints();
    gbc_lblBlackOffset.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblBlackOffset.insets = new Insets(0, 0, 5, 5);
    gbc_lblBlackOffset.gridx = 0;
    gbc_lblBlackOffset.gridy = 0;
    contentPane.add(lblBlackOffset, gbc_lblBlackOffset);

    txtBlackOffset = new JTextField();
    GridBagConstraints gbc_txtBlackOffset = new GridBagConstraints();
    gbc_txtBlackOffset.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtBlackOffset.insets = new Insets(0, 0, 5, 5);
    gbc_txtBlackOffset.gridx = 1;
    gbc_txtBlackOffset.gridy = 0;
    contentPane.add(txtBlackOffset, gbc_txtBlackOffset);
    txtBlackOffset.setColumns(6);

    JLabel lblBlackPercent =
        new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.lblBlackPercent"));
    GridBagConstraints gbc_lblBlackPercent = new GridBagConstraints();
    gbc_lblBlackPercent.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblBlackPercent.insets = new Insets(0, 0, 5, 5);
    gbc_lblBlackPercent.gridx = 2;
    gbc_lblBlackPercent.gridy = 0;
    contentPane.add(lblBlackPercent, gbc_lblBlackPercent);

    txtBlackPercent = new JTextField();
    GridBagConstraints gbc_txtBlackPercent = new GridBagConstraints();
    gbc_txtBlackPercent.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtBlackPercent.insets = new Insets(0, 0, 5, 0);
    gbc_txtBlackPercent.gridx = 3;
    gbc_txtBlackPercent.gridy = 0;
    contentPane.add(txtBlackPercent, gbc_txtBlackPercent);
    txtBlackPercent.setColumns(6);

    JLabel lblWhiteOffset =
        new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.lblWhiteOffset"));
    GridBagConstraints gbc_lblWhiteOffset = new GridBagConstraints();
    gbc_lblWhiteOffset.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblWhiteOffset.insets = new Insets(0, 0, 5, 5);
    gbc_lblWhiteOffset.gridx = 0;
    gbc_lblWhiteOffset.gridy = 1;
    contentPane.add(lblWhiteOffset, gbc_lblWhiteOffset);

    txtWhiteOffset = new JTextField();
    GridBagConstraints gbc_txtWhiteOffset = new GridBagConstraints();
    gbc_txtWhiteOffset.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtWhiteOffset.insets = new Insets(0, 0, 5, 5);
    gbc_txtWhiteOffset.gridx = 1;
    gbc_txtWhiteOffset.gridy = 1;
    contentPane.add(txtWhiteOffset, gbc_txtWhiteOffset);
    txtWhiteOffset.setColumns(6);

    JLabel lblWhitePercent =
        new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.lblWhitePercent"));
    GridBagConstraints gbc_lblWhitePercent = new GridBagConstraints();
    gbc_lblWhitePercent.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblWhitePercent.insets = new Insets(0, 0, 5, 5);
    gbc_lblWhitePercent.gridx = 2;
    gbc_lblWhitePercent.gridy = 1;
    contentPane.add(lblWhitePercent, gbc_lblWhitePercent);

    txtWhitePercent = new JTextField();
    GridBagConstraints gbc_txtWhitePercent = new GridBagConstraints();
    gbc_txtWhitePercent.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtWhitePercent.insets = new Insets(0, 0, 5, 0);
    gbc_txtWhitePercent.gridx = 3;
    gbc_txtWhitePercent.gridy = 1;
    contentPane.add(txtWhitePercent, gbc_txtWhitePercent);
    txtWhitePercent.setColumns(6);

    JLabel lblSyncInterval =
        new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.lblSyncInterval"));
    GridBagConstraints gbc_lblSyncInterval = new GridBagConstraints();
    gbc_lblSyncInterval.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblSyncInterval.insets = new Insets(0, 0, 5, 5);
    gbc_lblSyncInterval.gridx = 0;
    gbc_lblSyncInterval.gridy = 2;
    contentPane.add(lblSyncInterval, gbc_lblSyncInterval);

    txtSyncInterval = new JTextField();
    GridBagConstraints gbc_txtSyncInterval = new GridBagConstraints();
    gbc_txtSyncInterval.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtSyncInterval.insets = new Insets(0, 0, 5, 5);
    gbc_txtSyncInterval.gridx = 1;
    gbc_txtSyncInterval.gridy = 2;
    contentPane.add(txtSyncInterval, gbc_txtSyncInterval);
    txtSyncInterval.setColumns(6);

    JLabel lblGrayOffset =
        new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.lblGrayOffset"));
    GridBagConstraints gbc_lblGrayOffset = new GridBagConstraints();
    gbc_lblGrayOffset.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblGrayOffset.insets = new Insets(0, 0, 5, 5);
    gbc_lblGrayOffset.gridx = 2;
    gbc_lblGrayOffset.gridy = 2;
    contentPane.add(lblGrayOffset, gbc_lblGrayOffset);

    txtGrayOffset = new JTextField();
    GridBagConstraints gbc_txtGrayOffset = new GridBagConstraints();
    gbc_txtGrayOffset.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtGrayOffset.insets = new Insets(0, 0, 5, 0);
    gbc_txtGrayOffset.gridx = 3;
    gbc_txtGrayOffset.gridy = 2;
    contentPane.add(txtGrayOffset, gbc_txtGrayOffset);
    txtGrayOffset.setColumns(6);

    JLabel lblAutoMin =
        new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.lblAutoMin"));
    GridBagConstraints gbc_lblAutoMin = new GridBagConstraints();
    gbc_lblAutoMin.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblAutoMin.insets = new Insets(0, 0, 0, 5);
    gbc_lblAutoMin.gridx = 0;
    gbc_lblAutoMin.gridy = 3;
    contentPane.add(lblAutoMin, gbc_lblAutoMin);

    chkAutoMin = new JCheckBox("");
    GridBagConstraints gbc_chkAutoMin = new GridBagConstraints();
    gbc_chkAutoMin.insets = new Insets(0, 0, 0, 5);
    gbc_chkAutoMin.gridx = 1;
    gbc_chkAutoMin.gridy = 3;
    contentPane.add(chkAutoMin, gbc_chkAutoMin);

    JLabel lblVerifyPlacedMove =
        new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.lblVerifyPlacedMove"));
    GridBagConstraints gbc_lblVerifyPlacedMove = new GridBagConstraints();
    gbc_lblVerifyPlacedMove.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblVerifyPlacedMove.insets = new Insets(0, 0, 0, 5);
    gbc_lblVerifyPlacedMove.gridx = 2;
    gbc_lblVerifyPlacedMove.gridy = 3;
    contentPane.add(lblVerifyPlacedMove, gbc_lblVerifyPlacedMove);

    chkVerifyPlacedMove = new JCheckBox("");
    GridBagConstraints gbc_chkVerifyPlacedMove = new GridBagConstraints();
    gbc_chkVerifyPlacedMove.gridx = 3;
    gbc_chkVerifyPlacedMove.gridy = 3;
    contentPane.add(chkVerifyPlacedMove, gbc_chkVerifyPlacedMove);

    southPane.setLayout(new BorderLayout());
    southPane.add(buttonPane, BorderLayout.CENTER);
    southPane.add(noticePane, BorderLayout.NORTH);
    noticePane.setLayout(new GridLayout(2, 1, 0, 0));
    noticePane.add(new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.notice1")));
    noticePane.add(new JLabel(BoardSyncTool.resourceBundle.getString("SettingsFrame.notice2")));

    JButton btnReset =
        new JButton(BoardSyncTool.resourceBundle.getString("SettingsFrame.btnReset"));
    btnReset.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            txtBlackOffset.setText("96");
            txtBlackPercent.setText("33");
            txtWhiteOffset.setText("96");
            txtWhitePercent.setText("33");
            txtGrayOffset.setText("50");
            txtSyncInterval.setText("200");
            chkAutoMin.setSelected(true);
            chkVerifyPlacedMove.setSelected(true);
          }
        });
    buttonPane.add(btnReset);

    JButton btnApply =
        new JButton(BoardSyncTool.resourceBundle.getString("SettingsFrame.btnApply"));
    btnApply.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            BoardSyncTool.config.blackOffset =
                Utils.parseTextToInt(txtBlackOffset, BoardSyncTool.config.blackOffset, 0, 255);
            BoardSyncTool.config.blackPercent =
                Utils.parseTextToInt(txtBlackPercent, BoardSyncTool.config.blackPercent, 1, 100);
            BoardSyncTool.config.whiteOffset =
                Utils.parseTextToInt(txtWhiteOffset, BoardSyncTool.config.whiteOffset, 0, 255);
            BoardSyncTool.config.whitePercent =
                Utils.parseTextToInt(txtWhitePercent, BoardSyncTool.config.whitePercent, 1, 100);
            BoardSyncTool.config.grayOffset =
                Utils.parseTextToInt(txtGrayOffset, BoardSyncTool.config.grayOffset, 0, 255);
            BoardSyncTool.config.keepSyncIntervalMillseconds =
                Utils.parseTextToInt(
                    txtSyncInterval, BoardSyncTool.config.keepSyncIntervalMillseconds);
            BoardSyncTool.config.autoMinimize = chkAutoMin.isSelected();
            BoardSyncTool.config.verifyPlacedMove = chkVerifyPlacedMove.isSelected();
            try {
              BoardSyncTool.config.saveAndWriteConfig();
            } catch (IOException e1) {
              e1.printStackTrace();
            }
            setVisible(false);
          }
        });
    buttonPane.add(btnApply);

    JButton btnCancel =
        new JButton(BoardSyncTool.resourceBundle.getString("SettingsFrame.btnCancel"));
    btnCancel.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            setVisible(false);
          }
        });
    buttonPane.add(btnCancel);
    txtBlackOffset.setDocument(new IntDocument());
    txtBlackPercent.setDocument(new IntDocument());
    txtWhiteOffset.setDocument(new IntDocument());
    txtWhitePercent.setDocument(new IntDocument());
    txtGrayOffset.setDocument(new IntDocument());
    txtSyncInterval.setDocument(new IntDocument());
    loadValue();
    pack();
    setLocationRelativeTo(getOwner());
  }

  private void loadValue() {
    txtBlackOffset.setText(BoardSyncTool.config.blackOffset + "");
    txtBlackPercent.setText(BoardSyncTool.config.blackPercent + "");
    txtWhiteOffset.setText(BoardSyncTool.config.whiteOffset + "");
    txtWhitePercent.setText(BoardSyncTool.config.whitePercent + "");
    txtGrayOffset.setText(BoardSyncTool.config.grayOffset + "");
    txtSyncInterval.setText(BoardSyncTool.config.keepSyncIntervalMillseconds + "");
    chkAutoMin.setSelected(BoardSyncTool.config.autoMinimize);
    chkVerifyPlacedMove.setSelected(BoardSyncTool.config.verifyPlacedMove);
  }
}
