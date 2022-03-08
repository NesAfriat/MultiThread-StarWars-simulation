package bgu.spl.mics;


import bgu.spl.mics.application.messages.AttackEvent;


import java.util.HashMap;

import java.util.Queue;
import bgu.spl.mics.MicroService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	HashMap<String, BlockingQueue<Message>> msQueue;
	HashMap<Class<? extends Event>, BlockingQueue<MicroService>> eventsSubscribers;
	HashMap<Class<? extends Broadcast>, BlockingQueue<MicroService>> broadcastSubscribers;
	HashMap<Event, Future> eventsFuture;
	Object newMessage, existEvent, existBroadcast;

	/* Important notes - data structures to implement
    broadcastSubscribers - keeps the existed broadcasts with the participated microServices
    eventSubscribers - keeps the types of event with the microservice type who can handle it
    msQueue - keeps the registered microservices with their queue of waiting messages
    eventsFurture - keeps every event with its associated future
     */
	private static MessageBusImpl mb = new MessageBusImpl();;

	public static MessageBusImpl getMessegeBus() {
		return mb;
	}


	private MessageBusImpl() {
		eventsFuture = new HashMap<Event, Future>();
		msQueue = new HashMap<String, BlockingQueue<Message>>();
		eventsSubscribers = new HashMap<Class<? extends Event>, BlockingQueue<MicroService>>();
		broadcastSubscribers = new HashMap<Class<? extends Broadcast>, BlockingQueue<MicroService>>();
		mb = this;
		existBroadcast=new Object();
		existEvent=new Object();
		newMessage=new Object();
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
			synchronized (existEvent) {
				if (!eventsSubscribers.containsKey(type))
					eventsSubscribers.put(type, new LinkedBlockingQueue<>());
				if (!eventsSubscribers.get(type).contains(m))
					eventsSubscribers.get(type).add(m);
				existEvent.notifyAll();
			}
	}


	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
			synchronized (existBroadcast) {
				if (!broadcastSubscribers.containsKey(type))
					broadcastSubscribers.put(type, new LinkedBlockingQueue<>());
				broadcastSubscribers.get(type).add(m);
				existBroadcast.notifyAll();
			}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		eventsFuture.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) throws InterruptedException {
		boolean exist = broadcastSubscribers.containsKey(b.getClass());
		if(!exist) {
			synchronized (existBroadcast) {
				while (!exist) {
					existBroadcast.wait();
					exist = broadcastSubscribers.containsKey(b.getClass());
				}
			}
		}

			for (MicroService micro : broadcastSubscribers.get(b.getClass()))
				msQueue.get(micro.getName()).add(b);
		synchronized (newMessage)
		{
			newMessage.notifyAll();
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) throws InterruptedException {
		//Take care about the case that nobody subscribed to the specific event yet.
		if(!eventsSubscribers.containsKey(e.getClass()) || eventsSubscribers.get(e.getClass()).size() < 2||!eventsSubscribers.containsKey(e.getClass()) || eventsSubscribers.get(e.getClass()).size() == 0) {
			synchronized (existEvent) {
				if (e.getClass().equals(AttackEvent.class)) {
					while (!eventsSubscribers.containsKey(e.getClass()) || eventsSubscribers.get(e.getClass()).size() < 2) {
						existEvent.wait();
					}
				} else {
					while (!eventsSubscribers.containsKey(e.getClass()) || eventsSubscribers.get(e.getClass()).size() == 0) {
						existEvent.wait();
					}
				}
			}
		}

			MicroService micro = eventsSubscribers.get(e.getClass()).remove();
			msQueue.get(micro.getName()).add(e);
			eventsSubscribers.get(e.getClass()).add(micro);//was here AttackEvent.class
			Future<T> associatedFuture = new Future<T>();
			eventsFuture.put(e, associatedFuture);
		synchronized (newMessage) {
			newMessage.notifyAll();
		}
			return associatedFuture;
		}


	@Override
	public void register(MicroService m) {
			msQueue.put(m.getName(), new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		msQueue.remove(m.getName());
		for (Queue<MicroService> q : eventsSubscribers.values()) {
			q.remove(m);
		}
		for (Queue<MicroService> q : broadcastSubscribers.values()) {
			q.remove(m);
		}

	}
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		///It is suppose to be blocking method!!!!
		synchronized (newMessage) {
			boolean WeDontHaveMessage = !msQueue.containsKey(m.getName()) || msQueue.get(m.getName()).isEmpty();
			while (WeDontHaveMessage) {
				newMessage.wait();
				WeDontHaveMessage = !msQueue.containsKey(m.getName()) || msQueue.get(m.getName()).isEmpty();
			}
		}
		Queue<Message> messages = msQueue.get(m.getName());
		Message remove = messages.remove();
		return (remove);
	}


}
