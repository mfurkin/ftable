/**
 * 
 */
package org.misha.ftableapp.model;

import java.util.ArrayList;

/**
 * Класс, описывающий положение команды в таблице
 * @author mikhailf
 *
 */
public class FootballTeamPosition {
		private IntegerParam games,wins,draws,losts,scored,scoredAway,passed;// число игр, побед, ничьих, поражений,
		private PointsParam points; // число очков
	    private String name;	// название команды
	    private InstantImpl tableTime;	// время обновления положения в таблице данной команды
	    private FootballTableData table = null;	// таблица, в которой считаем положение. Возможно, уйдёт
	    

	    
	    public FootballTeamPosition(int aGames, int aWins, int aDraws, int aLosts, int aScored, int aScoredAway, 
	    							int aPassed, int aPoints, String aName, InstantImpl aTableTime) {
	        games = new IntegerParam(aGames);
	        wins = new IntegerParam(aWins);
	        draws = new IntegerParam(aDraws);
	        losts = new IntegerParam(aLosts);
	        scored = new IntegerParam(aScored);
	        scoredAway = new IntegerParam(aScoredAway);
	        passed = new IntegerParam(aPassed);
	        points = new PointsParam(aPoints,this);
	        name = aName;
	        tableTime = aTableTime;
	        table = null;
	    }
	    
	    void setTable(FootballTableData aTable) {
	    	table = aTable;
	    }
	    /**
	     * Обнволяет положение данной команды, по итогам конкретной игры
	     * @param newScored - забито в данной игрк
	     * @param newPassed - пропущено в данной игре
	     * @param newTime 	- время окончания данной игры
	     * @param resultIndex - результат игры(см. комментарий к одноимённой функции в классе FootballTableData)
	     * @param aGuest - играла ли команда в гостях(для учёта мячей на чужом поле)
	     * @return - новую позицию, если игра была сейчас или старую, если результат игры уже учтён в таблице
	     */
	    public void updatePosition(int newScored, int newPassed, InstantImpl newTime, int resultIndex, boolean aGuest) {
	        if (isActual(newTime)) {
	            IntegerParam newParams[]={wins,draws,losts};
	            int ptsForGame[] = {3,1,0};
	            newParams[resultIndex].addValue(1);
	            if (aGuest)
	                scoredAway.addValue(newScored);
	            games.addValue(1);
	            scored.addValue(newScored);
	            passed.addValue(newPassed);
	            points.addValue(ptsForGame[resultIndex]);
	        }
	    }
	    /**
	     * Проверяет, является ли данное время актуальным
	     * @param time - время
	     * @return
	     */
	    private boolean isActual(InstantImpl time) {
	        return time.isAfter(tableTime);
	    }

	    /**
	     * Ищет все игры этой команды с соперником в указанной таблице
	     * @param table	- таблица для поиска
	     * @param pos	- команда-соперник
	     * @return	- см. комментарий к функции findGame в классе FootballTableData
	     */
	    ArrayList<FootballGame> findGamesWith(FootballTeamPosition pos) {
	        return table.findGame(name.concat("-").concat(pos.name));
	    }

	    /**
	     * Сравнение команд по доппараметрам.
	     * @param pos2 - вторая команда
	     * @return +1, если эта команда выше, 0 - если параметры равны, -1 - если другая команда выше
	     */
	    int compareByAddParams(FootballTeamPosition pos2) {
	        int result = compareByWins(pos2);
	        if (result == 0) {
	            result = compareByGoals(pos2);
	        }
	        return result;
	    }

	    /**
	     * Сравнение команд по разнице, числу забитых и числу забитых на чужом поле
	     * @param pos2
	     * @return см. комментарий к функции compareByAddParams
	     */
	    private int compareByGoals(FootballTeamPosition pos2) {
	        int result = - pos2.compareByDifference(scored.minus(passed));
	        if (result == 0) {
	            result = - pos2.compareByScored(scored);
	            if (result == 0)
	                result = - pos2.compareByScoredAway(scoredAway);
	        }
	        return result;
	    }

