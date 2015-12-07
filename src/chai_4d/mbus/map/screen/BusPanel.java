package chai_4d.mbus.map.screen;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.constant.MapConstants.MapMode;
import chai_4d.mbus.map.constant.MapConstants.ViewType;
import chai_4d.mbus.map.model.BusInfo;
import chai_4d.mbus.map.util.ImageUtil;
import chai_4d.mbus.map.util.StringUtil;
import chai_4d.mbus.map.util.SwingUtil;

public class BusPanel extends FormPanel implements ActionListener, KeyListener
{
    private static final long serialVersionUID = 7624694135806662143L;

    private BusInfo busInfo = null;

    private JLabel lblId = new JLabel("ID");
    private JTextField txtId = new JTextField();

    private JLabel lblBusNoTh = new JLabel("Bus No (TH)");
    private JTextField txtBusNoTh = new JTextField();

    private JLabel lblBusNoEn = new JLabel("Bus No (EN)");
    private JTextField txtBusNoEn = new JTextField();

    private JLabel lblDetailTh = new JLabel("Detail (TH)");
    private JTextArea txtDetailTh = new JTextArea(2, 1);

    private JLabel lblDetailEn = new JLabel("Detail (EN)");
    private JTextArea txtDetailEn = new JTextArea(2, 1);

    private JLabel lblPicture = new JLabel("Picture");
    private JTextField txtPicture = new JTextField();
    private JButton butPicture = SwingUtil.createImageButton("/icons/file_open.gif");
    private BusPreview pnlPicture = new BusPreview();
    private JFileChooser filePicture = null;

    private JPanel pnlTwoWay = new LineItem(ViewType.VIEW_TWO_WAY);
    private JLabel lblTwoWay = new JLabel("(0)");

    private JPanel pnlOneWay = new LineItem(ViewType.VIEW_ONE_WAY);
    private JLabel lblOneWay = new JLabel("(0)");

    public BusPanel(MainFrame mainFrame)
    {
        super(mainFrame);
        initialize();
        setBusInfo(null);
    }

