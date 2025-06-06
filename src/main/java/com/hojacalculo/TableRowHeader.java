package com.hojacalculo;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TableRowHeader extends JList<String> {
    public TableRowHeader(JTable table) {
        setFixedCellWidth(50);
        setFixedCellHeight(table.getRowHeight());
        setFont(table.getTableHeader().getFont());

        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            model.addElement(String.valueOf(i + 1));
        }
        setModel(model);

        table.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateModel(table);
            }
        });
    }

    private void updateModel(JTable table) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            model.addElement(String.valueOf(i + 1));
        }
        setModel(model);
    }
}