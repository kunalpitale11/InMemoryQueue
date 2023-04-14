package com.models;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.exceptions.NoElementFoundException;

public class Topic {
	private String name;
	private List<Message> messages;

	public Topic(String name) {
		messages = new CopyOnWriteArrayList<>();
		this.name = name;
	}

	public Message getMessage(int index) {
		Message message = messages.get(index);
		if(message == null)	throw new NoElementFoundException();
		return message;
	}

	public String getName() {
		return name;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

}
