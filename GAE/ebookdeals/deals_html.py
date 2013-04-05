#from google.appengine.api import users
import webapp2
import book
import datetime
import logging
import os
import jinja2

jinja_environment = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)))

class MainPage(webapp2.RequestHandler):
    def get(self):

        logging.info("Request income...")
        
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

        template = jinja_environment.get_template('index.django.html')
        self.response.out.write(template.render(template_values))


app = webapp2.WSGIApplication([('/', MainPage)], debug = True)

# def main():
#     run_wsgi_app(application)

# if __name__ == "__main__":
#     main()
