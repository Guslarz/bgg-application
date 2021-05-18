package com.kaczmarek.bggapplication.logic.bggapi

import android.util.Xml
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class BggBoardGameOverviewListParser : BggResponseParser<List<BggBoardGameOverview>> {

    companion object {
        private val NS: String? = null

        private const val ITEMS_TAG = "items"
        private const val ITEM_TAG = "item"
        private const val ID_ATTR = "id"
        private const val TITLE_TAG = "name"
        private const val VALUE_ATTR = "value"
        private const val YEAR_TAG = "yearpublished"
    }

    override fun parse(inputStream: InputStream): List<BggBoardGameOverview> {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.next()
            return readItems(parser)
        }
    }

    private fun readItems(parser: XmlPullParser): List<BggBoardGameOverview> {
        val items = mutableListOf<BggBoardGameOverview>()

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

    private fun readItem(parser: XmlPullParser): BggBoardGameOverview {
        parser.require(XmlPullParser.START_TAG, NS, ITEM_TAG)
        val id = readId(parser)
        var title: String? = null
        var year: Int? = null

        while (parser.next() != XmlPullParser.END_TAG || parser.name != ITEM_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                TITLE_TAG -> title = readTitle(parser)
                YEAR_TAG -> year = readYear(parser)
                else -> skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, NS, ITEM_TAG)

        if (year == null)
            year = 0
        return BggBoardGameOverview(id, title!!, year)
    }

    private fun readId(parser: XmlPullParser): Long {
        return parser.getAttributeValue(NS, ID_ATTR).toLong()
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, NS, TITLE_TAG)
        return parser.getAttributeValue(NS, VALUE_ATTR)
    }

    private fun readYear(parser: XmlPullParser): Int {
        parser.require(XmlPullParser.START_TAG, NS, YEAR_TAG)
        return parser.getAttributeValue(NS, VALUE_ATTR).toInt()
    }
}