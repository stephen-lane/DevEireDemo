package com.deveiredemo.migration;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;

import com.deveiredemo.model.Article;
import com.deveiredemo.model.ArticleLead;
import com.deveiredemo.model.Author;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Tag;

public class ArticleMigrator extends AbstractMigrator {

    @Override
    public String getName() {
        return "Article Migrator";
    }

    @Override
    public String getDescription() {
        return "Migrates Story objects from the legacy DB.";
    }

    @Override
    protected void doMigrate() {

        Query<Object> foreignQuery = getMainForeignQuery("com.deveiredemo.Story");
        for (Object foreignImage : foreignQuery.iterable(0)) {

            State foreignState = State.getInstance(foreignImage);

            Article article = findOrCreate(Article.class, foreignState);

            State state = article.getState();

            if (overwriteExisting() || state.isNew()) {

                // field mappings
                state.putByPath("headline", foreignState.getByPath("headline"));
                state.putByPath("mobileHeadline", foreignState.getByPath("mobileHeadline"));
                state.putByPath("originalSource", foreignState.getByPath("originalSource"));

                state.putByPath("body", foreignState.getByPath("body"));
                state.putByPath("perspectiveText", foreignState.getByPath("perspectiveText"));

                Object foreignLeadImage = foreignState.getByPath("leadImage");
                if (foreignLeadImage instanceof Recordable) {
                    Image leadImage = find(Image.class, State.getInstance(foreignLeadImage));

                    if (leadImage != null) {

                        ArticleLead lead = new ArticleLead();

                        State leadState = lead.getState();
                        leadState.putByPath("image", leadImage);
                        leadState.putByPath("captionOverride", leadImage.getState().getByPath("oldCaption"));

                        state.putByPath("lead", lead);
                    }
                }

                Object foreignAuthor = foreignState.getByPath("author");
                if (foreignAuthor instanceof Recordable) {
                    state.putByPath("author", find(Author.class, State.getInstance(foreignAuthor)));
                }

                // TODO: Still need to handle: section record Section.

                // skipping originalContent record Link - Handled by the ArticleLinkMigrator.

                // skipping endModules list/record  Full Width Module

                state.putByPath("tags", convertForeignReferenceList(Tag.class, foreignState.getByPath("tags")));

                // copy permalinks from foreign object.
                setPermalinks(state, foreignState);

                // add it to the save queue.
                save(article);
            }
        }
    }
}
