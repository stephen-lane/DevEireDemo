package com.deveiredemo.model;

import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PaginatedResult;

import java.util.ArrayList;
import java.util.List;

@Recordable.DisplayName("Content List (Shared)")
@Recordable.Embedded
public class PromoListShared extends PromoList {

    @DisplayName("Content List")
    @Required
    private PromoList delegate;

    public PromoList getDelegate() {
        return delegate;
    }

    public void setDelegate(PromoList delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Promotable> getContent() {
        return delegate != null ? delegate.getContent() : new ArrayList<>();
    }

    @Override
    public PaginatedResult<Promotable> getPaginatedContent(long offset, int limit) {
        return delegate != null ? delegate.getPaginatedContent(offset, limit) : PaginatedResult.<Promotable>empty();
    }

    @Override
    public int getMaximumContentLength() {
        return delegate != null ? delegate.getMaximumContentLength() : 0;
    }

    @Override
    public int getMinimumContentLength() {
        return delegate != null ? delegate.getMinimumContentLength() : 0;
    }

    @Override
    public boolean applyFilter(Predicate predicate) {
        return delegate != null && delegate.applyFilter(predicate);
    }
}
