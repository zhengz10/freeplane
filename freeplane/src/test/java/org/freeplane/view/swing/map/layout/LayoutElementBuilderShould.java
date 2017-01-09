package org.freeplane.view.swing.map.layout;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class LayoutElementBuilderShould {
	private int nodeCount = 0;
	private SingleLayoutElement element() {
		return element("e" + ++nodeCount);
	}
	
	private SingleLayoutElement element(final String name) {
		return new Node(name);
	}
	
	private static class Node extends SingleLayoutElement {
		private final String s;

		private Node(String s) {
			this.s = s;
		}

		@Override
		public String toString() {
			return s;
		}
	}


	private LayoutElementBuilder uut;

	@Before
	public void setup() throws Exception {
		uut = new LayoutElementBuilder();
	}

	@Test
	public void createEmptyLayoutElement() throws Exception {
		LayoutElement builtElement = uut.build();
		assertThat(builtElement).isExactlyInstanceOf(EmptyLayoutElement.class);
	}

	@Test
	public void createElementWithSingleNode() throws Exception {
		final SingleLayoutElement singleLayoutElement = element();
		LayoutElement builtElement = uut.with(singleLayoutElement).build();
		assertThat(builtElement).isSameAs(singleLayoutElement);
	}


	@Test
	public void createElementWithTwoSingleNodes() throws Exception {
		final SingleLayoutElement firstLayoutElement = element();
		final SingleLayoutElement secondLayoutElement = element();
		LayoutElement builtElement = uut.with(firstLayoutElement).with(secondLayoutElement).build();
		assertThat(builtElement).isExactlyInstanceOf(LayoutElementPair.class);
		assertThat(new LayoutElementPair(firstLayoutElement, secondLayoutElement))
			.isEqualToComparingFieldByField(builtElement);
	}


	@Test
	public void createElementWithSummary() throws Exception {
		final SingleLayoutElement firstLayoutElement = element();
		final SingleLayoutElement summaryLayoutElement = element();
		LayoutElement builtElement = uut.beginGroup().with(firstLayoutElement).withSummary(summaryLayoutElement).build();
		assertThat(builtElement).isExactlyInstanceOf(LayoutSummaryPair.class);
		assertThat(new LayoutSummaryPair(firstLayoutElement, summaryLayoutElement))
			.isEqualToComparingFieldByField(builtElement);
	}

	@Test
	public void createElementWithSummaryFollowedBySingleElement() throws Exception {
		final SingleLayoutElement firstLayoutElement = element();
		final SingleLayoutElement summaryLayoutElement = element();
		final SingleLayoutElement otherLayoutElement = element();
		LayoutElement builtElement = uut.beginGroup().with(firstLayoutElement).withSummary(summaryLayoutElement).with(otherLayoutElement).build();
		assertThat(builtElement).isExactlyInstanceOf(LayoutElementPair.class);
		assertThat(new LayoutElementPair(new LayoutSummaryPair(firstLayoutElement, summaryLayoutElement), otherLayoutElement))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}

	@Test
	public void createElementFollowedByElementWithSummary() throws Exception {
		final SingleLayoutElement firstLayoutElement = element();
		final SingleLayoutElement secondLayoutElement = element();
		final SingleLayoutElement summaryLayoutElement = element();
		LayoutElement builtElement = uut.with(firstLayoutElement).beginGroup().with(secondLayoutElement).withSummary(summaryLayoutElement).build();
		assertThat(new LayoutElementPair(firstLayoutElement, new LayoutSummaryPair(secondLayoutElement, summaryLayoutElement)))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}

	@Test
	public void createElementWithSummaryOfSummary() throws Exception {
		final SingleLayoutElement firstLayoutElement = element();
		final SingleLayoutElement summaryLayoutElement = element();
		final SingleLayoutElement secondSummaryLayoutElement = element();
		LayoutElement builtElement = uut.beginGroup().with(firstLayoutElement).withSummaryBeginGroup(summaryLayoutElement).withSummary(secondSummaryLayoutElement).build();
		assertThat(new LayoutSummaryPair(new LayoutSummaryPair(firstLayoutElement, summaryLayoutElement), secondSummaryLayoutElement))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}

	@Test
	public void createElementWithSummaryOfSummaryOfSummary() throws Exception {
		final SingleLayoutElement firstLayoutElement = element();
		final SingleLayoutElement summaryLayoutElement = element();
		final SingleLayoutElement secondSummaryLayoutElement = element();
		final SingleLayoutElement thirdSummaryLayoutElement = element();
		LayoutElement builtElement = uut.beginGroup().with(firstLayoutElement).withSummaryBeginGroup(summaryLayoutElement).withSummaryBeginGroup(secondSummaryLayoutElement).withSummary(thirdSummaryLayoutElement).build();
		assertThat(new LayoutSummaryPair(new LayoutSummaryPair(new LayoutSummaryPair(firstLayoutElement, summaryLayoutElement), secondSummaryLayoutElement), thirdSummaryLayoutElement))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}

	@Test
	public void createElementWithSummaryOfTwoSummaries() throws Exception {
		final SingleLayoutElement firstLayoutElement = element();
		final SingleLayoutElement firstSummaryLayoutElement = element();
		final SingleLayoutElement secondLayoutElement = element();
		final SingleLayoutElement secondSummaryLayoutElement = element();
		final SingleLayoutElement secondLevelSummaryLayoutElement = element();
		LayoutElement builtElement = uut.beginGroup().with(firstLayoutElement).withSummaryBeginGroup(firstSummaryLayoutElement)
				.beginGroup().with(secondLayoutElement).withSummary(secondSummaryLayoutElement).withSummary(secondLevelSummaryLayoutElement).build();
		assertThat(new LayoutSummaryPair(
				new LayoutElementPair (
						new LayoutSummaryPair(firstLayoutElement, firstSummaryLayoutElement),
						new LayoutSummaryPair(secondLayoutElement, secondSummaryLayoutElement)), secondLevelSummaryLayoutElement))
			.isEqualToComparingFieldByFieldRecursively(builtElement);
	}
	

	@Test
	public void finalTest() throws Exception {
		final SingleLayoutElement e1 = element();
		final SingleLayoutElement e2 = element();
		final SingleLayoutElement e3 = element();
		final SingleLayoutElement e4 = element();
		final SingleLayoutElement e5 = element();
		final SingleLayoutElement e6 = element();
		final SingleLayoutElement e7 = element();
		final SingleLayoutElement e8 = element();
		final SingleLayoutElement e9 = element();
		final SingleLayoutElement e10 = element();
		
		LayoutElement builtElement = uut.with(e1).beginGroup().with(e2).with(e3).withSummaryBeginGroup(e4)
				.with(e5)
				.beginGroup().with(e6).with(e7).withSummary(e8).withSummary(e9).with(e10).build();
		
		assertThat(builtElement.toString())
			.isEqualTo("e1,{{e2,e3:e4},e5,{e6,e7:e8}:e9},e10");
		
		
	}
	
}
