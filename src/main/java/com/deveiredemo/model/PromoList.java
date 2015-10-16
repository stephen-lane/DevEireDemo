package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PaginatedResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of Promotable objects that allows editor flexibility when specifying
 * the content within the list, by either specifying individual objects or
 * specifying a query, or any combination thereof.
 */
@Recordable.DisplayName("Content List")
public abstract class PromoList extends Content {

    private String name;

    public String getName() {
        return name;
    }

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
    public abstract boolean applyFilter(Predicate predicate);

    /**
     * Fixes a brightspot bug where using Query as a field, when the query is executed
     * it places a '{}' in the query, causing the solr query to fail. This replaces the
     * '{}' with 'missing'
     * @param query the query to clean.
     * @param <T> the type of query.
     * @return the cleaned query.
     */
    protected static <T> Query<T> getCleanedQuery(Query<T> query) {
        if (query != null) {
            Query<T> clone = query.clone();

            // nothing to do here yet...

            return clone;
        }

        return null;
    }

    /**
     * Takes the list of items entered and removes any Promos that either have an empty URL
     * in the case of External Links or empty content in the case of an Internal Link.
     * @param items the items from which to remove empty content.
     * @return a list of "cleaned" Promos. Never {@code null}
     */
    protected static List<Promo> removeEmptyContent(List<Promo> items) {
        List<Promo> cleanPromos = new ArrayList<>();

        for (Promo item : items) {
            Link promoLink = item.getLink();

            if (promoLink instanceof ContentLink) {
                if (((ContentLink) promoLink).getContent() != null) {
                    cleanPromos.add(item);
                }
            } else if (promoLink instanceof ExternalLink) {
                if (((ExternalLink) promoLink).getUrl() != null) {
                    cleanPromos.add(item);
                }
            }
        }

        return cleanPromos;
    }
}
