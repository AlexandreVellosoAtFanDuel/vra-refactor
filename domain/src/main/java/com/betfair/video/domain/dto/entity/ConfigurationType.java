package com.betfair.video.domain.dto.entity;

public enum ConfigurationType {
    IMPORT_ENABLED("import.enabled"),
    MAPPER_ENABLED("mapper.enabled"),
    MAPPER_INEXACT_ENABLED("mapper.inexact.enabled"),
    IMPORT_APPROVAL_STATUS("import.approval.status"),
    MAPPER_APPROVAL_STATUS("mapper.approval.status"),
    MAPPING_ALLOWABLE_DATES_DIFFERENCE("mapping.allowable.date.diff"),
    UNMAPPING_ALLOWABLE_DATES_DIFFERENCE("unmapping.allowable.date.diff"),
    MAPPER_IGNORED_COMPETITIONS("mapper.ignored.competitions"),
    LEAD_TIME("lead.time"),
    TRAIL_TIME("trail.time"),
    GEO_BLOCKING("geo.blocking"),
    NEW_MAPPER_ALLOWED_PROVIDER_IDS("new.mapper.allowed.provider.ids"),
    BBV_ENABLED("bbv.enabled"),
    FUNDING_ENABLED("funding.bbv.enabled"),
    FUNDING_MIN_BALANCE("funding.bbv.min.balance"),
    FUNDING_COMBINED_BALANCE_ENABLED("funding.combined.balance.enabled"),
    FUNDING_COMBINED_BALANCE_BETS_LIMIT("funding.combined.balance.bets.limit"),
    BBV_ACCUMULATIVE_STAKES_ENABLED("bbv.accumulative.stakes.enabled"),
    BBV_FBR_ONLY_STAKES_ENABLED("bbv.fbr.only.stakes.enabled"),
    BBV_FBR_SETTLED_ENABLED("bbv.fbr.settled.enabled"),
    ACCUMULATIVE_FBR_ENABLED("accumulative.fbr.enabled"),
    ACCUMULATIVE_LBR_ENABLED("accumulative.lbr.enabled"),
    ACCUMULATIVE_TOTE_UK_ENABLED("accumulative.tote.uk.enabled"),
    ACCUMULATIVE_TOTE_RSA_ENABLED("accumulative.tote.rsa.enabled"),
    BBV_FBR_ONLY_STAKES("bbv.fbr.only.stakes"),
    BBV_ACCUMULATIVE_STAKES("bbv.accumulative.stakes"),
    SIZE_RESTRICTION_WIDTH_PERCENTAGE("size.restriction.width.percentage"),
    SIZE_RESTRICTION_WIDTH_CENTIMETER("size.restriction.width.centimeter"),
    SIZE_RESTRICTION_WIDTH_PIXEL("size.restriction.width.pixel"),
    SIZE_RESTRICTION_HEIGHT_PERCENTAGE("size.restriction.height.percentage"),
    SIZE_RESTRICTION_HEIGHT_PIXEL("size.restriction.height.pixel"),
    SIZE_RESTRICTION_HEIGHT_CENTIMETER("size.restriction.height.centimeter"),
    SIZE_RESTRICTION_FULLSCREEN_ALLOWED("size.restriction.fullScreen.allowed"),
    SIZE_RESTRICTION_AIRPLAY_ALLOWED("size.restriction.airplay.allowed"),
    SIZE_RESTRICTION_ASPECT_RATIO("size.restriction.aspectratio"),
    SIZE_RESTRICTION_MAX_WIDTH("size.restriction.max.width"),
    SIZE_RESTRICTION_DEAFULT_WIDTH("size.restriction.default.width"),
    DEFAULT_VIDEO_QUALITY("default.video.quality"),
    DEFAULT_BUFFERING_INTERVAL("default.buffering.interval"),
    PREFERRED_STREAMING_FORMAT("stream.media.format"),
    HIGH_LATENCY_ICON_SIZE("high.latency.icon.size"),
    CSS_PATH("css.path"),
    CSS_DIFF("css.diff"),
    PROVIDER_EVENT_NAME_SEPARATOR("provider.event.name.separator"),
    INEXACT_AUTO_ACCEPT_THRESHOLD_SPECIAL_LOGIC("inexact.auto.accept.threshold.special.logic.match"),
    INEXACT_MIN_THRESHOLD_SPECIAL_LOGIC("inexact.min.threshold.special.logic.match"),
    INEXACT_AUTO_ACCEPT_THRESHOLD_FALLBACK("inexact.auto.accept.threshold.fallback.match"),
    INEXACT_MIN_THRESHOLD_FALLBACK("inexact.min.threshold.fallback.match"),
    INEXACT_AUTO_ACCEPT_THRESHOLD_FALLBACK_INTEGRAL("inexact.auto.accept.threshold.fallback.integral.match"),
    INEXACT_MIN_THRESHOLD_FALLBACK_INTEGRAL("inexact.min.threshold.fallback.integral.match"),
    INEXACT_EDIT_DISTANCE_SCORE_MULTIPLIER("inexact.edit.distance.score.multiplier"),
    INEXACT_TEAM_MODIFIERS("inexact.team.modifiers"),
    INEXACT_TEAM_MODIFIERS_FRAMING_SYMBOLS("inexact.team.modifiers.framing"),
    CURRENCY_CONVERSION("currency.conversion"),
    STREAM_TYPE_ENABLED("stream.type.enabled"),
    PADDOCK_STREAM_LENGTH_MINUTES("paddock.stream.length.minutes"),
    PROVIDER_SERVICE_COUNTRYCODE_MAPPING("provider.service.countrycode.mapping"),
    PROVIDER_SERVICE_VENUE_MAPPING("provider.service.venue.mapping"),
    COUNTRY_VENUE_RESTRICTION_RULE("country.venue.restriction.rule"),
    DEFAULT_COUNTRY_NAME("default.country.name"),
    RACING_SPORT_IDS("racing.sport.ids"),
    SUPPORTED_CONTENT_VIEWS("supported.content.views"),
    PROVIDER_PRIORITY_LIST("provider.priority.list"),
    PROVIDER_IGNORE_EVENT_NAME_TOKENS("provider.ignore.event.name.tokens"),
    PROVIDER_WATCH_AND_BET_VENUES("provider.watch.and.bet.racecources"),
    PROVIDER_DASH_SUPPORT_VENUES("provider.dash.support.racecourses"),
    PROVIDER_DASH_SUPPORT_SPORTTYPES("provider.dash.support.sport.types"),
    STREAM_DELAYING_ALLOWED_END_TIME_DELTA("stream.delaying.allowed.end.time.delta"),
    STREAM_DELAYING_END_TIME_PROLONGATION_DELTA("stream.delaying.end.time.prolongation.delta"),
    DELAY_START_UPDATE_SPORT_IDS("delay.start.update.sport.ids"),
    MULTI_MATCH_STREAM_EXTRACTION_ALLOWED("multi.match.stream.extraction.allowed"),
    PROVIDER_PARTICIPANT_SUPPORTED("provider.participant.supported"),
    PROVIDER_PARTICIPANT_MIN_MATCH("provider.participant.min.match"),
    PROVIDER_PARTICIPANT_ALLOWED_MARKET_TYPES("provider.participant.allowed.market.types"),
    VIDEO_PLAYER_CONFIG("video.player.config"),
    BRAND_ENABLED("brand.enabled"),
    BRAND_IMPORT_ENABLED("brand.import.enabled"),
    BRAND_EXPORT_ENABLED("brand.export.enabled"),
    ON_TV_EXPORT_ENABLED("on.tv.export.enabled"),
    BRANDS_GROUPED_BY_COMMON_PROVIDER_CREDS("brands.grouped.by.common.provider.creds"),
    USE_KAFKA_AS_SOURCE("use.kafka.as.source"),
    CATALOGUE_STREAM_STORE_FOR_EXPORT_ENABLED("catalogue.stream.store.events.for.export.enabled"),
    VISUALIZATION_WIDGET_NAME("visualization.widget.name"),
    BLOCKED_APPKEYS_FOR_PROVIDER("blocked.appkeys.for.provider"),
    BLOCKED_APPKEYS_FOR_VENUES("blocked.appkeys.for.venues"),
    VAS_BLOCKED_SPORT_COMPETITIONS("vas.blocked.sport.competitions"),
    WEBRTC_CONTINUOUS_ACCESS_ACCOUNT_IDS("webrtc.continuous.access.account.ids"),
    SPORT_COMPETITIONS_IMPORT_ENABLED("sport.competitions.import.enabled"),
    LOW_LATENCY_ENABLED("streaming.low-latency.enabled");

    private final String type;

    ConfigurationType(String s) {
        this.type = s;
    }

    public static ConfigurationType fromString(String text) {
        for (ConfigurationType configurationType : ConfigurationType.values()) {
            if (configurationType.type.equalsIgnoreCase(text)) {
                return configurationType;
            }
        }

        return null;
    }

}
