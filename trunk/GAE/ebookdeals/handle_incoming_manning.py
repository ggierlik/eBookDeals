import logging, email
from google.appengine.ext import webapp
from google.appengine.ext.webapp.mail_handlers import InboundMailHandler
from google.appengine.ext.webapp.util import run_wsgi_app

class LogSenderHandler(InboundMailHandler):
    def receive(self, mail_message):
        logging.info("Received a message from: " + mail_message.sender)

        logging.info("Subject: " + mail_message.subject)

        plaintext_bodies = message.bodies('text/plain')
        html_bodies = message.bodies('text/html')

        for content_type, body in html_bodies:
            decoded_html = body.decode()
            logging.info(decoded_html)
