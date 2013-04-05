package com.ggierlik.ebookdeals;

import java.io.Serializable;

public class Book implements Serializable {

	private String publisher;
	private String offer;
	private String url;

	private static final long serialVersionUID = 1L;
	
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Book(String _publisher, String _offer, String _url) {
		publisher = _publisher;
		offer = _offer;
		url = _url;
	}
}
