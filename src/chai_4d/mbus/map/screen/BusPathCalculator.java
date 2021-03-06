package chai_4d.mbus.map.screen;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

import chai_4d.mbus.map.bean.MapDbBean;
import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.dijkstra.engine.DijkstraAlgorithm;
import chai_4d.mbus.map.dijkstra.model.Graph;
import chai_4d.mbus.map.dijkstra.model.Line;
import chai_4d.mbus.map.dijkstra.model.Point;
import chai_4d.mbus.map.util.StringUtil;

public class BusPathCalculator extends JDialog implements ActionListener, PropertyChangeListener
{
    private static final long serialVersionUID = 4774492044000418424L;

    private JButton butStart = new JButton("Start");
    private JProgressBar prgTaskProgress = new JProgressBar(0, 100);
    private JTextArea txtTaskOutput = new JTextArea(10, 30);
    private JButton butClose = new JButton("Close");
    private Task task;

    class Task extends SwingWorker<Void, Void>
    {
        public Void doInBackground()
        {
            setProgress(0);
            //MapDbBean.resetBusPath();

            Map<Integer, Point> points = MapDbBean.loadPointInfo();
            List<Line> lines = MapDbBean.loadBusLine(points);
            Graph graph = new Graph(points, lines);
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

            int total_source = points.size();
            txtTaskOutput.append("Total Source = " + StringUtil.toNumString(total_source) + "\n");

            int index_source = 0;
            for (Map.Entry source : points.entrySet())
            {
                Point sourcePoint = (Point) source.getValue();
                index_source++;
                int count_dest = MapDbBean.countBusPath(sourcePoint.getId());
                if (count_dest > 0)
                {
                    txtTaskOutput.append(StringUtil.toNumString(index_source) + ". " + sourcePoint + " : ");
                    for (int i = 0; i < 100; i++)
                    {
                        txtTaskOutput.append(".");
                    }
                }
                else
                {
                    dijkstra.execute(sourcePoint);
                    txtTaskOutput.append(StringUtil.toNumString(index_source) + ". " + sourcePoint + " : ");
                    int index_dest = 0;
                    for (Map.Entry destination : points.entrySet())
                    {
                        Point destinationPoint = (Point) destination.getValue();
                        if (StringUtil.isEmpty(destinationPoint.getNameTh()) && StringUtil.isEmpty(destinationPoint.getNameEn()))
                        {
                            continue;
                        }
                        LinkedList<Point> path = dijkstra.getPath(destinationPoint);
                        if (path != null && path.size() > 0)
                        {
                            String busPath = "";
                            for (Point point : path)
                            {
                                if (busPath.equals(""))
                                {
                                    busPath += "" + point.getId();
                                }
                                else
                                {
                                    busPath += "->" + point.getId();
                                }
                            }
                            MapDbBean.insertBusPath(sourcePoint.getId(), destinationPoint.getId(), busPath);
                            index_dest++;
                            if (index_dest <= 100)
                            {
                                txtTaskOutput.append(".");
                            }
                        }
                    }
                    count_dest = index_dest;
                }
                setProgress((index_source * 100) / total_source);
                txtTaskOutput.append(" completed. (" + StringUtil.toNumString(count_dest) + ")\n");
            }
            return null;
        }

        public void done()
        {
            Toolkit.getDefaultToolkit().beep();
            butStart.setEnabled(true);
            butClose.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            txtTaskOutput.append("Done!\n\n");
        }
    }

    public BusPathCalculator(Frame owner, String title)
    {
        super(owner, title, true);
        initialize();
    }

    private void initialize()
    {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(MapConstants.controlPanel);
        contentPane.setPreferredSize(new Dimension(800, 300));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        butStart.setActionCommand("start");
        butStart.addActionListener(this);

        prgTaskProgress.setValue(0);
        prgTaskProgress.setStringPainted(true);
        prgTaskProgress.setPreferredSize(new Dimension(700, 20));

        JPanel panelProgress = new JPanel();
        panelProgress.setBackground(MapConstants.controlPanel);
        panelProgress.add(butStart);
        panelProgress.add(prgTaskProgress);

        txtTaskOutput.setMargin(new Insets(5, 5, 5, 5));
        txtTaskOutput.setEditable(false);
        DefaultCaret caret = (DefaultCaret) txtTaskOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JSeparator line = new JSeparator(SwingConstants.HORIZONTAL);
        line.setPreferredSize(new Dimension(800, 1));

        butClose.setActionCommand("close");
        butClose.addActionListener(this);

        JPanel panelClose = new JPanel();
        panelClose.setBackground(MapConstants.controlPanel);
        panelClose.add(line);
        panelClose.add(butClose);

        contentPane.add(panelProgress, BorderLayout.PAGE_START);
        contentPane.add(new JScrollPane(txtTaskOutput), BorderLayout.CENTER);
        contentPane.add(panelClose, BorderLayout.PAGE_END);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent evt)
    {
        if ("start" == evt.getActionCommand())
        {
            butStart.setEnabled(false);
            butClose.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Instances of javax.swing.SwingWorker are not reusuable, so
            // we create new instances as needed.
            task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();
        }
        else if ("close" == evt.getActionCommand())
        {
            dispose();
        }
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if ("progress" == evt.getPropertyName())
        {
            int progress = (Integer) evt.getNewValue();
            prgTaskProgress.setValue(progress);
        }
    }
}
