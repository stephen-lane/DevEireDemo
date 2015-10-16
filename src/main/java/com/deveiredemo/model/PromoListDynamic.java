package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.DatabaseException;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PaginatedResult;
import com.psddev.dari.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * A query driven promo list. It is nearly identical in implementation to
 * {@link PromoListMixedItemDynamic}.
 *
 * @see PromoListMixedItemDynamic
 */
@Recordable.DisplayName("Content List (Dynamic)")
public class PromoListDynamic extends PromoList {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromoList.class);

    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 10;

    @ToolUi.Hidden
    protected long offset;

    protected int limit = DEFAULT_LIMIT;

    @ToolUi.Note("Checking this box enables pagination of this promo list, where the batch size is equal to the limit specified above.")
    protected boolean isLoadMore;

    @Required
    @Recordable.Embedded
    private Query<Promotable> query;

    private transient Predicate queryFilterPredicate;

    /**
     * @return the offset for the query.
     */
    public long getOffset() {
        if (offset <= 0) {
            offset = DEFAULT_OFFSET;
        }
        return offset;
    }

    /**
     * Sets the offset for the query.
     *
     * @param offset the offset to set.
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * @return the limit for the number of items returned from the query.
     */
    public int getLimit() {
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        return limit;
    }

    /**
     * Sets the limit for the query.
     *
     * @param limit the limit to set.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    /**
     * @return the query that powers the list of items in this dynamic promo list.
     */
    public Query<Promotable> getQuery() {
        return query;
    }

    /**
     * Sets the query for this dynamic promo list.
     *
     * @param query the query to set.
     */
    public void setQuery(Query<Promotable> query) {
        this.query = query;
    }

    /**
     * @return the query that powers the list of items in this dynamic promo
     *      list, but filtered by the tags predicate set from
     *      {@link #applyFilter(com.psddev.dari.db.Predicate)}.
     */
    private Query<Promotable> getFilteredQuery() {
        Query<Promotable> unfilteredQuery = getQuery();

        if (unfilteredQuery != null || queryFilterPredicate != null) {

            Query<Promotable> filteredQuery;

            if (unfilteredQuery == null) {
                // TODO: Need to make the sort configurable somehow.
                filteredQuery = Query.from(Promotable.class).sortDescending(Content.PUBLISH_DATE_FIELD);

            } else {
                filteredQuery = unfilteredQuery.clone();
            }

            if (queryFilterPredicate != null) {
                filteredQuery.and(queryFilterPredicate);
            }

            return filteredQuery;
        }

        return null;
    }

    /**
     * @return either filtered or unfiltered query based on whether the
     *      {@link #applyFilter(com.psddev.dari.db.Predicate)} method was
     *      caused prior to this invocation.
     */
    private Query<Promotable> getEffectiveQuery() {
        Query<Promotable> query;
        if (queryFilterPredicate != null) {
            query = getFilteredQuery();
        } else {
            query = getQuery();
        }

        Query<Promotable> effectiveQuery = getCleanedQuery(query);

        //SortOrder.filterQuery(effectiveQuery, sortOrder);

        return effectiveQuery;
    }

    @Override
    public List<Promotable> getContent() {
        Query<Promotable> effectiveQuery = getEffectiveQuery();
        return effectiveQuery != null ? effectiveQuery.select(getOffset(), getLimit()).getItems() : Collections.emptyList();
    }

    @Override
    public PaginatedResult<Promotable> getPaginatedContent(long offset, int limit) {

        Query<Promotable> effectiveQuery = getEffectiveQuery();

        if (effectiveQuery != null) {

            double timeout = JsgSite.getInstance().getDynamicQueryTimeout();
            effectiveQuery.timeout(timeout);

            try {
                return effectiveQuery.select(
                        offset + getOffset(),
                        Math.max(Math.min(limit, getLimit() - (int) offset), 0));

            } catch (DatabaseException e) {
                LOGGER.warn(e.getMessage(), e);
                return PaginatedResult.empty();
            }

        } else {
            return PaginatedResult.empty();
        }
    }

    @Override
    public int getMaximumContentLength() {
        return getLimit();
    }

    @Override
    public int getMinimumContentLength() {
        return 0;
    }

    @Override
    public boolean applyFilter(Predicate predicate) {
        if (getQuery() != null) {
            queryFilterPredicate = predicate;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getLabel() {
        String name = getName();
        if (!StringUtils.isBlank(name)) {
            return name;
        } else {
            int size = getLimit();
            return "Maximum of " + size + " Item" + (size != 1 ? "s" : "");
        }
    }
}
