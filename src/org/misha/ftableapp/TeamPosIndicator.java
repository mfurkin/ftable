package org.misha.ftableapp;

import com.codename1.ui.table.SortableTableModel;
/**
 * Интерфейс для gui. 
 * @author mikhailf
 *
 */
public interface TeamPosIndicator {
	void startView(SortableTableModel aModel);
	void setData(SortableTableModel aModel);
}
