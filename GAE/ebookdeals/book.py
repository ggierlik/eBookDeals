import datetime

from google.appengine.ext import db

import update
#import string

class Book(db.Model):
    publisher = db.StringProperty()
    title = db.StringProperty(multiline=True)
    desc = db.StringProperty(multiline=True)
    link = db.StringProperty()
    date = db.DateTimeProperty(auto_now_add=True)

    def to_dict(self):
        return dict([(p, unicode(getattr(self, p))) for p in self.properties()]) #@IndentOk

def get_book(publisher, title, desc, link):
    book = Book()

    book.publisher = publisher
    removed = dict.fromkeys(map(ord, u"\r\t\n"), u" ")
    
    book.title = title.translate(removed)
    book.desc = desc.translate(removed)
    book.link = link

    return book

def read_books():
    last_update = update.Update.gql("WHERE publisher = 'ALL' ORDER BY date DESC").get()

    if last_update:
        d = datetime.timedelta(minutes=2)
        dte = last_update.date
    else:
        d = datetime.timedelta(minutes=90)
        dte = datetime.datetime.now()

    dte = dte - d

    #books = book.Book.all()
    return [dte,Book.gql("WHERE date >= :1 ORDER BY date DESC", dte).fetch(6)]
