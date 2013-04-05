import logging, datetime
import json

import webapp2
#from google.appengine.ext.webapp.util import run_wsgi_app

import book

class MainPage(webapp2.RequestHandler):
    def get(self):
        logging.info("Request is comming")
        self.response.headers["Content-type"] = "application/json"

        dte, books = book.read_books()

        #data = {"foo": "bar"}

        self.response.out.write(json.dumps([b.to_dict() for b in books]))

        logging.info("done")

app = webapp2.WSGIApplication([('/get_deals', MainPage)], debug=True)
