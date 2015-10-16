package com.deveiredemo.model;

import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PaginatedResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A mixed list of dynamic query driven promos and statically selected promos.
 */
@Recordable.DisplayName("Content List (Mixed)")
public class PromoListMixed extends PromoList {

    @DisplayName("Content List Items")
    private List<PromoListMixedItem> listItems;

    public PromoListMixed() {
    }

    /**
     * @return the list of promo list items.
     */
    public List<PromoListMixedItem> getListItems() {
        if (listItems == null) {
            listItems = new ArrayList<>();
        }
        return listItems;
    }

    /**
     * Sets the promo list items for this promo list.
     *
     * @param listItems the promo list items to set.
     */
    public void setListItems(List<PromoListMixedItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public final List<Promotable> getContent() {

        List<Promotable> allContent = new ArrayList<>();

        for (PromoListMixedItem listItem : getListItems()) {
            List<Promotable> content = listItem.getContent();

            if (content != null) {
                allContent.addAll(content);
            }
        }

        return allContent;
    }

    // TODO: Still need to implement - Needs a more efficient implementation.
    @Override
    public final PaginatedResult<Promotable> getPaginatedContent(long offset, int limit) {
        return new PaginatedResult<>(offset, limit, getContent());
    }

    @Override
    public int getMaximumContentLength() {

        int maxCount = 0;

        for (PromoListMixedItem item : getListItems()) {
            maxCount += item.getMaximumContentLength();
        }

        return maxCount;
    }

    @Override
    public int getMinimumContentLength() {

        int minCount = 0;

        for (PromoListMixedItem item : getListItems()) {
            minCount += item.getMinimumContentLength();
        }

        return minCount;
    }

    @Override
    public boolean applyFilter(Predicate predicate) {

        boolean isApplied = false;

        for (PromoListMixedItem item : getListItems()) {
            isApplied = item.applyFilter(predicate) || isApplied;
        }

        return isApplied;
    }
}

