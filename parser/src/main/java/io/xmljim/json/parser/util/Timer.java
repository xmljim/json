/*
 * Copyright 2021 Jim Earley (xml.jim@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to
 * whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package io.xmljim.json.parser.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Timer<T> {
    private long accumulatedTime;
    private long currentStartMark;
    private final Map<T, Tick<T>> sequenceMap = new LinkedHashMap<>();

    private static final long NANO = 1L;
    private static final long MICRO = NANO * 1_000;
    private static final long MILLI = MICRO * 1_000;
    private static final long SECOND = MILLI * 1_000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;

    public T start(T subject) {
        this.currentStartMark = System.nanoTime();
        Tick<T> tick = sequenceMap.getOrDefault(subject, new Tick<>(subject));
        tick.start(this.currentStartMark);
        sequenceMap.putIfAbsent(subject, tick);

        return subject;
    }

    public T stop(T subject) {
        long end = System.nanoTime();
        sequenceMap.get(subject).stop(end);

        //accumulatedTime = getSequences().stream().mapToLong(tick -> tick.duration).sum();
        accumulatedTime += end - currentStartMark;
        currentStartMark = 0;
        return subject;
    }

    public List<Tick<T>> getSequences() {
        return sequenceMap.values().stream().toList();
    }

    public long getAccumulatedTime() {
        return accumulatedTime;
    }

    public double get(TimeUnit timeUnit) {
        return switch (timeUnit) {
            case NANOSECONDS -> (double) accumulatedTime / NANO;
            case MICROSECONDS -> (double) accumulatedTime / MICRO;
            case MILLISECONDS -> (double) accumulatedTime / MILLI;
            case SECONDS -> (double) accumulatedTime / SECOND;
            case MINUTES -> (double) accumulatedTime / MINUTE;
            case HOURS -> (double) accumulatedTime / HOUR;
            case DAYS -> (double) accumulatedTime / DAY;
        };
    }

    public static class Tick<T> {
        private long startMark;
        private long endMark;
        private final T subject;
        private long count;
        private long duration;

        public Tick(T subject) {
            this.subject = subject;
        }

        public void start(long nanoTime) {
            this.startMark = nanoTime;
        }

        public void stop(long nanoTime) {
            this.duration = nanoTime - startMark;
            this.endMark = nanoTime;
            count++;
        }

        public long getStartMark() {
            return startMark;
        }

        public long getEndMark() {
            return endMark;
        }

        public T getSubject() {
            return subject;
        }

        public long duration() {
            return duration;
        }

        public long count() {
            return count;
        }

        public double get(TimeUnit timeUnit) {
            return switch (timeUnit) {
                case NANOSECONDS -> (double) duration() / NANO;
                case MICROSECONDS -> (double) duration() / MICRO;
                case MILLISECONDS -> (double) duration() / MILLI;
                case SECONDS -> (double) duration() / SECOND;
                case MINUTES -> (double) duration() / MINUTE;
                case HOURS -> (double) duration() / HOUR;
                case DAYS -> (double) duration() / DAY;
            };
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Tick{");
            sb.append("startMark=").append(startMark);
            sb.append(", endMark=").append(endMark);
            sb.append(", subject=").append(subject);
            sb.append(", count=").append(count);
            sb.append(", duration=").append(get(TimeUnit.SECONDS)).append(" seconds");
            sb.append('}');
            return sb.toString();
        }
    }


}
