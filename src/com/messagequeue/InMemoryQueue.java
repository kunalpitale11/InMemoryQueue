package com.messagequeue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import com.exceptions.TopicAlreadyPresent;
import com.models.*;

public class InMemoryQueue {

	private Map<String, Topic> topics;
	private Set<Subscription> subscriptions;
	private SubscriptionModel subscriptionModel;
	private KeyBasedExecutor keyBasedExecutor = new KeyBasedExecutor(10);

	// Constructor
	public InMemoryQueue(SubscriptionModel subscriptionModel) {
		topics = new ConcurrentHashMap<>();
		this.subscriptionModel = subscriptionModel;
		this.subscriptions = new HashSet<>();
	}

	public void publish(Message message, Topic topic) {
		keyBasedExecutor.submit(topic.getName(), () -> {
			topics.get(topic.getName()).addMessage(message);
			// check all subscriber and put send message if any
			// Code for Pushing Message to Subscriber

			for (Subscription subscription : subscriptions) {
				if (subscription.getTopic().equals(topic.getName()))
					keyBasedExecutor.submit(
							subscription.getTopic().getName().concat(subscription.getSubscriber().getId()), () -> {
								subscription.sendMessage(message);
							});
			}

		});

	}

	public CompletionStage<Message> poll(Subscription subscription) {
		return keyBasedExecutor.get(subscription.getTopic().getName().concat(subscription.getSubscriber().getId()), () ->{
			Message message = subscription.getTopic().getMessage(subscription.getLastOffset());
			subscription.incrementOffset();
			return message;
		});
	}

	public Topic addTopic(String name) {
		if (topics.containsKey(name))
			throw new TopicAlreadyPresent();
		topics.put(name, new Topic(name));
		System.out.println("Topic created -> " + name);
		return topics.get(name);
	}

	public void subscribe(Subscriber subscriber, Topic topic, SubscribePattern pattern, Integer randomOffsetIfReqired) {
		// Handle multiple subscription
		if (!pattern.equals(SubscribePattern.RANDOM))
			randomOffsetIfReqired = null;
		Subscription subscription = new Subscription(topic, randomOffsetIfReqired, subscriber);
		subscriptions.add(subscription);
		System.out.println("Subscription created -> " + subscription.toString());
	}

	public void resetOffset(Subscription subscription, Integer offset) {
		subscription.setLastOffset(offset);
	}

}
