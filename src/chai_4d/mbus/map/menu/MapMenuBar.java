package chai_4d.mbus.map.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import chai_4d.mbus.map.constant.MapConstants.LineType;
import chai_4d.mbus.map.constant.MapConstants.MapMode;
import chai_4d.mbus.map.constant.MapConstants.ViewBusLine;
import chai_4d.mbus.map.constant.MapConstants.ViewType;
import chai_4d.mbus.map.screen.HelpDialog;
import chai_4d.mbus.map.screen.MainFrame;

public class MapMenuBar extends JMenuBar implements ActionListener
{
    private static final long serialVersionUID = -3467492107803276939L;

    private MainFrame mainFrame = null;

    private JMenu menu_program = null;
    private JMenu menu_edit = null;
    private JMenu menu_view = null;
    private JMenu menu_help = null;

    private JMenuItem save = null;
    private JMenuItem reset = null;
    private JMenuItem calculate = null;
    private JMenuItem exit = null;

    private JMenuItem addLine = null;
    private JMenuItem editLine = null;
    private JMenuItem deleteLine = null;
    private JRadioButtonMenuItem lineType1 = null;
    private JRadioButtonMenuItem lineType2 = null;
    private JRadioButtonMenuItem lineType3 = null;
    private JMenuItem editPoint = null;

    private JCheckBoxMenuItem viewPointType1 = null;
    private JCheckBoxMenuItem viewPointType2 = null;
    private JRadioButtonMenuItem viewSelected = null;
    private JRadioButtonMenuItem viewNone = null;
    private JCheckBoxMenuItem viewLineType1 = null;
    private JCheckBoxMenuItem viewLineType2 = null;
    private JCheckBoxMenuItem viewBusNo = null;

    private JMenuItem about = null;

    public MapMenuBar(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        initialize();
    }

    private JMenuItem createMenu(String name, int itemNo)
    {
        JMenuItem menu = new JMenuItem(name);
        menu.addActionListener(this);
        menu.putClientProperty("itemNo", new Integer(itemNo));
        return menu;
    }

    private JRadioButtonMenuItem createRadioMenu(String name, int itemNo)
    {
        JRadioButtonMenuItem menu = new JRadioButtonMenuItem(name);
        menu.addActionListener(this);
        menu.putClientProperty("itemNo", new Integer(itemNo));
        return menu;
    }

    private JCheckBoxMenuItem createCheckMenu(String name, int itemNo)
    {
        JCheckBoxMenuItem menu = new JCheckBoxMenuItem(name);
        menu.addActionListener(this);
        menu.putClientProperty("itemNo", new Integer(itemNo));
        return menu;
    }

