import logging
import urllib
from xml.etree.ElementTree import parse
import feedparser
import sys

import book
import update

APRESS_FEED_RSS = 'http://www.apress.com/index.php/dailydeals/index/rss'

PUBLISHER = 'Apress'

def get_apress_rss_feed():
    f = feedparser.parse(APRESS_FEED_RSS)
    return book.get_book(publisher, f.entries[0].title, f.entries[0].description, f.entries[0].link)

try:
    logging.info("BEGIN APRESS")

    ebook = get_apress_rss_feed()
    ebook.put()

    logging.info("APRESS OK")

    update(PUBLISHER, true)
except:
    logging.exception("APRESS ERROR:", sys.exc_info()[0])
    update(PUBLISHER, false)
