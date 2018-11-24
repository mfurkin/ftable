package org.misha.ftableapp.model;
/**
 * Класс, описывающий отдельную игру. Пока хранится только счёт, потом предполагается расширить данные
 * @author mikhailf
 *
 */
public class FootballGame {
	  	private String hosts,guests;		// названия команд хозяев и гостей
	    private int scoredHosts, scoredGuests;	// счёт 
	    private InstantImpl gameTime; // время окончания игры
	    public final static int WIN = 0, DRAW = 1, LOST = 2;
	    public FootballGame(String aHosts, String aGuests, int aScoredHosts, int aScoredGuests, InstantImpl aGameTime) {
	        hosts = aHosts;
	        guests = aGuests;
	        scoredHosts = aScoredHosts;
	        scoredGuests = aScoredGuests;
	        gameTime = aGameTime;
	    }
	    // Обновляет положение каждой команды-участницы
	    public void updatePositions(FootballTableData table) {
	        if (scoredHosts == scoredGuests) {
	            // Ничья!
	            table.updatePosition(hosts,scoredHosts,scoredGuests,gameTime,DRAW,false);
	            table.updatePosition(guests,scoredGuests,scoredHosts,gameTime,DRAW,true);
	        } else {
	            if (scoredHosts > scoredGuests) {
	                // Победили хозяева
	                table.updatePosition(hosts,scoredHosts,scoredGuests,gameTime,WIN,false);
	                table.updatePosition(guests,scoredGuests,scoredHosts,gameTime,LOST,true);
	            } else {
	                // Победили гости
	                table.updatePosition(guests,scoredGuests,scoredHosts,gameTime,WIN,true);
	                table.updatePosition(hosts,scoredHosts,scoredGuests,gameTime,LOST,false);
	            }
	        }
	    }
	    
	    private int calcHostGuest(int forHosts, int forGuests, String name) {
	    	int result;
			if (name.equals(hosts))
				result = forHosts;
			else 
				if (name.equals(guests))
					result = forGuests;
				else 
					throw new IllegalArgumentException();
			return result;
	    }
	    
	    // Считает разницу мячей данной игры
		int getDifference(String name) {
			int diff = scoredHosts - scoredGuests;
			return calcHostGuest(diff,-diff,name);
		}

		// Возвращает сколько мячей на чужом поле забила команда в ходе данной игры
		int getAwayGoals(String name) {
			return calcHostGuest(0,scoredGuests,name);
		}

		// Добавляет игру в таблицу игр
		void addMe(FootballGamesData table) {
			table.addThisGameToIndex(this,getFirstKey(),getSecondKey());
		}

		private String getFirstKey() {
			return hosts.concat("-").concat(guests);
		}

		private String getSecondKey() {
			return guests.concat("-").concat(hosts);
		}
}
