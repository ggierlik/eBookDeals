import urllib
from xml.etree.ElementTree import parse
import feedparser
import sys

import book
import update

MANNING_BOOKS_FEED_RSS = 'http://twitter.com/statuses/user_timeline/24914741.rss'
PUBLISHER = 'Manning Books'

def get_manning_rss_feed(publisher):

    f = feedparser.parse(MANNING_BOOKS_FEED_RSS)

    #print "PARSED"

    #print "items: %d" % (len(f.entries), )

    for item in f.entries:
        title = item.title
        #print "title: %s" % (title, )
        i = title.find('Use code dotd')
        #print "i: %d" % (i, )
        if i > 0:
            i = title.find('(http')
            j = title.find(')', i)
            #print "(%d, %d)" % (i, j, )
            if j < i:
                j = i
            link = title[i+1:j]
            #print "link: %s %d %d" % (link, i, j)
            return book.get_book(publisher, title, item.summary, link)

try:
    print "BEGIN MANNING"

    ebook = get_manning_rss_feed(PUBLISHER)
    ebook.put()

    print "MANNING OK"

    update(PUBLISHER, true)
except:
    print "MANNING ERROR:", sys.exc_info()[0]
    update(PUBLISHER, false)
