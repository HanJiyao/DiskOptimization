package algorithm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Objects;

// done by Han Jiyao Admin 164399M

// Swing init
class DiskOptimizationUI extends JFrame {
    private JLabel result = new JLabel();
    DiskOptimizationUI(){
        super("Disk Optimization App");
        setSize(900,300);
        setResizable(true);
        JLabel title = new JLabel("Choose One Algorithm: ");
        JPanel p = new JPanel();
        p.add(title);
        String[] algorithm = new String[]{"FCFS", "SSTF", "SCAN", "LOOK", "C-SCAN", "C-LOOK"};
        JComboBox<String> combo = new JComboBox<String>(algorithm);
        p.add(combo);
        String resultDisplay = String.valueOf(DiskOptimization.getResult().get(0));
        result.setText("<html>" + resultDisplay.replaceAll("<","&lt;").
                replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        combo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Get the source of the component, which is our combo
                JComboBox comboBox = (JComboBox) event.getSource();
                Object selected = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
                String resultChange="";
                if(selected.equals("FCFS"))
                    resultChange = String.valueOf(DiskOptimization.getResult().get(0));
                else if(selected.toString().equals("SSTF"))
                    resultChange = String.valueOf(DiskOptimization.getResult().get(1));
                else if(selected.toString().equals("SCAN"))
                    resultChange = String.valueOf(DiskOptimization.getResult().get(2));
                else if(selected.toString().equals("LOOK"))
                    resultChange = String.valueOf(DiskOptimization.getResult().get(3));
                else if(selected.toString().equals("C-SCAN"))
                    resultChange = String.valueOf(DiskOptimization.getResult().get(4));
                else if(selected.toString().equals("C-LOOK"))
                    resultChange = String.valueOf(DiskOptimization.getResult().get(5));
                result.setText("<html>" + resultChange.replaceAll("<","&lt;").replaceAll
                        (">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
            }
        });
        p.add(result);
        p.setBorder(new EmptyBorder(50,0,0,0));
        add(p);
        setVisible(true);
    }
}


