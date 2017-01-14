package org.freeplane.view.swing.map.layout;

public class StructureBuilder<T> {
	interface PairFactory<T> {
		T createPair(T first, T second);
	}
	
	private final T emptyElement;
	private final PairFactory<T> elementPairFactory;
	private final PairFactory<T> elementSummaryFactory;
	private T element;
	private StructureBuilder<T> parent;

	public StructureBuilder(T emptyElement, PairFactory<T> elementPairFactory, PairFactory<T> elementSummaryFactory) {
		this.emptyElement = emptyElement;
		this.element = emptyElement;
		this.elementPairFactory = elementPairFactory;
		this.elementSummaryFactory = elementSummaryFactory;
	}

	private StructureBuilder(StructureBuilder<T> parent) {
		this(parent.emptyElement, parent.elementPairFactory, parent.elementSummaryFactory);
		this.parent = parent;
	}

	public T build() {
		return element;
	}

	public StructureBuilder<T> with(T singleElement) {
		if(element == emptyElement)
			this.element = singleElement;
		else
			this.element = elementPairFactory.createPair(element, singleElement);
		return this;
	}

	public StructureBuilder<T> beginGroup() {
		return new StructureBuilder<T>(this);
	}

	
	public StructureBuilder<T> withSummary(T summaryElement) {
		final T summaryPair = elementSummaryFactory.createPair(element, summaryElement);
		parent.with(summaryPair);
		return parent;
	}
	

	public StructureBuilder<T> withSummaryBeginGroup(T summaryElement) {
		final StructureBuilder<T> groupBuilder = new StructureBuilder<T>(parent);
		final T summaryPair = elementSummaryFactory.createPair(element, summaryElement);
		groupBuilder.with(summaryPair);
		return groupBuilder;
	}
}