    private void initialize()
    {
        menu_program = new JMenu("Program");
        menu_edit = new JMenu("Edit");
        menu_view = new JMenu("View");
        menu_help = new JMenu("Information");

        save = createMenu("Save", 11);
        reset = createMenu("Reset", 12);
        calculate = createMenu("Calculate Data", 13);
        exit = createMenu("Exit", 0);
        menu_program.add(save);
        menu_program.add(reset);
        menu_program.addSeparator();
        menu_program.add(calculate);
        menu_program.addSeparator();
        menu_program.add(exit);

        addLine = createMenu("Add Bus Line", 21);
        editLine = createMenu("Edit Bus Line", 22);
        deleteLine = createMenu("Delete Bus Line", 23);
        lineType1 = createRadioMenu("Use Bi-Direct Line", 24);
        lineType2 = createRadioMenu("Use P1-to-P2 Line", 25);
        lineType3 = createRadioMenu("Use P2-to-P1 Line", 26);
        editPoint = createMenu("Edit Point", 27);
        ButtonGroup groupLineType = new ButtonGroup();
        groupLineType.add(lineType1);
        groupLineType.add(lineType2);
        groupLineType.add(lineType3);
        menu_edit.add(addLine);
        menu_edit.add(editLine);
        menu_edit.add(deleteLine);
        menu_edit.addSeparator();
        menu_edit.add(lineType1);
        menu_edit.add(lineType2);
        menu_edit.add(lineType3);
        menu_edit.addSeparator();
        menu_edit.add(editPoint);

        viewPointType1 = createCheckMenu("View Point Link", 31);
        viewPointType2 = createCheckMenu("View Point Name", 32);
        viewSelected = createRadioMenu("View Selected Bus Line", 33);
        viewNone = createRadioMenu("View None", 34);
        viewLineType1 = createCheckMenu("View Two-Way Line", 35);
        viewLineType2 = createCheckMenu("View One-Way Line", 36);
        viewBusNo = createCheckMenu("View Bus No", 37);
        ButtonGroup groupBusLine = new ButtonGroup();
        groupBusLine.add(viewSelected);
        groupBusLine.add(viewNone);
        menu_view.add(viewPointType1);
        menu_view.add(viewPointType2);
        menu_view.addSeparator();
        menu_view.add(viewSelected);
        menu_view.add(viewNone);
        menu_view.addSeparator();
        menu_view.add(viewLineType1);
        menu_view.add(viewLineType2);
        menu_view.addSeparator();
        menu_view.add(viewBusNo);

        about = createMenu("About m-Bus Map Screen", 41);
        menu_help.add(about);

        add(menu_program);
        add(menu_edit);
        add(menu_view);
        add(menu_help);

        setActiveMenuItem();
        setActiveLineType();
        setActiveViewType();
    }

    public void setActiveMenuItem()
    {
        MapMode mode = mainFrame.getMapMode();
        switch (mode)
        {
            case VIEW:
                save.setEnabled(false);
                reset.setEnabled(false);
                calculate.setEnabled(true);

                addLine.setEnabled(true);
                editLine.setEnabled(true);
                deleteLine.setEnabled(true);
                lineType1.setEnabled(false);
                lineType2.setEnabled(false);
                lineType3.setEnabled(false);
                editPoint.setEnabled(true);

                viewPointType1.setEnabled(true);
                viewPointType2.setEnabled(true);
                viewSelected.setEnabled(true);
                viewNone.setEnabled(true);
                viewLineType1.setEnabled(true);
                viewLineType2.setEnabled(true);
                viewBusNo.setEnabled(true);
                break;
            case ADD_BUS:
            case EDIT_BUS:
            case DEL_BUS:
                save.setEnabled(true);
                reset.setEnabled(true);
                calculate.setEnabled(false);

                addLine.setEnabled(false);
                editLine.setEnabled(false);
                deleteLine.setEnabled(false);
                lineType1.setEnabled(true);
                lineType2.setEnabled(true);
                lineType3.setEnabled(true);
                editPoint.setEnabled(false);

                viewPointType1.setEnabled(false);
                viewPointType2.setEnabled(false);
                viewSelected.setEnabled(false);
                viewNone.setEnabled(false);
                viewLineType1.setEnabled(false);
                viewLineType2.setEnabled(false);
                viewBusNo.setEnabled(false);
                break;
            case EDIT_POINT:
                save.setEnabled(true);
                reset.setEnabled(true);
                calculate.setEnabled(false);

                addLine.setEnabled(false);
                editLine.setEnabled(false);
                deleteLine.setEnabled(false);
                lineType1.setEnabled(false);
                lineType2.setEnabled(false);
                lineType3.setEnabled(false);
                editPoint.setEnabled(false);

                viewPointType1.setEnabled(false);
                viewPointType2.setEnabled(false);
                viewSelected.setEnabled(false);
                viewNone.setEnabled(false);
                viewLineType1.setEnabled(false);
                viewLineType2.setEnabled(false);
                viewBusNo.setEnabled(false);
                break;
        }
    }

    private void setActiveLineType()
    {
        LineType type = mainFrame.getLineType();
        switch (type)
        {
            case BIDIRECT:
                lineType1.setSelected(true);
                break;
            case P1_P2:
                lineType2.setSelected(true);
                break;
            case P2_P1:
                lineType3.setSelected(true);
                break;
        }
    }

