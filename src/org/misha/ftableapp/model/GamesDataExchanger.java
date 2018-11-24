package org.misha.ftableapp.model;

import java.util.ArrayList;
/**
 * Класс, для чтения/записи списка матчей
 * @author mikhailf
 *
 */
public class GamesDataExchanger extends FootballDataExchanger<FootballGamesData> {

	@Override
	public FootballDataResult<FootballGamesData> readData(String st) {
		// TODO Написать чтение файла матчей
		return new FootballDataResult<FootballGamesData>(new FootballGamesData(new ArrayList<FootballGame>()));
	}
}
