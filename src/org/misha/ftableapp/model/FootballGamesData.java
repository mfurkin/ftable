package org.misha.ftableapp.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.misha.ftableapp.model.FootballGame;
/** Таблица сыгранных игр(не турнирная таблица!!)
 * Просто список всех сыгранных матчей 
 * @author mikhailf
 *
 */
public class FootballGamesData {
	private ArrayList<FootballGame> games;
	private HashMap<String,ArrayList<FootballGame>> gameByTeams; // позволяет найти игру по участникам
	public FootballGamesData(ArrayList<FootballGame> aGames) {
		games = aGames;
		buildGamesByTeam();
	}
	
	/*
	 * Строит map для поиска игр по участникам
	 */
	private void buildGamesByTeam() {
		gameByTeams = new HashMap<String,ArrayList<FootballGame>>();
		for (FootballGame game : games) {
			game.addMe(this);
		}
	}
	
	/**
	 * Ищет игру по участникам
	 * @param game - участники игры в формате участник1-участник2
	 * @return пустой список либо список всех игр между данными командами в текущем чемпионате
	 */
	public ArrayList<FootballGame> findGame(String game) {
	    return existsGame(game) ? gameByTeams.get(game) : new ArrayList<FootballGame>();
	}
	
	/**
	 * Проверяет, сыграли ли команды хоть по разу
	 * @param key см. комментарий к функции findgame
	 * @return true - если сыграли, false - ещё не играли
	 */
	private boolean existsGame(String key) {
		return gameByTeams.containsKey(key);
	}
	
	/**
	 * Вспомогательная функция для построения map-а
	 * @param game - игра, которую нужно добавить в map
	 * @param key - ключ по которому её искать(см. комментарий к функции findgame
	 */
	private void addGameToIndex(FootballGame game, String key) {
		ArrayList<FootballGame> result;
		if (existsGame(key))
			result = gameByTeams.get(key);
		else {
			result = new ArrayList<>(2);
			gameByTeams.put(key, result);
		}
		result.add(game);
	}
	/**
	 *  Вспомогательная функция для добавления игры в map. Поскольку искать можно по обеим командам, 
	 *  ключей два("команда1-команда2" и "команда2-команда1"), вне зависимости от того, где играли
	 * @param game - добавляемая игра
	 * @param key1 - один ключ для игры(для поиска по команде хозяев)
	 * @param key2 - второй ключ для игрв(для поиска по команде гостей)
	 */
	void addThisGameToIndex(FootballGame game, String key1, String key2) {
		addGameToIndex(game,key1);
		addGameToIndex(game,key2);
	}
	
	@Override
	public String toString() {
		String result = "\n";
		for (FootballGame game : games) {
			result.concat(game.toString()).concat("\n");
		}
		return result;
	}
}
