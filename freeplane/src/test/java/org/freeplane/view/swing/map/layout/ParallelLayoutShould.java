package org.freeplane.view.swing.map.layout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParallelLayoutShould {
	@Mock
	private LayoutElement first;
	@Mock
	private LayoutElement second;

	private ParallelLayout uut;
	
	@Before
	public void setup(){
		uut = new ParallelLayout(first, second);
	}

	
	@Test
	public void placeSecondElementInTheMiddle() throws Exception {
		when(first.getContentSize()).thenReturn(4);
		when(second.getContentSize()).thenReturn(2);
		uut.setCoordinate(0);
		verify(first).setCoordinate(0);
		verify(second).setCoordinate(1);
	}

	@Test
	public void placeFirstElementInTheMiddle() throws Exception {
		when(first.getContentSize()).thenReturn(2);
		when(second.getContentSize()).thenReturn(4);
		uut.setCoordinate(0);
		verify(first).setCoordinate(1);
		verify(second).setCoordinate(0);
	}

	@Test
	public void shiftSecondElementInPositiveDirection() throws Exception {
		when(first.getContentSize()).thenReturn(4);
		when(second.getContentSize()).thenReturn(2);
		when(second.getShift()).thenReturn(10);
		uut.setCoordinate(0);
		verify(first).setCoordinate(0);
		verify(second).setCoordinate(11);
	}

	@Test
	public void shiftSecondElementInNegativeDirection() throws Exception {
		when(first.getContentSize()).thenReturn(4);
		when(second.getContentSize()).thenReturn(2);
		when(second.getShift()).thenReturn(-2);
		uut.setCoordinate(0);
		verify(first).setCoordinate(1);
		verify(second).setCoordinate(0);
	}

	@Test
	public void shiftFirstElementInPositiveDirection() throws Exception {
		when(first.getContentSize()).thenReturn(4);
		when(first.getShift()).thenReturn(10);
		when(second.getContentSize()).thenReturn(2);
		uut.setCoordinate(0);
		verify(first).setCoordinate(9);
		verify(second).setCoordinate(0);
	}

	@Test
	public void placeElementsAtGivenCoordinate() throws Exception {
		uut.setCoordinate(1);
		verify(first).setCoordinate(1);
		verify(second).setCoordinate(1);
	}

	@Test
	public void returnFirstElementShiftAsItsOwnShift() throws Exception {
		when(first.getShift()).thenReturn(10);
		assertThat(uut.getShift()).isEqualTo(10);
		
	}


	@Test
	public void returnFirstElementContentSizeAsItsOwnContentSize() throws Exception {
		when(first.getContentSize()).thenReturn(10);
		when(second.getContentSize()).thenReturn(1);
		assertThat(uut.getContentSize()).isEqualTo(10);
		
	}

	@Test
	public void returnSecondElementContentSizeAsItsOwnContentSize() throws Exception {
		when(first.getContentSize()).thenReturn(1);
		when(second.getContentSize()).thenReturn(10);
		assertThat(uut.getContentSize()).isEqualTo(10);
		
	}
}
