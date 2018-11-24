package org.misha.ftableapp.model;
/**
 * Фигня, для получения считанных данных
 * @author mikhailf
 *
 * @param <T>
 */
public class FootballDataResult<T> {
	private T data;
	public FootballDataResult(T aData) {
		data = aData;
	}
	public T getData() {
		return data;
	}
}
