package at.fhj.swd13.pse.controller;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author florian.genser
 *
 */
public class FeedContent {

	private final String text;

	public FeedContent(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
