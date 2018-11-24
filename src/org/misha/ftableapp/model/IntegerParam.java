/**
 * 
 */
package org.misha.ftableapp.model;

/**
 * @author mikhailf
 *
 */
public class IntegerParam implements Comparable<IntegerParam> {
	private int currentValue;
	
	public IntegerParam(int aCurrentValue) {
		currentValue = aCurrentValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(IntegerParam another) {
		System.out.println("compareTo value="+currentValue+" another value="+another.currentValue);
		return (currentValue == another.currentValue) ? 0 : (currentValue > another.currentValue) ? 1 : -1;
	}

	@Override
	public String toString() {
		return Integer.toString(currentValue);
	}
	
	public void updateValue(int newValue) {
		currentValue = newValue;
	}
	
	public void addValue(int aValue) {
		currentValue += aValue;
	}

	public int minus(IntegerParam passed) {
		return currentValue - passed.currentValue;
	}
}
