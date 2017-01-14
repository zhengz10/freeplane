package org.freeplane.view.swing.map.layout;


public class ParallelLayout implements LayoutElement{
	private final LayoutElement first;
	private final LayoutElement second;

	public ParallelLayout(LayoutElement first, LayoutElement second) {
		super();
		this.first = first;
		this.second = second;
	}

	@Override
	public int getContentSize() {
		return Math.max(first.getContentSize(), second.getContentSize());
	}

	@Override
	public int getShift() {
		return first.getShift();
	}

	@Override
	public void setCoordinate(int coordinate) {
		final int firstSize = first.getContentSize();
		final int firstShift = first.getShift();
		final int secondSize = second.getContentSize();
		final int secondShift = second.getShift();
		final int secondCoordinateRelativelyToFirst = (firstSize - secondSize) / 2 + (secondShift - firstShift);
		if(secondCoordinateRelativelyToFirst > 0) {
			first.setCoordinate(coordinate);
			second.setCoordinate(coordinate + secondCoordinateRelativelyToFirst);
		}
		else {
			first.setCoordinate(coordinate-secondCoordinateRelativelyToFirst);
			second.setCoordinate(coordinate);
		}
	}

}
