package com.deveiredemo.migration;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.model.Article;
import com.deveiredemo.model.ContentLink;
import com.deveiredemo.model.ExternalLink;

public class ArticleLinkMigrator extends AbstractMigrator {

    @Override
    public String getName() {
        return "Article (Original Content Link) Migrator";
    }

    @Override
    public String getDescription() {
        return "2nd pass of Article Migrator that updates existing Article's"
                + " \"Original Content\" Link field which can point to other Articles.";
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

                Object foreignOriginalContent = foreignState.getByPath("originalContent");

                if (foreignOriginalContent instanceof Recordable) {

                    State foreignOriginalContentState = State.getInstance(foreignOriginalContent);

                    String text = ObjectUtils.to(String.class, foreignOriginalContentState.getByPath("text"));
                    String url = ObjectUtils.to(String.class, foreignOriginalContentState.getByPath("url"));

                    if (!StringUtils.isBlank(url)) {

                        ExternalLink externalLink = new ExternalLink();
                        externalLink.setText(text);
                        externalLink.setUrl(url);

                        state.putByPath("originalContent", externalLink);
                        didUpdate = true;

                    } else {

                        Object foreignLinkedContent = foreignOriginalContentState.getByPath("linkedContent");
                        if (foreignLinkedContent instanceof Recordable) {

                            MigratedContent linkedContent = find(MigratedContent.class, State.getInstance(foreignLinkedContent));

                            if (linkedContent instanceof Content) {

                                ContentLink contentLink = new ContentLink();
                                contentLink.setText(text);
                                contentLink.setContent((Content) linkedContent);

                                state.putByPath("originalContent", contentLink);
                                didUpdate = true;
                            }
                        }
                    }
                }

                // add it to the save unsafely queue.
                if (didUpdate) {
                    saveUnsafely(article);
                }
            }
        }
    }
}
