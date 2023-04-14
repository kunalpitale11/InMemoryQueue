package com.models;

import java.util.List;

public class Group extends Subscriber{
	
	private List<Subscriber> subscribers;

	public Group(List<Subscriber> subscribers) {
		super();
		this.subscribers = subscribers;
	}
	
	public void addSubscriber(Subscriber subscriber) {
		subscribers.add(subscriber);
	}
}
