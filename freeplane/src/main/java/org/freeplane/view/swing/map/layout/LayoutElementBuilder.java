package org.freeplane.view.swing.map.layout;

public class LayoutElementBuilder {

	protected static final EmptyLayoutElement EMPTY_LAYOUT_ELEMENT = new EmptyLayoutElement();
	protected LayoutElement layoutElement = EMPTY_LAYOUT_ELEMENT;
	private LayoutElementBuilder parent;

	public LayoutElementBuilder() {
	}

	public LayoutElement build() {
		return layoutElement;
	}

	public LayoutElementBuilder with(LayoutElement singleLayoutElement) {
		if(layoutElement == EMPTY_LAYOUT_ELEMENT)
			this.layoutElement = singleLayoutElement;
		else
			this.layoutElement = new LayoutElementPair(layoutElement, singleLayoutElement);
		return this;
	}

	public LayoutElementBuilder beginGroup() {
		return new LayoutElementBuilder(this);
	}

	
	public LayoutElementBuilder(LayoutElementBuilder parent) {
		this.parent = parent;
	}

	public LayoutElementBuilder withSummary(SingleLayoutElement summaryLayoutElement) {
		final LayoutSummaryPair layoutSummaryPair = new LayoutSummaryPair(layoutElement, summaryLayoutElement);
		parent.with(layoutSummaryPair);
		return parent;
	}
	

	public LayoutElementBuilder withSummaryBeginGroup(SingleLayoutElement summaryLayoutElement) {
		final LayoutElementBuilder layoutGroupBuilder = new LayoutElementBuilder(parent);
		final LayoutSummaryPair layoutSummaryPair = new LayoutSummaryPair(layoutElement, summaryLayoutElement);
		layoutGroupBuilder.with(layoutSummaryPair);
		return layoutGroupBuilder;
	}
}
