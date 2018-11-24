package org.misha.ftableapp.model;

import java.util.Comparator;

public interface ViewUpdaterFactory {
	ViewUpdater createUpdater(Comparator<PointsParam> comparator);
}
