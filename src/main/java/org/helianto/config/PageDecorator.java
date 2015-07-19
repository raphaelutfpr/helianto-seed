package org.helianto.config;

import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Page decorator to start numbering from 1 instead of 0.
 * 
 * @author mauriciofernandesdecastro
 *
 * @param <T>
 */
public class PageDecorator<T> implements Page<T>{
	
	private final Page<T> page;
	
	public PageDecorator(Page<T> page) {
		super();
		this.page = page;
	}

	public List<T> getContent() {
		return page.getContent();
	}

	public int getNumber() {
		return page.getNumber() + 1;
	}

	public int getNumberOfElements() {
		return page.getNumberOfElements();
	}

	public int getSize() {
		return page.getSize();
	}

	public Sort getSort() {
		return page.getSort();
	}

	public long getTotalElements() {
		return page.getTotalElements();
	}

	public int getTotalPages() {
		return page.getTotalPages();
	}

	public boolean hasContent() {
		return page.hasContent();
	}

	public boolean hasNext() {
		return page.hasNext();
	}

	public boolean hasPrevious() {
		return page.hasPrevious();
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public Iterator<T> iterator() {
		return page.iterator();
	}

	public <S> Page<S> map(Converter<? super T, ? extends S> arg0) {
		return page.map(arg0);
	}

	public Pageable nextPageable() {
		return page.nextPageable();
	}

	public Pageable previousPageable() {
		return page.previousPageable();
	}

}
