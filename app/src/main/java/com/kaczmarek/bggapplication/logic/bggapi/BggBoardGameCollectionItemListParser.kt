package com.kaczmarek.bggapplication.logic.bggapi

import android.util.Xml
import com.kaczmarek.bggapplication.entities.external.BggBoardGameCollectionItem
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class BggBoardGameCollectionItemListParser : BggResponseParser<List<BggBoardGameCollectionItem>> {
    companion object {
        private val NS: String? = null

        private const val ITEMS_TAG = "items"
        private const val ITEM_TAG = "item"
        private const val ID_ATTR = "objectid"
        private const val TITLE_TAG = "name"
        private const val VALUE_ATTR = "value"
        private const val YEAR_TAG = "yearpublished"
        private const val TYPE_ATTR = "type"
        private const val STATISTICS_TAG = "statistics"
        private const val RATINGS_TAG = "ratings"
        private const val RANKS_TAG = "ranks"
        private const val RANK_TAG = "rank"
        private const val RANK_TYPE_EXPECTED = "subtype"
        private const val NAME_ATTR = "name"
        private const val RANK_NAME_EXPECTED = "boardgame"
        private const val NOT_RANKED_VALUE = "Not Ranked"
    }

    override fun parse(inputStream: InputStream): List<BggBoardGameCollectionItem> {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.next()
            return readItems(parser)
        }
    }

    private fun readItems(parser: XmlPullParser): List<BggBoardGameCollectionItem> {
        val items = mutableListOf<BggBoardGameCollectionItem>()

        parser.require(XmlPullParser.START_TAG, NS, ITEMS_TAG)
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == ITEM_TAG) {
                items.add(readItem(parser))
            } else {
                skip(parser)
            }
        }

        return items
    }

    private fun readItem(parser: XmlPullParser): BggBoardGameCollectionItem {
        parser.require(XmlPullParser.START_TAG, NS, ITEM_TAG)
        val id = readId(parser)
        var title: String? = null
        var year: Int? = null
        var rank: Long? = null

        while (parser.next() != XmlPullParser.END_TAG || parser.name != ITEM_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                TITLE_TAG -> title = readTitle(parser)
                YEAR_TAG -> year = readYear(parser)
                STATISTICS_TAG -> rank = readRankFromStatistics(parser)
                else -> skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, NS, ITEM_TAG)
        return BggBoardGameCollectionItem(id, title!!, year!!, rank)
    }

    private fun readId(parser: XmlPullParser): Long {
        return parser.getAttributeValue(NS, ID_ATTR).toLong()
    }

    private fun readTitle(parser: XmlPullParser) = readText(parser, NS, TITLE_TAG)

    private fun readYear(parser: XmlPullParser) = readText(parser, NS, YEAR_TAG).toInt()

    private fun readRankFromStatistics(parser: XmlPullParser): Long? {
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
                return readRankFromRanks(parser)
            } else {
                skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, NS, RATINGS_TAG)
        while (parser.next() != XmlPullParser.END_TAG) {
            continue
        }
        parser.require(XmlPullParser.END_TAG, NS, STATISTICS_TAG)
        return null
    }

    private fun readRankFromRanks(parser: XmlPullParser): Long? {
        parser.require(XmlPullParser.START_TAG, NS, RANKS_TAG)
        while (parser.next() != XmlPullParser.END_TAG || parser.name != RANKS_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == RANK_TAG) {
                if (parser.getAttributeValue(NS, TYPE_ATTR) == RANK_TYPE_EXPECTED &&
                    parser.getAttributeValue(NS, NAME_ATTR) == RANK_NAME_EXPECTED) {

                    val value = parser.getAttributeValue(NS, VALUE_ATTR)
                    if (value != NOT_RANKED_VALUE) {
                        return value.toLong()
                    }
                }
            } else {
                skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, NS, RANKS_TAG)
        return null
    }
}