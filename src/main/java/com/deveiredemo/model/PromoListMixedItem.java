package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PaginatedResult;

import java.util.List;

/**
 * Base class for items that can go in a mixed promo list. Each mixed promo
 * item is generally ALSO itself a list. It provides both a paginated view into
 * its underlying list and a total list.
 */
@Recordable.Embedded
public abstract class PromoListMixedItem extends Content {

    /**
     * @return a list of all the promotable content in the promo list item.
     */
    public abstract List<Promotable> getContent();

    /**
     * @param offset the paginated result offset
     * @param limit the paginated result limit.
     * @return a paginated result for the promotable content in this promo list
     *      item based on the given {@code offset} and {@code limit}.
     */
    public abstract PaginatedResult<Promotable> getPaginatedContent(long offset, int limit);

    /**
     * @return the maximum number of items that could potentially be returned
     *      from this promo list.
     */
    public abstract int getMaximumContentLength();

    /**
     * @return the minimum number of items that could potentially be returned
     *      from this promo list.
     */
    public abstract int getMinimumContentLength();

    /**
     * Applies a filter to the content returned from {@link #getContent()} or
     * {@link #getPaginatedContent(long, int)}. It is at the discretion of the
     * underlying implementation whether to *ACTUALLY* apply the filter or not,
     * but if it will NOT, it must return {@code false}.
     *
     * @param predicate the predicate to use as a filter on the content returned.
     * @return true if the filter was applied, false otherwise.
     */
    protected abstract boolean applyFilter(Predicate predicate);
}

