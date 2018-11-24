/**
 * 
 */
package org.misha.ftableapp.model;

/**
 * @author mikhailf
 *
 */
public class PointsParam extends IntegerParam {
	private FootballTeamPosition position;
	/**
	 * @param aCurrentValue
	 */
	public PointsParam(int aCurrentValue, FootballTeamPosition aPosition) {
		super(aCurrentValue);
		position = aPosition;
	}

	@Override
	public int compareTo(IntegerParam another) {
		int result = super.compareTo(another);
		if ((result == 0) && (another instanceof PointsParam)) {
			result = position.calcOtherParams(((PointsParam)another).position);
		}
		return result; 
	}

}
