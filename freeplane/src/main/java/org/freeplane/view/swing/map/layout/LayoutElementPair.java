package org.freeplane.view.swing.map.layout;

public class LayoutElementPair implements LayoutElement {

	private LayoutElement first;
	private LayoutElement second;

	public LayoutElementPair(LayoutElement firstLayoutElement, LayoutElement layoutSummaryPair) {
		this.first = firstLayoutElement;
		this.second = layoutSummaryPair;
	}

	@Override
	public String toString() {
		return first + "," + second;
	}

}
