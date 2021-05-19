package com.kaczmarek.bggapplication.logic.bggapi

import android.util.Log
import android.util.Xml
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameDetails
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class BggBoardGameDetailsParser : BggResponseParser<BggBoardGameDetails> {

    companion object {
        private val NS: String? = null

        private const val ITEMS_TAG = "items"
        private const val ITEM_TAG = "item"
        private const val TITLE_TAG = "name"
        private const val TYPE_ATTR = "type"
        private const val TITLE_TYPE_EXPECTED = "primary"
        private const val VALUE_ATTR = "value"
        private const val THUMBNAIL_TAG = "thumbnail"
        private const val LINK_TAG = "link"
        private const val DESIGNER_TYPE = "boardgamedesigner"
        private const val ARTIST_TYPE = "boardgameartist"
        private const val DESCRIPTION_TAG = "description"
        private const val STATISTICS_TAG = "statistics"
        private const val RATINGS_TAG = "ratings"
        private const val RANKS_TAG = "ranks"
        private const val RANK_TAG = "rank"
        private const val RANK_TYPE_EXPECTED = "subtype"
        private const val NAME_ATTR = "name"
        private const val RANK_NAME_EXPECTED = "boardgame"
        private const val NOT_RANKED_VALUE = "Not Ranked"
    }

    override fun parse(inputStream: InputStream): BggBoardGameDetails {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.next()
            return readItem(parser)
        }
    }

    private fun readItem(parser: XmlPullParser): BggBoardGameDetails {
        parser.require(XmlPullParser.START_TAG, NS, ITEMS_TAG)
        while (parser.next() != XmlPullParser.START_TAG) {
            continue
        }
        parser.require(XmlPullParser.START_TAG, NS, ITEM_TAG)

        var title: String? = null
        var thumbnail: String? = null
        val designers = mutableListOf<String>()
        val artists = mutableListOf<String>()
        var description: String? = null
        var rank: Long? = null
        val type = readType(parser)

        while (parser.next() != XmlPullParser.END_TAG || parser.name != ITEM_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                TITLE_TAG -> {
                    if (isPrimaryTitle(parser)) {
                        title = readTitle(parser)
                    } else {
                        skip(parser)
                    }
                }
                THUMBNAIL_TAG -> thumbnail = readThumbnail(parser)
                LINK_TAG -> {
                    val linkType = parser.getAttributeValue(NS, TYPE_ATTR)
                    val value = parser.getAttributeValue(NS, VALUE_ATTR)
                    when (linkType) {
                        DESIGNER_TYPE -> designers.add(value)
                        ARTIST_TYPE -> artists.add(value)
                    }
                }
                DESCRIPTION_TAG -> description = readDescription(parser)
                STATISTICS_TAG -> {
                    rank = readRankFromStatistics(parser)
                }
                else -> skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, NS, ITEM_TAG)
        while (parser.next() != XmlPullParser.END_TAG) {
            continue
        }
        parser.require(XmlPullParser.END_TAG, NS, ITEMS_TAG)
        return BggBoardGameDetails(title!!, type, thumbnail, designers, artists,
            description!!, rank)
    }

    private fun readType(parser: XmlPullParser): String {
        return parser.getAttributeValue(NS, TYPE_ATTR)
    }

    private fun isPrimaryTitle(parser: XmlPullParser): Boolean {
        parser.require(XmlPullParser.START_TAG, NS, TITLE_TAG)
        return parser.getAttributeValue(NS, TYPE_ATTR) == TITLE_TYPE_EXPECTED
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, NS, TITLE_TAG)
        return parser.getAttributeValue(NS, VALUE_ATTR)
    }

    private fun readThumbnail(parser: XmlPullParser) =
        readText(parser, NS, THUMBNAIL_TAG)
    
    private fun readDescription(parser: XmlPullParser) =
        readText(parser, NS, DESCRIPTION_TAG)

    private fun readRankFromStatistics(parser: XmlPullParser): Long? {
        var rank: Long? = null
        parser.require(XmlPullParser.START_TAG, NS, STATISTICS_TAG)
        while (parser.next() != XmlPullParser.START_TAG) {
            continue
        }
        parser.require(XmlPullParser.START_TAG, NS, RATINGS_TAG)
        while (parser.next() != XmlPullParser.END_TAG || parser.name != RATINGS_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == RANKS_TAG) {
                rank = readRankFromRanks(parser)
            } else {
                skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, NS, RATINGS_TAG)
        while (parser.next() != XmlPullParser.END_TAG) {
            continue
        }
        parser.require(XmlPullParser.END_TAG, NS, STATISTICS_TAG)
        return rank
    }

    private fun readRankFromRanks(parser: XmlPullParser): Long? {
        var rank: Long? = null
        parser.require(XmlPullParser.START_TAG, NS, RANKS_TAG)
        while (parser.next() != XmlPullParser.END_TAG || parser.name != RANKS_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == RANK_TAG) {
                val type = parser.getAttributeValue(NS, TYPE_ATTR)
                val name =  parser.getAttributeValue(NS, NAME_ATTR)
                if (type == RANK_TYPE_EXPECTED && name == RANK_NAME_EXPECTED) {
                    val value = parser.getAttributeValue(NS, VALUE_ATTR)
                    if (value != NOT_RANKED_VALUE) {
                        rank = value.toLong()
                    }
                }
            } else {
                skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, NS, RANKS_TAG)
        return rank
    }
}