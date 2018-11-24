package org.misha.ftableapp.model;
/**
 * Интерфейс для обновления gui
 * @author mikhailf
 *
 */
public interface ViewUpdater {
	void startView(FootballTeamPosition[] positions); // начать показывать данные
	void updateView(FootballTeamPosition[] positions);	// обновить данные
}
