package org.misha.ftableapp;

import java.util.Comparator;

import org.misha.ftableapp.model.PointsParam;
import org.misha.ftableapp.model.ViewUpdater;
import org.misha.ftableapp.model.ViewUpdaterFactory;
/**
 * Класс-фабрика для VuewUpdater. Финт ушами, чтобы не создавать отдельный компаратор
 * @author mikhailf
 *
 */
public class ViewUpdaterFactoryImpl implements ViewUpdaterFactory {
	private int sortIndex;
	private String[] names;
	private boolean editable;
	private TeamPosIndicator indicator;
	
	public ViewUpdaterFactoryImpl(int aSortIndex, String[] aNames, boolean anEditable, TeamPosIndicator anIndicator) {
		sortIndex = aSortIndex;
		names = aNames;
		editable = anEditable;
		indicator = anIndicator;
	}

	@Override
	public ViewUpdater createUpdater(Comparator<PointsParam> comparator) {
		return new ViewUpdaterImpl(sortIndex,names,editable,comparator,indicator);
	}
}
