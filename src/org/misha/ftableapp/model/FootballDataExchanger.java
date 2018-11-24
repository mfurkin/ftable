package org.misha.ftableapp.model;

/**
 * Абстрактный класс для записи/чтения данных в/из файл(а). 
 * @author mikhailf
 *
 * @param <T>
 */
public abstract class FootballDataExchanger<T> {
	public abstract FootballDataResult<T> readData(String st);
	public String writeData(T data) {
		return data.toString();
	}
}
