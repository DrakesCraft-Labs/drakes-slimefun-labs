package io.github.sefiraat.networks.slimefun.network.grid;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GridCache {

    @Setter
    @Getter
    private int page;
    @Setter
    @Getter
    private int maxPages;
    @Nonnull
    private SortOrder sortOrder;
    @Nullable
    private String filter;

    public GridCache(int page, int maxPages, @Nonnull SortOrder sortOrder) {
        this.page = page;
        this.maxPages = maxPages;
        this.sortOrder = sortOrder;
    }

    @Nonnull
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(@Nonnull SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Nullable
    public String getFilter() {
        return filter;
    }

    public void setFilter(@Nullable String filter) {
        this.filter = filter;
    }

    public enum SortOrder {
        ALPHABETICAL,
        NUMBER
    }
}
