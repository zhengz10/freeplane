package org.freeplane.view.swing.map.layout;

import static org.assertj.core.api.Assertions.assertThat;

import org.freeplane.view.swing.map.layout.StructureBuilder.PairFactory;
import org.junit.Before;
import org.junit.Test;

public class StructureBuilderShould {
	private int nodeCount = 0;
	private SingleElement element() {
		return element("e" + ++nodeCount);
	}
	
	private SingleElement element(final String name) {
		return new Node(name);
	}
	
	private static class Node extends SingleElement {
		private final String s;

		private Node(String s) {
			this.s = s;
		}

		@Override
		public String toString() {
			return s;
		}
	}


	private StructureBuilder<Element> uut;

	@Before
	public void setup() throws Exception {
		uut = new StructureBuilder<Element>(new EmptyElement(), new PairFactory<Element>() {

			@Override
			public Element createPair(Element first, Element second) {
				return new ElementPair(first, second);
			}
		}, 
				new PairFactory<Element>() {

			@Override
			public Element createPair(Element first, Element second) {
				return new SummaryPair(first, second);
			}
		});
	}

	@Test
	public void createEmptyLayoutElement() throws Exception {
		Element builtElement = uut.build();
		assertThat(builtElement).isExactlyInstanceOf(EmptyElement.class);
	}

	@Test
	public void createElementWithSingleNode() throws Exception {
		final SingleElement singleLayoutElement = element();
		Element builtElement = uut.with(singleLayoutElement).build();
		assertThat(builtElement).isSameAs(singleLayoutElement);
	}


	@Test
	public void createElementWithTwoSingleNodes() throws Exception {
		final SingleElement firstLayoutElement = element();
		final SingleElement secondLayoutElement = element();
		Element builtElement = uut.with(firstLayoutElement).with(secondLayoutElement).build();
		assertThat(builtElement).isExactlyInstanceOf(ElementPair.class);
		assertThat(new ElementPair(firstLayoutElement, secondLayoutElement))
			.isEqualToComparingFieldByField(builtElement);
	}


	@Test
	public void createElementWithSummary() throws Exception {
		final SingleElement firstLayoutElement = element();
		final SingleElement summaryLayoutElement = element();
		Element builtElement = uut.beginGroup().with(firstLayoutElement).withSummary(summaryLayoutElement).build();
		assertThat(builtElement).isExactlyInstanceOf(SummaryPair.class);
		assertThat(new SummaryPair(firstLayoutElement, summaryLayoutElement))
			.isEqualToComparingFieldByField(builtElement);
	}

	@Test
	public void createElementWithSummaryFollowedBySingleElement() throws Exception {
		final SingleElement firstLayoutElement = element();
		final SingleElement summaryLayoutElement = element();
		final SingleElement otherLayoutElement = element();
		Element builtElement = uut.beginGroup().with(firstLayoutElement).withSummary(summaryLayoutElement).with(otherLayoutElement).build();
		assertThat(builtElement).isExactlyInstanceOf(ElementPair.class);
		assertThat(new ElementPair(new SummaryPair(firstLayoutElement, summaryLayoutElement), otherLayoutElement))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}

	@Test
	public void createElementFollowedByElementWithSummary() throws Exception {
		final SingleElement firstLayoutElement = element();
		final SingleElement secondLayoutElement = element();
		final SingleElement summaryLayoutElement = element();
		Element builtElement = uut.with(firstLayoutElement).beginGroup().with(secondLayoutElement).withSummary(summaryLayoutElement).build();
		assertThat(new ElementPair(firstLayoutElement, new SummaryPair(secondLayoutElement, summaryLayoutElement)))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}

	@Test
	public void createElementWithSummaryOfSummary() throws Exception {
		final SingleElement firstLayoutElement = element();
		final SingleElement summaryLayoutElement = element();
		final SingleElement secondSummaryLayoutElement = element();
		Element builtElement = uut.beginGroup().with(firstLayoutElement).withSummaryBeginGroup(summaryLayoutElement).withSummary(secondSummaryLayoutElement).build();
		assertThat(new SummaryPair(new SummaryPair(firstLayoutElement, summaryLayoutElement), secondSummaryLayoutElement))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}

	@Test
	public void createElementWithSummaryOfSummaryOfSummary() throws Exception {
		final SingleElement firstLayoutElement = element();
		final SingleElement summaryLayoutElement = element();
		final SingleElement secondSummaryLayoutElement = element();
		final SingleElement thirdSummaryLayoutElement = element();
		Element builtElement = uut.beginGroup().with(firstLayoutElement).withSummaryBeginGroup(summaryLayoutElement).withSummaryBeginGroup(secondSummaryLayoutElement).withSummary(thirdSummaryLayoutElement).build();
		assertThat(new SummaryPair(new SummaryPair(new SummaryPair(firstLayoutElement, summaryLayoutElement), secondSummaryLayoutElement), thirdSummaryLayoutElement))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}

	@Test
	public void createElementWithSummaryOfTwoSummaries() throws Exception {
		final SingleElement firstLayoutElement = element();
		final SingleElement firstSummaryLayoutElement = element();
		final SingleElement secondLayoutElement = element();
		final SingleElement secondSummaryLayoutElement = element();
		final SingleElement secondLevelSummaryLayoutElement = element();
		Element builtElement = uut.beginGroup().with(firstLayoutElement).withSummaryBeginGroup(firstSummaryLayoutElement)
				.beginGroup().with(secondLayoutElement).withSummary(secondSummaryLayoutElement).withSummary(secondLevelSummaryLayoutElement).build();
		assertThat(new SummaryPair(
				new ElementPair (
						new SummaryPair(firstLayoutElement, firstSummaryLayoutElement),
						new SummaryPair(secondLayoutElement, secondSummaryLayoutElement)), secondLevelSummaryLayoutElement))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}
	

	@Test
	public void finalTest() throws Exception {
		final SingleElement e1 = element();
		final SingleElement e2 = element();
		final SingleElement e3 = element();
		final SingleElement e4 = element();
		final SingleElement e5 = element();
		final SingleElement e6 = element();
		final SingleElement e7 = element();
		final SingleElement e8 = element();
		final SingleElement e9 = element();
		final SingleElement e10 = element();
		
		Element builtElement = uut.with(e1).beginGroup().with(e2).with(e3).withSummaryBeginGroup(e4)
				.with(e5)
				.beginGroup().with(e6).with(e7).withSummary(e8).withSummary(e9).with(e10).build();
		
		assertThat(builtElement.toString())
			.isEqualTo("e1,{{e2,e3:e4},e5,{e6,e7:e8}:e9},e10");
		
		
	}
	
}

interface Element{
	
}

class SummaryPair implements Element {

	private Element first;
	private Element second;

	public SummaryPair(Element firstLayoutElement, Element secondLayoutElement) {
		this.first = firstLayoutElement;
		this.second = secondLayoutElement;
	}

	@Override
	public String toString() {
		return "{" + first + ":" + second + "}";
	}
}

class ElementPair implements Element {

	private Element first;
	private Element second;

	public ElementPair(Element firstLayoutElement, Element layoutSummaryPair) {
		this.first = firstLayoutElement;
		this.second = layoutSummaryPair;
	}

	@Override
	public String toString() {
		return first + "," + second;
	}

}


class SingleElement implements Element {

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}

class EmptyElement implements Element {

}