    public void setActiveViewType()
    {
        viewPointType1.setSelected(mainFrame.containsViewType(ViewType.VIEW_POINT_LINK));
        viewPointType2.setSelected(mainFrame.containsViewType(ViewType.VIEW_POINT_NAME));
        viewLineType1.setSelected(mainFrame.containsViewType(ViewType.VIEW_TWO_WAY));
        viewLineType2.setSelected(mainFrame.containsViewType(ViewType.VIEW_ONE_WAY));
        viewBusNo.setSelected(mainFrame.containsViewType(ViewType.VIEW_BUS_NO));

        ViewBusLine busLine = mainFrame.getViewBusLine();
        switch (busLine)
        {
            case VIEW_SELECTED:
                viewSelected.setSelected(true);
                break;
            case VIEW_NONE:
                viewNone.setSelected(true);
                break;
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        JMenuItem menu = (JMenuItem) e.getSource();
        int itemNo = ((Integer) menu.getClientProperty("itemNo")).intValue();
        switch (itemNo)
        {
            case 0: // Exit
                mainFrame.onExit();
                break;
            case 11: // Save
                mainFrame.onSave();
                break;
            case 12: // Reset
                mainFrame.onReset();
                break;
            case 13: // Calculate Data
                // TODO
                break;
            case 21: // Add Bus Line
                mainFrame.onAddBusLine();
                break;
            case 22: // Edit Bus Line
                mainFrame.onEditBusLine();
                break;
            case 23: // Delete Bus Line
                mainFrame.onDeleteBusLine();
                break;
            case 24: // Use Bi-Direct Line
                mainFrame.setLineType(LineType.BIDIRECT);
                break;
            case 25: // Use P1-to-P2 Line
                mainFrame.setLineType(LineType.P1_P2);
                break;
            case 26: // Use P2-to-P1 Line
                mainFrame.setLineType(LineType.P2_P1);
                break;
            case 27: // Edit Point
                mainFrame.onEditPoint();
                break;
            case 31: // View Point Link
                if (viewPointType1.isSelected())
                {
                    mainFrame.addViewType(ViewType.VIEW_POINT_LINK);
                }
                else
                {
                    mainFrame.removeViewType(ViewType.VIEW_POINT_LINK);
                }
                break;
            case 32: // View Point Name
                if (viewPointType2.isSelected())
                {
                    mainFrame.addViewType(ViewType.VIEW_POINT_NAME);
                }
                else
                {
                    mainFrame.removeViewType(ViewType.VIEW_POINT_NAME);
                }
                break;
            case 33: // View Selected Bus Line
                mainFrame.setViewBusLine(ViewBusLine.VIEW_SELECTED);
                break;
            case 34: // View None
                mainFrame.setViewBusLine(ViewBusLine.VIEW_NONE);
                break;
            case 35: // View Two-Way Line
                if (viewLineType1.isSelected())
                {
                    mainFrame.addViewType(ViewType.VIEW_TWO_WAY);
                }
                else
                {
                    mainFrame.removeViewType(ViewType.VIEW_TWO_WAY);
                }
                break;
            case 36: // View One-Way Line
                if (viewLineType2.isSelected())
                {
                    mainFrame.addViewType(ViewType.VIEW_ONE_WAY);
                }
                else
                {
                    mainFrame.removeViewType(ViewType.VIEW_ONE_WAY);
                }
                break;
            case 37: // View Bus No
                if (viewBusNo.isSelected())
                {
                    mainFrame.addViewType(ViewType.VIEW_BUS_NO);
                }
                else
                {
                    mainFrame.removeViewType(ViewType.VIEW_BUS_NO);
                }
                break;
            case 41: // About m-Bus Map Screen
                HelpDialog dialog = new HelpDialog(mainFrame, "About m-Bus Map Screen");
                dialog.setVisible(true);
                break;
        }
    }
}
