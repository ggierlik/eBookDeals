import urllib
from xml.etree.ElementTree import parse
import feedparser
import sys

#import datetime

import book
import update

#TODO: move function to class
#TODO: move every publisher to separate cron task

#TODO: it would be nice to have dictonary here
OREILLY_FEED_RSS = 'http://feeds.feedburner.com/oreilly/ebookdealoftheday'
MS_FEED_RSS = 'http://feeds.feedburner.com/oreilly/mspebookdeal'
APRESS_FEED_RSS = 'http://twitter.com/statuses/user_timeline/34233602.rss'
MANNING_BOOKS_FEED_RSS = 'http://twitter.com/statuses/user_timeline/24914741.rss'
INFORMIT_FEED_RSS = "http://www.informit.com/deals/deal_rss.aspx"

def get_oreilly_rss_feed(publisher, url):

    f = feedparser.parse(url)
    print "PARSED"

    print "title: %s" % (f.entries[0].title,)
#    print "summary: %s" % (f.entries[0].summary,)
    print "link: %s" % (f.entries[0].link,)

    return book.get_book(publisher, f.entries[0].title , f.entries[0].title, f.entries[0].link)


def get_apress_rss_feed():
    publisher = 'Apress'

    f = feedparser.parse(APRESS_FEED_RSS)
    return book.get_book(publisher, f.entries[0].title, f.entries[0].summary, 'http://apress.com/info/dailydeal')


def get_manning_rss_feed():
    publisher = 'Manning Books'

    f = feedparser.parse(MANNING_BOOKS_FEED_RSS)

    print "PARSED"

    print "items: %d" % (len(f.entries), )

    j = 1

    for item in f.entries:
        print "%d" % (j,)
        print "title: %s" % (item.title,)

        title = item.title

        i = title.find('Use code dotd')
        print "i: %d" % (i, )
        if i > 0:
            i = title.find('(http')
            j = title.find(')', i)
            print "(%d, %d)" % (i, j, )
            if j < i:
                j = i
            link = title[i+1:j]
            print "link: %s %d %d" % (link, i, j)
            return book.get_book(publisher, title, item.summary, link)

def get_informit_rss_feed():
    publisher = "informIT"

    f = feedparser.parse(INFORMIT_FEED_RSS)
    return book.get_book(publisher, f.entries[0].title, f.entries[0].title_detail.value, f.entries[0].link)


#def clear_deals():
#    books = book.Book.all()
#    books.delete()
#
#try:
#    print "Clear previous deals"
#    clear_deals()
#    print "CLEAR OK"
#except:
#    print "CLEAR:", sys.exc_info()[0]

#dte = datetime.date.today()
#print "Start: " + dte

update.update("ALL", True)

try:
    print "BEGIN O'REILLY"

    ebook = get_oreilly_rss_feed('O''Reilly Media', OREILLY_FEED_RSS)
    ebook.put()

    print "O'REILLY OK"
except:
    print "O'REILLY ERROR:", sys.exc_info()[0]

try:
    print "BEGIN MSPRESS"

    ebook = get_oreilly_rss_feed('Microsoft Press', MS_FEED_RSS)

    if ebook is not None:
        ebook.put()


    print "MSPRESS OK"
except:
    print "MSPRESS ERROR:", sys.exc_info()[0]

try:
    print "BEGIN APRESS"

    ebook = get_apress_rss_feed()

    if ebook is not None:
        ebook.put()

    print "APRESS OK"
except:
    print "APRESS ERROR:", sys.exc_info()[0]

try:
    print "BEGIN MANNING"

    ebook = get_manning_rss_feed()

    if ebook is not None:
        ebook.put()

    print "MANNING OK"
except:
    print "MANNING ERROR:", sys.exc_info()[0]

try:
    print "BEGIN INFORMIT"
    ebook = get_informit_rss_feed()

    if ebook is not None:
        ebook.put()

    print "INFORMIT OK"
except:
    print "INFORMIT ERROR:", sys.exc_info()[0]


#dte = datetime.date.today()
#print "End: " + dte
