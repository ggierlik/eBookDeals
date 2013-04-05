import logging, email
from google.appengine.ext import webapp
from google.appengine.ext.webapp.mail_handlers import InboundMailHandler
from google.appengine.ext.webapp.util import run_wsgi_app

class LogSenderHandler(InboundMailHandler):
    def receive(self, mail_message):
        logging.info("Received a message from: " + mail_message.sender)

        logging.info("Subject: " + mail_message.subject)

        plaintext = mail_message.bodies('text/plain')
        
        for text in plaintext:
            textmsg = text[1].decode()
            logging.info("Body is:\n%s" % textmsg)
             
        html_bodies = mail_message.bodies('text/html')

        for content_type, body in html_bodies:
            decoded_html = body.decode()
            logging.info("HTML is:\n%s" % decoded_html)

application = webapp.WSGIApplication([LogSenderHandler.mapping()], debug=True)

def main():
    run_wsgi_app(application)
 
if __name__ == '__main__':
    main()
    