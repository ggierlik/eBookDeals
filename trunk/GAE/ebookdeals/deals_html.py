import cgi

import os

import datetime

from google.appengine.ext.webapp import template
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app

import book

class MainPage(webapp.RequestHandler):
    def get(self):

        dte, books = book.read_books()
        delta = datetime.datetime.now() - dte

        minutes = delta.seconds / 60

        if minutes < 60:
            label = str(minutes) + " minutes"
        else:
            if minutes < 120:
                label = "an hour"
            else:
                label = str(minutes / 60) + " hours"

        template_values = {
            'books': books,
            'time_label': label,
        }

        path = os.path.join(os.path.dirname(__file__), 'index.django.html')
        self.response.out.write(template.render(path, template_values))


application = webapp.WSGIApplication(
    [('/', MainPage)],
    debug = True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()
