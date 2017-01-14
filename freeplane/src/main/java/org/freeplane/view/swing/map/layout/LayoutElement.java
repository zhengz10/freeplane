package org.freeplane.view.swing.map.layout;

public interface LayoutElement {
	
	int getContentSize();
	int getShift();

	void setCoordinate(int coordinate);
}
