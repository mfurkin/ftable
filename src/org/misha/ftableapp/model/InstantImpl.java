package org.misha.ftableapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Класс имитирующий Instant из java 8. Будет потом заменён нормальным Instant
 * @author mikhailf
 *
 */
public class InstantImpl {
	public final static String FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
	private Date date;
	

	public InstantImpl(Date aDate) {
		date = aDate;
	}


	public boolean isAfter(InstantImpl tableTime) {
		return  tableTime.isBefore(date.getTime());
	}

	private boolean isBefore(long time) {
		return date.getTime() < time;
	}


	public static InstantImpl fromString(String st) {
		SimpleDateFormat format = new SimpleDateFormat(FORMAT);
		Date d;
		try {
			d = format.parse(st);
		} catch (ParseException e) {
			e.printStackTrace();
			d = Calendar.getInstance().getTime();
		}
		return new InstantImpl(d);
	}


	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat(FORMAT);
		return format.format(date);
	}
	
}
