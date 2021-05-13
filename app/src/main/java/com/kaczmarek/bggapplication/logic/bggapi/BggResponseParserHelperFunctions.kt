package com.kaczmarek.bggapplication.logic.bggapi

import org.xmlpull.v1.XmlPullParser

fun skip(parser: XmlPullParser) {
    if (parser.eventType != XmlPullParser.START_TAG) {
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        when (parser.next()) {
            XmlPullParser.END_TAG -> depth--
            XmlPullParser.START_TAG -> depth++
        }
    }
}

fun readText(parser: XmlPullParser, ns: String?, tag: String): String {
    parser.require(XmlPullParser.START_TAG, ns, tag)
    parser.next()
    val text = parser.text
    parser.next()
    parser.require(XmlPullParser.END_TAG, ns, tag)
    return text
}