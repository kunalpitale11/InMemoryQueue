package com.models;

public class Subscription {
	private Topic topic;
	private Integer lastOffset;
	private Subscriber subscriber;

	public Subscription(Topic topic, Integer lastOffset, Subscriber subscriber) {
		super();
		this.topic = topic;
		if (lastOffset == null)
			lastOffset = 0;
		else
			this.lastOffset = lastOffset;
		this.subscriber = subscriber;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Subscription) {
			Subscription sub = (Subscription) obj;
			if(sub.topic.getName().equals(this.topic.getName()) && sub.subscriber.getId().equals(this.subscriber.getId()))
				return true;
		}
		return false;
		
	}

	@Override
	public String toString() {
		return "Subscription [topic=" + topic.getName() + ", subscriber=" + subscriber.getId() + "]";
	}

	public Integer getLastOffset() {
		return lastOffset;
	}

	public void setLastOffset(Integer lastOffset) {
		this.lastOffset = lastOffset;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public void incrementOffset() {
		lastOffset+=1;
		
	}

	
}
