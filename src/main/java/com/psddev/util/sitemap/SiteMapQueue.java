package com.psddev.util.sitemap;

import com.psddev.dari.util.AsyncQueue;

import java.util.concurrent.CountDownLatch;

final class SiteMapQueue {

    private AsyncQueue<SiteMapEntry> queue;

    private SiteMapEntryConsumer consumer;

    private CountDownLatch latch;

    public SiteMapQueue(AsyncQueue<SiteMapEntry> queue, SiteMapEntryConsumer consumer, CountDownLatch latch) {

        if (queue != null) {
            this.queue = queue;
        } else {
            throw new IllegalStateException("Site Map Entry queue is required!");
        }

        if (consumer != null) {
            this.consumer = consumer;
        } else {
            throw new IllegalStateException("Site Map Entry Consumer is required!");
        }

        if (latch != null) {
            this.latch = latch;
        } else {
            throw new IllegalStateException("CountDown Latch is required!");
        }
    }

    public AsyncQueue<SiteMapEntry> getQueue() {
        return queue;
    }

    public SiteMapEntryConsumer getConsumer() {
        return consumer;
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
