package org.misha.ftableapp;

import java.util.Comparator;

import org.misha.ftableapp.model.FootballTableData;
import org.misha.ftableapp.model.FootballTeamPosition;
import org.misha.ftableapp.model.PointsParam;
import org.misha.ftableapp.model.ViewUpdater;

import com.codename1.io.Log;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.SortableTableModel;
/**
 * Класс, реализующий интерфейс для обновления gui
 * @author mikhailf
 *
 */
public class ViewUpdaterImpl implements ViewUpdater {
	private int sortIndex;		// номер столбца для сортировки
	private String [] names;	// заголовки таблицы в gui
	private boolean editable;	// возможность вносить изменения
	private Comparator<PointsParam> comparator;	// компаратор для сравнения
	private TeamPosIndicator app;							//  собственно gui
	public static final int COLUMNS_VIEW_QTY = 10;
	public ViewUpdaterImpl(int aSortIndex, String[] aNames, boolean anEditable, Comparator<PointsParam> aComparator, 
							TeamPosIndicator anApp) {
		sortIndex = aSortIndex;
		names = aNames;
		editable = anEditable;
		comparator = aComparator;
		app = anApp;
	}

	private SortableTableModel getModel(FootballTeamPosition[] positions) {
		return new SortableTableModel(sortIndex,true,
				new DefaultTableModel(names,toObjectMatrix(positions),editable),comparator);
	}
	
	@Override
	public void updateView(FootballTeamPosition[] positions) {
		app.setData(getModel(positions));
	}
	
	@Override
	public void startView(FootballTeamPosition[] positions) {
		app.startView(getModel(positions));		
	}
	
	private Object[][] toObjectMatrix(FootballTeamPosition[] positions) {
		int col_qty = FootballTableData.COLUMNS_VIEW_QTY+1;
		Object [][] result = new Object[positions.length][col_qty];
		System.out.println("result len="+result.length+" res0 len="+result[0].length);
		for (int i=0;i<positions.length;i++) {			
			result[i][0] = i + 1;
			Object res [] = positions[i].toArray();
			System.out.println("res len="+res.length);
			for (int j=1;j<col_qty;j++) {
				result[i] [j]= res[j-1];
				System.out.println("toObjectMatrix obj["+i+"]["+j+"]="+result[i][j]);
			}
		}
		return result;
	}
}
