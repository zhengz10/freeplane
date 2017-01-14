package org.freeplane.view.swing.map.layout;

public class LayoutSummaryPair implements LayoutElement {

	private LayoutElement first;
	private LayoutElement second;

	public LayoutSummaryPair(LayoutElement firstLayoutElement, LayoutElement secondLayoutElement) {
		this.first = firstLayoutElement;
		this.second = secondLayoutElement;
	}

	@Override
	public String toString() {
		return "{" + first + ":" + second + "}";
	}
}