    private void initialize()
    {
        setLayout(new GridBagLayout());
        setBackground(MapConstants.controlPanel);

        add(lblId, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(
            txtId,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        add(lblBusNoTh, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(
            txtBusNoTh,
            new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        add(lblBusNoEn, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(
            txtBusNoEn,
            new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        add(
            lblDetailTh,
            new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        JScrollPane sclDetailTh = new JScrollPane(txtDetailTh);
        sclDetailTh.setSize(new Dimension(100, 40));
        sclDetailTh.setPreferredSize(new Dimension(100, 40));
        sclDetailTh.setMinimumSize(new Dimension(100, 40));
        add(
            sclDetailTh,
            new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        add(
            lblDetailEn,
            new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        JScrollPane sclDetailEn = new JScrollPane(txtDetailEn);
        sclDetailEn.setSize(new Dimension(100, 40));
        sclDetailEn.setPreferredSize(new Dimension(100, 40));
        sclDetailEn.setMinimumSize(new Dimension(100, 40));
        add(
            sclDetailEn,
            new GridBagConstraints(0, 6, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        JPanel pnlTextPicture = new JPanel(new GridBagLayout());
        pnlTextPicture.setBackground(MapConstants.controlPanel);
        pnlTextPicture.add(
            lblPicture,
            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        pnlTextPicture.add(
            txtPicture,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        pnlTextPicture.add(
            butPicture,
            new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(
            pnlTextPicture,
            new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        add(
            pnlPicture,
            new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        JPanel pnlBusLine = new JPanel(new GridBagLayout());
        pnlBusLine.setBackground(MapConstants.controlPanel);
        pnlBusLine.add(
            pnlTwoWay,
            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        pnlBusLine.add(
            lblTwoWay,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 10, 0));
        pnlBusLine.add(
            pnlOneWay,
            new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        pnlBusLine.add(
            lblOneWay,
            new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 10, 0));
        add(
            pnlBusLine,
            new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        txtId.setEnabled(false);
        txtPicture.setEnabled(false);

        txtBusNoTh.addKeyListener(this);
        txtBusNoEn.addKeyListener(this);
        txtDetailTh.addKeyListener(this);
        txtDetailEn.addKeyListener(this);
        butPicture.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (filePicture == null)
        {
            filePicture = new JFileChooser();
            filePicture.setDialogType(JFileChooser.CUSTOM_DIALOG);
            filePicture.setDialogTitle("Open Picture File");

            filePicture
                .addChoosableFileFilter(new ImageFilter("JPEG File Interchange Format (*.jpg)", new String[] { ImageUtil.jpeg, ImageUtil.jpg }));
            filePicture.addChoosableFileFilter(new ImageFilter("PNG Portable Network Graphics Format (*.png)", ImageUtil.png));
            filePicture.addChoosableFileFilter(new ImageFilter("TIFF Tag Image File Format (*.tif)", new String[] { ImageUtil.tiff, ImageUtil.tif }));
            filePicture.addChoosableFileFilter(new ImageFilter("GIF Graphics Interchange Format (*.gif)", ImageUtil.gif));
            filePicture.addChoosableFileFilter(new ImageFilter("Windows Bitmap (*.bmp)", ImageUtil.bmp));
            filePicture.addChoosableFileFilter(
                new ImageFilter(
                    "All Picture Files",
                    new String[] { ImageUtil.bmp, ImageUtil.jpeg, ImageUtil.jpg, ImageUtil.gif, ImageUtil.tiff, ImageUtil.tif, ImageUtil.png }));
            filePicture.setAcceptAllFileFilterUsed(false);

            filePicture.setFileView(new ImageFileView());
            filePicture.setAccessory(new ImagePreview(filePicture));
        }

        int returnVal = filePicture.showDialog(mainFrame, "Open");
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = filePicture.getSelectedFile();
            busInfo.setBusPic(file.getPath());
            busInfo.setEdited();
            setActivePanel();
        }
        filePicture.setSelectedFile(null);
    }

    public void keyPressed(KeyEvent e)
    {
    }

    public void keyReleased(KeyEvent e)
    {
        if (e.getComponent().equals(txtBusNoTh))
        {
            busInfo.setBusNoTh(txtBusNoTh.getText());
            busInfo.setEdited();
        }
        else if (e.getComponent().equals(txtBusNoEn))
        {
            busInfo.setBusNoEn(txtBusNoEn.getText());
            busInfo.setEdited();
        }
        else if (e.getComponent().equals(txtDetailTh))
        {
            busInfo.setDetailTh(txtDetailTh.getText());
            busInfo.setEdited();
        }
        else if (e.getComponent().equals(txtDetailEn))
        {
            busInfo.setDetailEn(txtDetailEn.getText());
            busInfo.setEdited();
        }
    }

    public void keyTyped(KeyEvent e)
    {
    }

    private void setActivePanel()
    {
        if (this.busInfo == null)
        {
            txtId.setText("");
            txtBusNoTh.setText("");
            txtBusNoEn.setText("");
            txtDetailTh.setText("");
            txtDetailEn.setText("");
            txtPicture.setText("");
            pnlPicture.setFile(null);
            lblTwoWay.setText("(0)");
            lblOneWay.setText("(0)");
        }
        else
        {
            txtId.setText(busInfo.getBusId() + "");
            txtBusNoTh.setText(busInfo.getBusNoTh());
            txtBusNoEn.setText(busInfo.getBusNoEn());
            txtDetailTh.setText(busInfo.getDetailTh());
            txtDetailEn.setText(busInfo.getDetailEn());
            txtPicture.setText(busInfo.getBusPic());
            pnlPicture.setFile(ImageUtil.getFile(busInfo.getBusPic()));
            lblTwoWay.setText("(" + StringUtil.toNumString(busInfo.getCountTwoWay()) + ")");
            lblOneWay.setText("(" + StringUtil.toNumString(busInfo.getCountOneWay()) + ")");
        }

        if (this.busInfo == null || mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
        {
            txtBusNoTh.setEnabled(false);
            txtBusNoEn.setEnabled(false);
            txtDetailTh.setEnabled(false);
            txtDetailEn.setEnabled(false);
            butPicture.setEnabled(false);
        }
        else
        {
            txtBusNoTh.setEnabled(true);
            txtBusNoEn.setEnabled(true);
            txtDetailTh.setEnabled(true);
            txtDetailEn.setEnabled(true);
            butPicture.setEnabled(true);
        }
    }

    public boolean validateForm()
    {
        if (StringUtil.isEmpty(txtBusNoTh.getText()))
        {
            SwingUtil.alertWarning("Please enter Bus No (TH).");
            txtBusNoTh.requestFocus();
            return false;
        }
        else if (StringUtil.isEmpty(txtBusNoEn.getText()))
        {
            SwingUtil.alertWarning("Please enter Bus No (EN).");
            txtBusNoEn.requestFocus();
            return false;
        }
        return true;
    }

    public BusInfo getBusInfo()
    {
        return busInfo;
    }

    public void setBusInfo(BusInfo busInfo)
    {
        this.busInfo = busInfo;
        setActivePanel();
    }
}
