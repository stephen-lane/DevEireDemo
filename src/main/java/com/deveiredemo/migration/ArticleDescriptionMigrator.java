package com.deveiredemo.migration;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;

import com.deveiredemo.model.Article;

public class ArticleDescriptionMigrator extends AbstractMigrator {

    @Override
    public String getName() {
        return "Article (Promoted Title/Description) Migrator";
    }

    @Override
    public String getDescription() {
        return "Forgot to copy over the promoted title and description in the original article migrator.";
    }

    @Override
    protected void doMigrate() {

        Query<Object> foreignQuery = getMainForeignQuery("com.deveiredemo.Story");
        for (Object foreignObject : foreignQuery.iterable(0)) {

            boolean didUpdate = false;

            State foreignState = State.getInstance(foreignObject);

            Article article = find(Article.class, foreignState);

            if (article != null) {
                State state = article.getState();

                Object promoTitle = foreignState.getByPath("promoTitle");
                Object promoDescription = foreignState.getByPath("promoDescription");

                if (promoTitle != null || promoDescription != null) {

                    state.putByPath("jsg.promotable.title", promoTitle);
                    state.putByPath("jsg.promotable.description", promoDescription);

                    didUpdate = true;
                }

                // add it to the save unsafely queue.
                if (didUpdate) {
                    saveUnsafely(article);
                }
            }
        }
    }
}
