package com.deveiredemo.model;

import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PaginatedResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A static list of {@link Promo}s within a mixed promo list.
 */
@Recordable.DisplayName("Static List")
@Recordable.Embedded
public class PromoListMixedItemStatic extends PromoListMixedItem {

    private List<Promo> items;

    public PromoListMixedItemStatic() {
    }

    public PromoListMixedItemStatic(List<Promo> items) {
        this.items = items;
    }

    /**
     * @return the list of promo items.
     */
    public List<Promo> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    /**
     * Sets the list of promos.
     *
     * @param items the promo items to set.
     */
    public void setItems(List<Promo> items) {
        this.items = items;
    }

    @Override
    public final List<Promotable> getContent() {

        List<Promo> items = PromoList.removeEmptyContent(getItems());
        if (items != null) {
            return new ArrayList<>(items);

        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public final PaginatedResult<Promotable> getPaginatedContent(long offset, int limit) {
        return new PaginatedResult<>(offset, limit, getContent());
    }

    @Override
    public int getMaximumContentLength() {
        return getItems().size();
    }

    @Override
    public int getMinimumContentLength() {
        return getItems().size();
    }

    @Override
    protected boolean applyFilter(Predicate predicate) {
        return false;
    }

    @Override
    public String getLabel() {
        int size = getContent().size();
        return size + " Item" + (size != 1 ? "s" : "");
    }
}
