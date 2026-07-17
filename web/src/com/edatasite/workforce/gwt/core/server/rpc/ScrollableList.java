package com.edatasite.workforce.gwt.core.server.rpc;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * web
 * Created by Sher on 12/9/2015.
 */
public class ScrollableList<T> implements Iterable<T> {
    private ScrollableResults results;
    private int i = 0;

    public ScrollableList(Criteria query, int fetchSize) {
        query.setFetchSize(fetchSize);
        this.results = query.scroll(ScrollMode.FORWARD_ONLY);
    }

    public ScrollableList(Criteria query) {
        this(query, 500);
    }

    public int getIndex() {
        return i - 1;
    }

    public boolean next() {
        i++;
        return results.next();
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) results.get(0);
    }

    public List<T> toList() {
        if (i > 0)
            throw new RuntimeException("Cannot convert to list, already iterate");

        List<T> result = new ArrayList<>();
        for (T e : this)
            result.add(e);

        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return ScrollableList.this.next();
            }

            @Override
            public T next() {
                return ScrollableList.this.get();
            }

            @Override
            public void remove() {
                throw new RuntimeException("Cannot remove");
            }
        };
    }
}
