package chai_4d.mbus.map.screen;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.constant.MapConstants.MapMode;
import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.constant.MapConstants.PointType;
import chai_4d.mbus.map.model.NamedPointModel;
import chai_4d.mbus.map.model.PointInfo;
import chai_4d.mbus.map.model.PointName;
import chai_4d.mbus.map.util.StringUtil;
import chai_4d.mbus.map.util.SwingUtil;

public class PointPanel extends FormPanel implements ActionListener, KeyListener
{
    private static final long serialVersionUID = 6354826348160944834L;

    private PointInfo pointInfo = null;

    private JLabel lblId = new JLabel("ID");
    private JTextField txtId = new JTextField();

    private JLabel lblXY = new JLabel("X : Y");
    private JTextField txtXY = new JTextField();

    private JLabel lblType = new JLabel("Type");
    private JRadioButton rdoTypeLink = new JRadioButton("Link");
    private JRadioButton rdoTypeName = new JRadioButton("Name");

    private JLabel lblNameTh = new JLabel("Name (TH)");
    private JTextField txtNameTh = new JTextField();

    private JLabel lblNameEn = new JLabel("Name (EN)");
    private JTextField txtNameEn = new JTextField();
    private JButton butAddName = SwingUtil.createImageButton("/icons/but_add.gif");

    private JTable tblName = new JTable(new NamedPointModel());
    private JButton butDelName = SwingUtil.createImageButton("/icons/but_remove.gif");

    public PointPanel(MainFrame mainFrame)
    {
        super(mainFrame);
        initialize();
        setPointInfo(null);
    }

    private void initialize()
    {
        setLayout(new GridBagLayout());
        setBackground(MapConstants.controlPanel);

        add(lblId, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(
            txtId,
            new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        add(lblXY, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(
            txtXY,
            new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        ButtonGroup groupPointType = new ButtonGroup();
        groupPointType.add(rdoTypeLink);
        groupPointType.add(rdoTypeName);

        add(lblType, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        JPanel pnlRdoType = new JPanel(new GridBagLayout());
        pnlRdoType.setBackground(MapConstants.controlPanel);
        rdoTypeLink.setBackground(MapConstants.controlPanel);
        rdoTypeName.setBackground(MapConstants.controlPanel);
        pnlRdoType.add(
            rdoTypeLink,
            new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        pnlRdoType.add(
            rdoTypeName,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        add(
            pnlRdoType,
            new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        add(lblNameTh, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(
            txtNameTh,
            new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        add(lblNameEn, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(
            txtNameEn,
            new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        add(butAddName, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        JScrollPane sclName = new JScrollPane(tblName);
        sclName.setSize(new Dimension(100, 80));
        sclName.setPreferredSize(new Dimension(100, 80));
        sclName.setMinimumSize(new Dimension(100, 80));
        tblName.setPreferredScrollableViewportSize(new Dimension(100, 80));
        tblName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //tblName.setFillsViewportHeight(true);
        //tblName.setAutoCreateRowSorter(true);
        add(
            sclName,
            new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        add(
            butDelName,
            new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        txtId.setEnabled(false);
        txtXY.setEnabled(false);
        rdoTypeLink.setEnabled(false);
        rdoTypeName.setEnabled(false);

        txtNameTh.addKeyListener(this);
        txtNameEn.addKeyListener(this);
        butAddName.addActionListener(this);
        butDelName.addActionListener(this);
    }

    private boolean isDupPointName(int colType, String value)
    {
        List<PointName> list = pointInfo.getPointName();
        for (int i = 0; i < list.size(); i++)
        {
            PointName pointName = list.get(i);
            if (pointName.getMode() != Mode.DELETE)
            {
                if (colType == 1 && pointName.getNameTh().equals(value))
                {
                    return true;
                }
                else if (colType == 2 && pointName.getNameEn().equals(value))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(butAddName))
        {
            if (StringUtil.isEmpty(txtNameTh.getText()))
            {
                SwingUtil.alertWarning("Please enter Name (TH).");
                txtNameTh.requestFocus();
                return;
            }
            else if (StringUtil.isEmpty(txtNameEn.getText()))
            {
                SwingUtil.alertWarning("Please enter Name (EN).");
                txtNameEn.requestFocus();
                return;
            }
            else if (isDupPointName(1, txtNameTh.getText()))
            {
                SwingUtil.alertWarning("Name (TH) is already exist.");
                txtNameTh.requestFocus();
                return;
            }
            else if (isDupPointName(2, txtNameEn.getText()))
            {
                SwingUtil.alertWarning("Name (EN) is already exist.");
                txtNameEn.requestFocus();
                return;
            }

            pointInfo.getPointName().add(new PointName(pointInfo.getPId(), txtNameTh.getText(), txtNameEn.getText()));
            pointInfo.setType(PointType.NAME);
            pointInfo.setEdited();
            setActivePanel();
            mainFrame.getMapPanel().repaint();
        }
        else if (e.getSource().equals(butDelName))
        {
            int row = tblName.getSelectedRow();
            if (row >= 0)
            {
                NamedPointModel model = (NamedPointModel) tblName.getModel();
                PointName pointName = model.getRow(row);
                if (pointName.getMode() == Mode.INSERT)
                {
                    pointInfo.getPointName().remove(pointName);
                }
                else
                {
                    pointName.setMode(Mode.DELETE);
                }
                pointInfo.setType(model.getRowCount() == 0 ? PointType.LINK : PointType.NAME);
                pointInfo.setEdited();
                setActivePanel();
                mainFrame.getMapPanel().repaint();
            }
            else
            {
                SwingUtil.alertWarning("Please select a row before remove.");
            }
        }
    }

    public void keyPressed(KeyEvent e)
    {
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
    }

    private void setActivePanel()
    {
        if (this.pointInfo == null)
        {
            txtId.setText("");
            txtXY.setText("");
            rdoTypeLink.setSelected(true);
            rdoTypeName.setSelected(false);
            txtNameTh.setText("");
            txtNameEn.setText("");
            ((NamedPointModel) tblName.getModel()).setData(null);
        }
        else
        {
            txtId.setText(pointInfo.getPId() + "");
            txtXY.setText(StringUtil.toNumString(pointInfo.getAxisX()) + " : " + StringUtil.toNumString(pointInfo.getAxisY()));
            rdoTypeLink.setSelected(pointInfo.getType() == PointType.LINK);
            rdoTypeName.setSelected(pointInfo.getType() == PointType.NAME);
            txtNameTh.setText("");
            txtNameEn.setText("");
            ((NamedPointModel) tblName.getModel()).setData(pointInfo.getPointName());
        }

        if (this.pointInfo == null || mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
        {
            txtNameTh.setEnabled(false);
            txtNameEn.setEnabled(false);
            butAddName.setEnabled(false);
            butDelName.setEnabled(false);
        }
        else
        {
            txtNameTh.setEnabled(true);
            txtNameEn.setEnabled(true);
            butAddName.setEnabled(true);
            butDelName.setEnabled(true);
        }
    }

    public boolean validateForm()
    {
        return true;
    }

    public PointInfo getPointInfo()
    {
        return pointInfo;
    }

    public void setPointInfo(PointInfo pointInfo)
    {
        this.pointInfo = pointInfo;
        setActivePanel();
    }
}
