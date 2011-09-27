import logging, datetime

from django.utils import simplejson

from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app

import book

class MainPage(webapp.RequestHandler):
    def get(self):
        logging.info("Request is comming")
        self.response.headers["Content-type"] = "application/json"

        dte, books = book.read_books()

        #data = {"foo": "bar"}

        self.response.out.write(simplejson.dumps([b.to_dict() for b in books]))

        logging.info("done")

application = webapp.WSGIApplication(
                                     [('/get_deals', MainPage)],
                                     debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()
