package org.misha.ftableapp.model;

import java.util.ArrayList;
import java.util.List;

import org.misha.ftableapp.TeamPosIndicator;
import org.misha.ftableapp.ViewUpdaterFactoryImpl;

import com.codename1.util.StringUtil;
/**
 * Класс для чтения/ записи турнирной таблицы
 * @author mikhailf
 *
 */
public class TableDataExchanger extends FootballDataExchanger<FootballTableData> {
	private FootballGamesData games; // таблица матчей
	private String [] names;		 // заголовок таблицы(нужно только для gui) 
	private TeamPosIndicator indicator;	// gui
	public TableDataExchanger(FootballGamesData aGames, String [] aNames, TeamPosIndicator anIndicator) {
		games = aGames;
		names = aNames;
		indicator = anIndicator;
	}

	@Override
	public FootballDataResult<FootballTableData> readData(String st) {
		FootballDataResult<FootballTableData> result = null;
		if (st != null) {
			List<String> teams_st = StringUtil.tokenize(st, "\r\n");
			InstantImpl time = InstantImpl.fromString(teams_st.get(0));
			teams_st.remove(0);
			ArrayList<FootballTeamPosition> positions = new ArrayList<FootballTeamPosition>(FootballTableData.TEAM_QTY);
			for ( String team_st : teams_st) {
				FootballTeamPosition pos = createPosition(team_st, time);
				positions.add(pos);
			}
			FootballTeamPosition pos2[] = new FootballTeamPosition[positions.size()];
			pos2 = positions.toArray(pos2);
			ViewUpdaterFactory factory = new ViewUpdaterFactoryImpl(FootballTableData.POINTS_VIEW_INDEX,names,false,indicator);
			result = new FootballDataResult<FootballTableData>(new FootballTableData(pos2,games,time,factory));
		}
		return result;
	}

	/**
	 * Вспомогательная функция. Создаёт позицию каждой команды
	 * @param team_st - название команды
	 * @param date - время обновления таблицы
	 * @return вновь созданное положение команды
	 */
	private FootballTeamPosition createPosition(String team_st,InstantImpl date) {
			String tokens[] = new String[FootballTableData.COLUMNS_QTY];
			tokens = StringUtil.tokenize(team_st, " -()").toArray(tokens);
			String name = tokens[FootballTableData.NAME_INDEX].replace('_', ' ');
			int game_qty = Integer.valueOf(tokens[FootballTableData.GAMES_INDEX]), 
					wins = Integer.valueOf(tokens[FootballTableData.WINS_INDEX]), 
					draws = Integer.valueOf(tokens[FootballTableData.DRAW_INDEX]), 
					losts = Integer.valueOf(tokens[FootballTableData.LOSTS_INDEX]),
					scored = Integer.valueOf(tokens[FootballTableData.SCORED_INDEX]), 
					scoredAway = Integer.valueOf(tokens[FootballTableData.SCOREDAWAY_INDEX]),
					passed = Integer.valueOf(tokens[FootballTableData.PASSED_INDEX]),	
					points = Integer.valueOf(tokens[FootballTableData.POINTS_INDEX]);
			return new FootballTeamPosition(game_qty,wins,draws,losts,scored,scoredAway,passed,points,name,date);
	}
}
