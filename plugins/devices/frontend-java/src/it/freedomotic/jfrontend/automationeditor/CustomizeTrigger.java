/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CustomizeEvent.java
 *
 * Created on 4-set-2010, 9.57.19
 */
package it.freedomotic.jfrontend.automationeditor;

import it.freedomotic.app.Freedomotic;
import it.freedomotic.reactions.Payload;
import it.freedomotic.reactions.Statement;
import it.freedomotic.reactions.Trigger;
import it.freedomotic.reactions.TriggerPersistence;
import it.freedomotic.util.i18n;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author enrico
 */
public class CustomizeTrigger extends javax.swing.JFrame {

    ReactionList main;
    Trigger original;
    DefaultTableModel model = new DefaultTableModel();
    JTable table;

    /**
     * Creates new form CustomizeEvent
     */
    public CustomizeTrigger(ReactionList main, Trigger t) {
        initComponents();
        this.main = main;
        original = t;
        this.setTitle(t.getName() + " Trigger Editor");
        txtName.setText(t.getName());
        txtDescription.setText(t.getDescription());
        txtChannel.setText(t.getChannel());
        txtSuspTime.setText(Long.toString(t.getSuspensionTime())) ;
        txtDelay.setText(Integer.toString(t.getDelay()));
        txtMaxExTimes.setText(Long.toString(t.getMaxExecutions()));
        lblExplanation.setText("Fire this trigger if parameters are:");

        if (t.isHardwareLevel() || !t.isToPersist()) {
            btnEdit.setEnabled(false);
        }
        lblTemplateWarning.setVisible(!t.isToPersist());

        model.addColumn(i18n.msg("logical"));
        model.addColumn(i18n.msg("attribute"));
        model.addColumn(i18n.msg("operand"));
        model.addColumn(i18n.msg("value"));
        table = new JTable(model);
        pnlParam.add(table);
        Iterator it = t.getPayload().iterator();
        int row = 0;
        while (it.hasNext()) {
            Statement statement = (Statement) it.next();
            List list = new ArrayList();
            list.add(statement.getLogical());
            list.add(statement.getAttribute());
            list.add(statement.getOperand());
            list.add(statement.getValue());
            model.insertRow(row, list.toArray());
        }
    }

    private void addEmptyRow() {
        model.addRow(new Object[]{"", "", "", ""});
    }

    private void save(Trigger t) {
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }
        
        Payload payload = new Payload();
        for (int r = 0; r < model.getRowCount(); r++) {
            boolean saveCurrent = true;
            for (int k = 0;k<4;k++){
                String test = model.getValueAt(r, 0).toString();
                if (test == null || test.equals("")){
                    saveCurrent = false;
                    break;
                }
            }  
            if (saveCurrent) payload.addStatement(
                    model.getValueAt(r, 0).toString(),
                    model.getValueAt(r, 1).toString(),
                    model.getValueAt(r, 2).toString(),
                    model.getValueAt(r, 3).toString());

        }
        t.setName(txtName.getText());
        t.setDescription(txtDescription.getText());
        t.setChannel(txtChannel.getText());
        t.setSuspensionTime(Long.parseLong(txtSuspTime.getText()));
        t.setDelay(Integer.parseInt(txtDelay.getText()));
        t.setMaxExecutions(Long.parseLong(txtMaxExTimes.getText()));
        t.setPersistence(true);
        t.setPayload(payload);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSave = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        lblExplanation = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlParam = new javax.swing.JPanel();
        btnAddRow = new javax.swing.JButton();
        lblTemplateWarning = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtChannel = new javax.swing.JTextField();
        txtSuspTime = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDelay = new javax.swing.JTextField();
        txtMaxExTimes = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnSave.setText(i18n.msg("save_as_new")+i18n.msg("trigger"));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jLabel1.setText(i18n.msg("name")+":");

        jLabel2.setText(i18n.msg("description")+":");

        lblExplanation.setText("Fire the event if this parameters are consistent with the received event:");

        btnEdit.setText(i18n.msg("save"));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText(i18n.msg("delete")+i18n.msg("trigger"));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        pnlParam.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(pnlParam);

        btnAddRow.setText("Add Statement");
        btnAddRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRowActionPerformed(evt);
            }
        });

        lblTemplateWarning.setText("This trigger is a predefined template so you cannot change it. Save it as new trigger.");

        jLabel3.setText(i18n.msg("channel")+":");

        jLabel4.setText(i18n.msg("suspension_time")+":");

        jLabel5.setText(i18n.msg("delay")+":");

        jLabel6.setText(i18n.msg("max_executions")+":");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTemplateWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEdit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblExplanation)
                                .addGap(18, 18, 18)
                                .addComponent(btnAddRow))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(61, 61, 61)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaxExTimes, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtDescription)
                                        .addComponent(txtName)
                                        .addComponent(txtChannel)
                                        .addComponent(txtSuspTime, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDelay, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 183, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtChannel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSuspTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaxExTimes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExplanation)
                    .addComponent(btnAddRow))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTemplateWarning)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnSave)
                    .addComponent(btnDelete))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        Trigger trigger = new Trigger();
        save(trigger); //saves as new trigger
        trigger.setPersistence(true);
        int preSize = TriggerPersistence.size();
        TriggerPersistence.addAndRegister(trigger);
        int postSize = TriggerPersistence.size();
        if (preSize < postSize) {
            Freedomotic.logger.info("Trigger added correctly [" + postSize + " triggers]");
        } else {
            Freedomotic.logger.warning("Error while addind a trigger in trigger editor");
        }
        //to be sure it can be saved on hard drive
        main.setTargetTrigger(trigger);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        save(original); //save changes over original trigger
        main.setTargetTrigger(original);
        this.dispose();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        System.out.println("Trying to remove a trigger from the list");
        TriggerPersistence.remove(original);
        main.updateData();
        this.dispose();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRowActionPerformed
        addEmptyRow();
    }//GEN-LAST:event_btnAddRowActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddRow;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblExplanation;
    private javax.swing.JLabel lblTemplateWarning;
    private javax.swing.JPanel pnlParam;
    private javax.swing.JTextField txtChannel;
    private javax.swing.JTextField txtDelay;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtMaxExTimes;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtSuspTime;
    // End of variables declaration//GEN-END:variables

    private int getOperatorIndex(String operand) {
        if (operand.equalsIgnoreCase("EQUALS")) {
            return 0;
        }
        if (operand.equalsIgnoreCase("GREATER_THEN")) {
            return 1;
        }
        if (operand.equalsIgnoreCase("LESS_THEN")) {
            return 2;
        }
        if (operand.equalsIgnoreCase("REGEX")) {
            return 3;
        }
        return -1;
    }

    private String getOperatorName(int index) {
        if (index == 0) {
            return "EQUALS";
        }
        if (index == 1) {
            return "GREATER_THEN";

        }
        if (index == 2) {
            return "LESS_THEN";

        }
        if (index == 3) {
            return "REGEX";
        }
        return "";
    }
}
