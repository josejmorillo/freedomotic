/**
 *
 * Copyright (c) 2009-2014 Freedomotic team http://freedomotic.com
 *
 * This file is part of Freedomotic
 *
 * This Program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Freedomotic; see the file COPYING. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.freedomotic.bus;

import com.freedomotic.app.Freedomotic;
import com.freedomotic.app.Profiler;
import com.freedomotic.util.UidGenerator;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 * {@link MessageListener} implementation (former AbstractBusConnector class)
 * <p>
 * Receives an {@link ObjectMessage} (it can be an event or a command) and sends
 * it to his {@link BusConsumer}
 * <p>
 * This is the bus hook for any {@link BusConsumer} that should register itself
 * in this listener.
 *
 * @author Freedomotic Team
 *
 * @see BusConsumer
 */
public class BusMessagesListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(BusMessagesListener.class.getName());

    private BusService busService;

    private BusConsumer messageHandler;

    // A listener can consume from multiple sources
    private Set<MessageConsumer> consumers = new HashSet<MessageConsumer>();

    /**
     * Constructor
     *
     * @param busConsumer
     * @param busService
     */
    @Inject
    public BusMessagesListener(BusConsumer busConsumer, BusService busService) {
        if (busService == null) {
            throw new IllegalArgumentException("Bus service cannot be not null");
        }
        this.messageHandler = busConsumer;
        this.busService = busService;

        if (busService == null) {
            throw new IllegalStateException("A message listener must have a working bus link");
        }
        if (busConsumer == null) {
            throw new IllegalStateException("A message listener must have an attached consumer");
        }
    }

    /**
     * Passes a message to the listener
     *
     * @param message
     */
    @Override
    public final void onMessage(Message message) {

        Profiler.incrementReceivedEvents();

        if (message instanceof ObjectMessage) {
            final ObjectMessage objectMessage = (ObjectMessage) message;
            messageHandler.onMessage(objectMessage);
        } else {

            LOG.severe("Message received by " + this.getClass().getSimpleName()
                    + " is not an object message, is a "
                    + message.getClass().getCanonicalName());
            if (message instanceof TextMessage) {
                TextMessage text = (TextMessage) message;
                try {
                    LOG.severe(text.getText());
                } catch (JMSException ex) {
                    LOG.log(Level.SEVERE, "Error while receiving a text message", ex);
                }
            }
        }
    }

    /**
     * Registers on a command queue
     *
     * @param queueName Queue name
     */
    public void consumeCommandFrom(String queueName) {

        try {
            Queue queue = busService.getReceiveSession().createQueue(queueName);
            MessageConsumer consumer = busService.getReceiveSession().createConsumer(queue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            LOG.severe(Freedomotic.getStackTraceInfo(e));
        }
    }

    /**
     * Registers on a event topic. It is a Virtual Topic in activemq lingo
     *
     * @param topicName
     */
    public void consumeEventFrom(String topicName) {
        try {

            final String virtualTopicName
                    = "Consumer." + UidGenerator.getNextStringUid() + ".VirtualTopic."
                    + topicName;

            Queue queue = busService.getReceiveSession().createQueue(virtualTopicName);
            MessageConsumer consumer = busService.getReceiveSession().createConsumer(queue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            LOG.severe(Freedomotic.getStackTraceInfo(e));
        }
    }

    /**
     * Subscribes a messaging topic. The message will be received by ALL the
     * subscribers. It's not a virtual topic as in consumeEventFrom(). DO NOT USE
     * IT IF YOU ARE NOT AWARE OF THE CONSEQUENCES. USE consumeEventFrom() instead.
     *
     * @param topicName
     */
    public void subscribeCrossInstanceEvents(String topicName) {
        try {
            Topic topic = busService.getReceiveSession().createTopic("VirtualTopic." + topicName);
            //TODO: add a selector for provenance field which should be "not from current instance"
            MessageConsumer consumer = busService.getReceiveSession().createConsumer(topic);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            LOG.severe(Freedomotic.getStackTraceInfo(e));
        }
    }

    /**
     * Unsubscribes from all messaging channels (topics and queues)
     * <br>
     * (invocations should be life cycle managed)
     */
    public void unsubscribe() {
        try {
            while (consumers.iterator().hasNext()) {
                MessageConsumer consumer = consumers.iterator().next();
                LOG.info("Closing message consumer " + consumer.toString());
                consumer.close();
            }
        } catch (JMSException ex) {
            Logger.getLogger(BusMessagesListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
