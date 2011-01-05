import urllib
from xml.etree.ElementTree import parse
import feedparser
import sys

import book
import update

OREILLY_FEED_RSS = 'http://feeds.feedburner.com/oreilly/ebookdealoftheday'
MS_FEED_RSS = 'http://feeds.feedburner.com/oreilly/mspebookdeal'

def get_oreilly_rss_feed(publisher, url):

    f = feedparser.parse(url)
    return book.get_book(publisher, f.entries[0].title , f.entries[0].summary, f.entries[0].link)


try:
    publisher = 'O''Reilly Media'
    print "BEGIN O'REILLY"

    ebook = get_oreilly_rss_feed(publisher, OREILLY_FEED_RSS)
    ebook.put()

    print "O'REILLY OK"

    update(publisher, true)
except:
    print "O'REILLY ERROR:", sys.exc_info()[0]
    update(publisher, false)

try:
    publisher = 'Microsoft Press'
    print "BEGIN MSPRESS"

    ebook = get_oreilly_rss_feed(publisher, MS_FEED_RSS)
    ebook.put()

    print "MSPRESS OK"

    update(publisher, true)
except:
    print "MSPRESS ERROR:", sys.exc_info()[0]
    update(publisher, false)
