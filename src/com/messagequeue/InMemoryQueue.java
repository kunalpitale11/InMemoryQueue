package com.messagequeue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.exceptions.TopicAlreadyPresent;
import com.models.*;

public class InMemoryQueue {

	private Map<String, Topic> topics;
	private Set<Subscription> subscriptions;
	private SubscriptionModel subscriptionModel;
	
	// Constructor
	public InMemoryQueue(SubscriptionModel subscriptionModel) {
		topics = new ConcurrentHashMap<>();
		this.subscriptionModel = subscriptionModel;
		this.subscriptions = new HashSet<>();
	}

	public void publish(Message message, Topic topic) {
		topics.get(topic.getName()).addMessage(message);
		// check all subscriber and put send message if any
		// Code for Pushing Message to Subscriber

		for(Subscription subscription : subscriptions) {
			if(subscription.getTopic().equals(topic.getName()))
				subscription.getSubscriber().sendMEssage(message);
		}
	}

	public Message poll(Subscription subscription) {
		Message message = subscription.getTopic().getMessage(subscription.getLastOffset());
		subscription.incrementOffset();
		return message;
	}

	public Topic addTopic(String name) {
		if(topics.containsKey(name))
			throw new TopicAlreadyPresent();
		topics.put(name, new Topic(name));
		System.out.println("Topic created -> "+ name);
		return topics.get(name);
	}

	public void subscribe(Subscriber subscriber, Topic topic, SubscribePattern pattern, Integer randomOffsetIfReqired) {
		// Handle multiple subscription
		Subscription subscription = new Subscription(topic, randomOffsetIfReqired, subscriber);
		subscriptions.add(subscription);
		System.out.println("Subscription created -> "+ subscription.toString());
	}
	
	public void resetOffset(Subscription subscription, Integer offset) {
		subscription.setLastOffset(offset);
	}

}
