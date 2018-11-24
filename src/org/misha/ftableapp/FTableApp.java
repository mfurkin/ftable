package org.misha.ftableapp;


import static com.codename1.ui.CN.addNetworkErrorListener;
import static com.codename1.ui.CN.getCurrentForm;
import static com.codename1.ui.CN.updateNetworkThreadCount;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.misha.ftableapp.model.FootballDataExchanger;
import org.misha.ftableapp.model.FootballDataResult;
import org.misha.ftableapp.model.FootballGamesData;
import org.misha.ftableapp.model.FootballTableData;
import org.misha.ftableapp.model.GamesDataExchanger;
import org.misha.ftableapp.model.InstantImpl;
import org.misha.ftableapp.model.TableDataExchanger;

import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.SortableTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.util.Resources;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 */
public class FTableApp implements TeamPosIndicator {

    private Form current;
    private Resources theme;
	private Table table;
    
    private final static String CURRENT_TABLE = "table.txt";
    private final static String CURRENT_GAMES = "games.txt";
	private final static String [] names = {"№","Команда","Игр","В","Н","П","МЗ","МП","Очки"};	
	
	
    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature, uncomment if you have a pro subscription
        Log.bindCrashProtection(true);
        createCurrentTable();
        //
        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    private void createCurrentTable() {
    	String pos[] = { 	
  //  						"24/11/2018 01:26\n",
    						"1. Зенит 14 10 1 3 22(9)-11 31\n",
    			 			"2. Краснодар 14 8 2 4 25(15)-12 26\n",
    			 			"3. ЦСКА 15 7 5 3 21(10)-7 26\n",
    			 			"4. Локомотив 15 7 4 4 20(9)-14 25\n",
    			 			"5. Ростов 14 6 5 3 14(7)-8 23\n",
    			 			"6. Урал 15 6 4 5 18(7)-22 22\n",
    			 			"7. Рубин 14 4 8 2 13(5)-11 20\n",
    			 			"8. Ахмат 15 5 4 6 12(7)-16 19\n",
    			 			"9. Спартак 14 5 4 5 14(6)-15 19\n",
    			 			"10. Оренбург 14 5 4 5 15(11)-13 19\n",
    			 			"11. Арсенал 14 4 5 5 19(5)-18 17\n",
    			 			"12. Динамо 14 3 7 4 10(3)-10 16\n",
    			 			"13. Крылья_Советов 14 4 2 8 8(5)-19 14\n",
    			 			"14. Уфа 14 3 5 6 11(3)-16 14\n",
    			 			"15. Анжи 14 4 1 9 8(3)-21 13\n",
    			 			"16. Енисей 14 1 3 10 7(2)-24 6\n"    			
    	};
    	SimpleDateFormat format = new SimpleDateFormat(InstantImpl.FORMAT);
    	try (OutputStream os = Storage.getInstance().createOutputStream(CURRENT_TABLE);) {
    		os.write(format.format(Calendar.getInstance().getTime()).concat("\n").getBytes());
    		for (String st : pos) {
    			os.write(st.getBytes());
    		}		
    	} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
        if(current != null){
            current.show();
            return;
        }
        Form hi = new Form("РПЛ 2018/2019", BoxLayout.y());
        hi.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        FootballGamesData games = buildGames();
        FootballTableData data = buildTable(games);
        data.startView();
        hi.add(new Label("Чемпионат России по футболу 2018/2019")).add(table);
        hi.show();
    }

    private FootballDataResult<?> buildFootballData(String name, FootballDataExchanger reader) {
    	String st;
    	try ( InputStream is = Storage.getInstance().createInputStream(CURRENT_TABLE);) {
    		st = Util.readToString(is);
		} catch (IOException e) {
			st = null;
			e.printStackTrace();
		}
    	return reader.readData(st);
    }
    
    private FootballTableData buildTable(FootballGamesData games) {
    	return (FootballTableData) buildFootballData(CURRENT_TABLE, new TableDataExchanger(games,names,this)).getData();
    }
    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    private FootballGamesData buildGames() {
		return (FootballGamesData) buildFootballData(CURRENT_GAMES, new GamesDataExchanger()).getData();
	}
    public void destroy() {
    }

	@Override
	public void setData(SortableTableModel aModel) {
		table.setModel(aModel);
	}

	@Override
	public void startView(SortableTableModel aModel) {
		table = new Table(aModel);
	}
}