	    /**
	     * Сравнение команд по числу забитых на чужом поле
	     * @param aScoredAway - сколько на чужом поле забила другая команда
	     * @return см. комментарий к функции compareByAddParams
	     */
	    private int compareByScoredAway(IntegerParam aScoredAway) {
	    	return scoredAway.compareTo(aScoredAway);
	    }
	    
	    /**
	     * Сравнение команд по числу забитых во всех матчах
	     * @param aScored - сколько забила другая команда
	     * @return см. комментарий к функции compareByAddParams
	     */
	    private int compareByScored(IntegerParam aScored) {
	    	return scored.compareTo(aScored);
	    }

	    /**
	     * Сравнение команд по разнице мячей
	     * @param aDiff - разница мячей другой команды
	     * @return см. комментарий к функции compareByAddParams
	     */
	    private int compareByDifference(int aDiff) {
	        int diff = scored.minus(passed);
	        return (diff == aDiff) ? 0 : (diff > aDiff) ? 1 : -1;
	    }

	    /**
	     * Сравнение команд по числу побед
	     * @param pos2 - вторая команда
	     * @return см. комментарий к функции compareByAddParams
	     */
	    private int compareByWins(FootballTeamPosition pos2) {
	        return wins.compareTo(pos2.wins);
	    }

	    /**
	     * Сравнение команд по числу набранных очков
	     * @param pos2 - вторая команда
	     * @return
	     */
	    int compareByPoints(FootballTeamPosition pos2) {
	    	return points.compareTo(pos2.points);
	    }

	    /**
	     * Сравнение по личным встречам(по разнице). 
	     * @param game1 - игра, которую рассматриваем
	     * @return см. комментарий к функции compareByAddParams
	     */
		int calcPersonal(FootballGame game1) {
			return (game1 == null) ? 0 : game1.getDifference(name);
		}

		/**
		 * Сравнение по личным встречам(мячи на чужом поле)
		 * @param game1 - игра, которую рассматриваем
		 * @return см. комментарий к функции compareByAddParams
		 */
		int calcPersonalAway(FootballGame game1) {
			return (game1 == null) ? 0 : game1.getAwayGoals(name);
		}

		public Object[] toArray() {
			Object [] result = {name,games,wins,draws,losts,scored,passed,points};
			return result;
		}

		@Override
		public String toString() {
			String delim1 = " ",delim2 = "(",delim3 = ")-",result = getPosNum();
			result = addString(result,name.replace(' ','_'),delim1);
			result = addIntegerParam(result,games,delim1);
			result = addIntegerParam(result,wins,delim1);
			result = addIntegerParam(result,draws,delim1);
			result = addIntegerParam(result,losts,delim1);
			result = addIntegerParam(result,scored,delim1);
			result = addIntegerParam(result,scoredAway,delim2);
			result = addIntegerParam(result,passed,delim3);
			result = addIntegerParam(result,points,delim1);
			return result; 
		}

		private String addIntegerParam(String result, IntegerParam num, String delim) {
			return addString(result,num.toString(),delim);
		}
		
		private String addString(String first, String second, String delim) {
			return first.concat(delim).concat(second);
		}
		
		/**
		 * Вспомогательная функция для сериализации. Получает номер позиции команды в таблице из таблицы
		 * @return позиция команды или "__"
		 */
		private String getPosNum() {
			return (table == null) ? "__" : Integer.toString(table.getPosition(name));
		}
		
		/**
		 * Прокси для обновления положения команды
		 * @param aTable - обновляемая таблица
		 * @param index - новое положение
		 */
		public void setNewNumber(FootballTableData aTable, int index) {
			aTable.setNewPosition(name,index);
		}

		int calcOtherParams(FootballTeamPosition position) {
			return table.calcOtherParams(this,position);
		}		
}
