package org.misha.ftableapp.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Класс, описывающий собственно турнирную таблицу
 * @author mikhailf
 *
 */

public class FootballTableData implements Comparator<PointsParam>{
		public static final int NAME_INDEX = 1;
		public static final int GAMES_INDEX = 2;
		public static final int WINS_INDEX = 3;
		public static final int DRAW_INDEX = 4;
		public static final int LOSTS_INDEX = 5;
		public static final int SCORED_INDEX = 6;
		public static final int SCOREDAWAY_INDEX = 7;
		public static final int PASSED_INDEX = 8;
		public static final int POINTS_INDEX = 9;
		public static final int POINTS_VIEW_INDEX = 8;
		public static final int TEAM_QTY = 16;
		public final static int COLUMNS_QTY = 11;
		private FootballTeamPosition [] positions;	// собственно набор позиций - турнирная таблица
	    private FootballGamesData games;			// список игр для проверки позиций при равенстве очков
	    private HashMap<String,Integer> positionNums;	// map для быстрого поиска номера позиции по названию команды
	    
		private InstantImpl curTime;				// время последнего обновления таблицы
	    
		private ViewUpdater updater;				// фигня для обновления gui. Стыковка с view
//		public static final int COLUMNS_VIEW_QTY = 10;
		public static final int COLUMNS_VIEW_QTY = 8;

		
	    public FootballTableData(FootballTeamPosition[] aPositions, FootballGamesData aGames, InstantImpl aCurTime, 
	    							ViewUpdaterFactory factory) {
	        positions = aPositions;
	        games = aGames;
	        curTime = aCurTime;
	        updater = factory.createUpdater(this);
	        for (FootballTeamPosition position : positions)
	        	position.setTable(this);
	    }
	    /**
	     * Обновляет положение команды-участницы конкретной игры. Пересчитывает таблицу, если положение команды 
	     * изменилось(т.е. игра происходитв настоящем времени, а не просто вбили результат давно прошедшей игры  
	     * @param team - команда-участница
	     * @param scored - сколько команда забила
	     * @param passed - сколько команда пропустила
	     * @param gameTime - время окончания игры
	     * @param resultIndex - обозначение результата(0 - победа, 1 - ничья, 2 - поражение
	     * @param aGuest - играла ли команда в гостях(для учёта мячей на чужом поле)
	     */
	    void updatePosition(String team, int scored, int passed, InstantImpl gameTime, int resultIndex, boolean aGuest) {
	        int index = positionNums.get(team);
	        FootballTeamPosition pos = positions[index];
	   		pos.updatePosition(scored,passed,gameTime,resultIndex,aGuest);
            updateTable(gameTime);
	    }
	        
	    /**
	     * Обновляет положение команд и время обновления
	     * @param gameTime - время обновления
	     */
	    private void updateTable(InstantImpl gameTime) {
	    	curTime = gameTime;
		}

	    @Override
		public String toString() {
			String st = curTime.toString().concat("\n");
			for (FootballTeamPosition position : positions) {
				st.concat(position.toString().concat("\n"));
			}
			return st;
		}
	    
		/**
		 * Выдает текущее положение команды в таблице. Подумать, как избавиться от этой функции
		 * @param name
		 * @return
		 */
		int getPosition(String name) {
			return positionNums.get(name);
		}

		/**
		 *  Обновляет положение команды в map-е. Тоже уйдет, скорее всего, если уйдём от предыдущей функции
		 * @param name название команды
		 * @param index новое место
		 */
		void setNewPosition(String name, int index) {
			positionNums.put(name, index);
		}

		/**
		 *  В принципе, прокси для поиска по играм(См. одноимённую функцию в классе FootballGamesData
		 * @param game - ключ для потска игры
		 * @return список найдеенных игр(или пустой)
		 */
		ArrayList<FootballGame> findGame(String game) {
			return games.findGame(game);
		}
		
		@Override
		public int compare(PointsParam pts1, PointsParam pts2) {
	         int result =  pts1.compareTo(pts2);
	         System.out.println("result="+result);
	         return result;
		}

		/**
		 * Функция для определения победителейв двухраундовом поединке. Подумать, как вынести в отдельный класс
		 * @param games - список игр
		 * @param pos1 - команда 1
		 * @param pos2 - команда 2
		 * @return +1, если выше команда 1, 0 - если параметры равны, -1 - если выше команда 2
		 */
		private int calcGames(ArrayList<FootballGame> games, FootballTeamPosition pos1, FootballTeamPosition pos2) {
			int diff = 0, result;
			for (FootballGame game : games) {
				diff += pos1.calcPersonal(game);
			}
			if (diff !=0)
				result = diff / Math.abs(diff);
			else {
				int away1 = 0, away2 = 0;
				for (FootballGame game : games) {
					away1 += pos1.calcPersonalAway(game);
					away2 += pos2.calcPersonalAway(game);
				}
				result = (away1 > away2) ? 1 : (away1 < away2) ? -1 : 0;
			}
			return result;
		}
		
		/**
		 * Стартует показ gui.
		 */
		public void startView() {
			updater.startView(positions);
		}
		
		public int calcOtherParams(FootballTeamPosition pos1, FootballTeamPosition pos2) {
            ArrayList<FootballGame> games = pos1.findGamesWith(pos2);
			return ((games == null) || (games.isEmpty())) ? pos1.compareByAddParams(pos2) : 
					calcGames(games, pos1, pos2);
		}
}
