package com.deveiredemo.model;

/**
 * Site wide constants.
 */
public final class Constants {

    private Constants() {
    }

    public static final String TOOL_KEY = "jsg-cms";

    /** 3 letter app key */
    public static final String APP_KEY = "jsg";

    /** Standard note for internal CMS fields. */
    public static final String TOOL_UI_NOTE_CMS_USE_ONLY = "For CMS use only.";

    /** The JSON data value for an enhancement that is left aligned. */
    public static final String RICH_TEXT_ALIGNMENT_LEFT = "left";

    /** The JSON data value for an enhancement that is right aligned. */
    public static final String RICH_TEXT_ALIGNMENT_RIGHT = "right";

    /** The JSON data value for an enhancement whose alignment is set to full width. */
    public static final String RICH_TEXT_ALIGNMENT_FULL = "full";

    public static final String EMPTY_PLACEHOLDER_KEY = ".";

    public static final String OEMBED_AUTHOR_NAME_KEY = "author_name";
    public static final String OEMBED_AUTHOR_URL_KEY = "author_url";
    public static final String OEMBED_HEIGHT_KEY = "height";
    public static final String OEMBED_HTML_KEY = "html";
    public static final String OEMBED_MAXIMUM_WIDTH_KEY = "_maximumWidth";
    public static final String OEMBED_MAXIMUM_HEIGHT_KEY = "_maximumHeight";
    public static final String OEMBED_PROVIDER_NAME_KEY = "provider_name";
    public static final String OEMBED_PROVIDER_URL_KEY = "provider_url";
    public static final String OEMBED_THUMBNAIL_HEIGHT_KEY = "thumbnail_height";
    public static final String OEMBED_THUMBNAIL_URL_KEY = "thumbnail_url";
    public static final String OEMBED_THUMBNAIL_WIDTH_KEY = "thumbnail_width";
    public static final String OEMBED_TITLE_KEY = "title";
    public static final String OEMBED_TYPE_KEY = "type";
    public static final String OEMBED_URL_KEY = "_url";
    public static final String OEMBED_VERSION_KEY = "version";
    public static final String OEMBED_WIDTH_KEY = "width";

    public static final String STORAGE_ITEM_METADATA_WIDTH_KEY = "width";
    public static final String STORAGE_ITEM_METADATA_HEIGHT_KEY = "height";

    public static final String STANDARD_IMAGE_SIZE_PROMO_SMALL_2_1 = "promo-small"; // 2:1 - 370x185
    public static final String STANDARD_IMAGE_SIZE_PROMO_MEDIUM_2_1 = "promo-medium"; // 2:1 - 565x280 (560x280)
    public static final String STANDARD_IMAGE_SIZE_PROMO_LARGE_7_2 = "promo-large"; // 7:2 - 1160x330 (1155x330)
    public static final String STANDARD_IMAGE_SIZE_PROMO_SUPER_3_1 = "promo-super"; // 3:1 - (1260x420)

    public static final String STANDARD_IMAGE_SIZE_PROMO_LARGE_MOBILE_8_5 = "promo-large-mobile"; // 8:5 - 320x200
    public static final String STANDARD_IMAGE_SIZE_PROMO_SUPER_MOBILE_8_5 = "promo-super-mobile"; // 8:5 - 320x200

    public static final String STANDARD_IMAGE_SIZE_GRID_TALL = "photo-grid-tall"; // 1:~2 - 160x320 (155x325)
    public static final String STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1 = "photo-grid-small-square"; // 1:1 - 160x160 (155x155)
    public static final String STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1 = "photo-grid-large-square"; // 1:1 - 320x320 (325x325)
    public static final String STANDARD_IMAGE_SIZE_GRID_WIDE = "photo-grid-wide"; // ~2:1 - 320x160 (325x155)
    public static final String STANDARD_IMAGE_SIZE_ARTICLE_INLINE_PORTRAIT_1_2 = "article-inline-portrait"; // 1:2 - 160x320
    public static final String STANDARD_IMAGE_SIZE_ARTICLE_INLINE_LANDSCAPE_2_1 = "article-inline-landscape"; // 2:1 - 800x440
    public static final String STANDARD_IMAGE_SIZE_ARTICLE_LEAD = "article-lead"; // 2:1 - 1270x635

    public static final String AJAX_ENDPOINT_PHOTO_FALL = "/ajax/photofall";
    public static final String AJAX_ENDPOINT_GALLERY_FALL = "/ajax/galleryfall";
    public static final String AJAX_ENDPOINT_PHOTO_FALL_SLIDE = "/ajax/photofall-slide";
    public static final String AJAX_ENDPOINT_PROMO_MODULE_LOAD_MORE = "/ajax/promo-module-load-more";
    public static final String AJAX_ENDPOINT_TOURNAMENT_SEASON_SCHEDULE = "/ajax/tournament-season-schedule";

    public static final String TOOL_ENDPOINT_GOLF_MATCH_PLAY_SCORECARD_FIELD = "/golfMatchPlayScorecardField";
    public static final String TOOL_ENDPOINT_GOLF_TOURNAMENT_SCORECARD_FIELD = "/golfTournamentScorecardField";
}
