import book
import feedparser
import logging
import sys
import update
import re

# import datetime

#TODO: move function to class
#TODO: move every publisher to separate cron task
#TODO: refactor these functions (i.e. logging statements)
#TODO: it would be nice to have dictonary here

# OREILLY_FEED_RSS = 'http://feeds.feedburner.com/oreilly/ebookdealoftheday'
# MS_FEED_RSS = 'http://feeds.feedburner.com/oreilly/mspebookdeal'
APRESS_FEED_RSS = 'http://www.apress.com/index.php/dailydeals/index/rss'
MANNING_BOOKS_FEED_RSS = 'https://api.twitter.com/1/statuses/user_timeline.rss?screen_name=manningbooks'
INFORMIT_FEED_RSS = "http://www.informit.com/deals/deal_rss.aspx"

# def get_oreilly_rss_feed(publisher, url):
# 
#     logging.info("parsing %s", publisher)
#     f = feedparser.parse(url)
#     logging.info("PARSED")
# 
#     logging.info("title: %s" % (f.entries[0].title,))
#     logging.info("link: %s" % (f.entries[0].link,))
# 
#     return book.get_book(publisher, f.entries[0].title , f.entries[0].title, f.entries[0].link)
# 

def get_apress_rss_feed():
    publisher = 'Apress'

    logging.info("parsing %s", publisher)

    f = feedparser.parse(APRESS_FEED_RSS)

    logging.info("PARSED")

    return book.get_book(publisher, f.entries[0].title, f.entries[0].description, f.entries[0].link)


def get_manning_rss_feed():
    publisher = 'Manning Books'
    logging.info("parsing %s", publisher)

    f = feedparser.parse(MANNING_BOOKS_FEED_RSS)

    logging.info("PARSED")

    j = 1

    for item in f.entries:

        title = item.title

        logging.info("title is %s", title)

        i = title.find('Use code dotd')
        if i > 0:
            logging.info('DOTD code found')

            link = re.search("(?P<url>https?://[^\s]+)", title).group("url");

            logging.info('url to deal is %s', link);

            return book.get_book(publisher, title, item.summary, link)

def get_informit_rss_feed():
    publisher = "informIT"

    logging.info("parsing %s", publisher)

    f = feedparser.parse(INFORMIT_FEED_RSS)

    logging.info("PARSED")

    return book.get_book(publisher, f.entries[0].title, f.entries[0].title_detail.value, f.entries[0].link)


#def clear_deals():
#    books = book.Book.all()
#    books.delete()

#try:
#    print "Clear previous deals"
#    clear_deals()
#    print "CLEAR OK"
#except:
#    print "CLEAR:", sys.exc_info()[0]

# dte = datetime.date.today()
# print "Start: " + str(dte)

logging.getLogger().setLevel(logging.DEBUG)

update.update("ALL", True)

# try:
#     logging.info("BEGIN O'REILLY")
# 
#     ebook = get_oreilly_rss_feed('O\'Reilly Media', OREILLY_FEED_RSS)
#     ebook.put()
# 
#     logging.info("O'REILLY OK")
# except:
#     logging.exception("O'REILLY ERROR:", sys.exc_info()[0])
# 
# try:
#     logging.info("BEGIN MSPRESS")
# 
#     ebook = get_oreilly_rss_feed('Microsoft Press', MS_FEED_RSS)
# 
#     if ebook is not None:
#         ebook.put()
# 
# 
#     logging.info("MSPRESS OK")
# except:
#     logging.exception("MSPRESS ERROR:", sys.exc_info()[0])
# 
try:
    logging.info("BEGIN APRESS")

    ebook = get_apress_rss_feed()

    if ebook is not None:
        ebook.put()

    logging.info("APRESS OK")
except:
    logging.exception("APRESS ERROR:", sys.exc_info()[0])

try:
    logging.info("BEGIN MANNING")

    ebook = get_manning_rss_feed()

    if ebook is not None:
        ebook.put()

    logging.info("MANNING OK")
except:
    logging.exception("MANNING ERROR:", sys.exc_info()[0])

try:
    logging.info("BEGIN INFORMIT")
    ebook = get_informit_rss_feed()

    if ebook is not None:
        ebook.put()

    logging.info("INFORMIT OK")
except:
    logging.exception("INFORMIT ERROR:", sys.exc_info()[0])


# dte = datetime.date.today()
# print "End: " + str(dte)
