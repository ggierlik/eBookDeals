import logging, email
from google.appengine.ext import webapp
from google.appengine.ext.webapp.mail_handlers import InboundMailHandler
from google.appengine.ext.webapp.util import run_wsgi_app

class LogSenderHandler(InboundMailHandler):
    def receive(self, mail_message):
        logging.warning("Received a message from: " + mail_message.sender)
        logging.warning("Subject: " + mail_message.subject)
        logging.warning("To: " + mail_message.to)

        plaintext = mail_message.bodies('text/plain')
        
        for text in plaintext:
            textmsg = text[1].decode()
            logging.info("Body is:\n%s" % textmsg)

application = webapp.WSGIApplication([LogSenderHandler.mapping()], debug=True)

def main():
    run_wsgi_app(application)
 
if __name__ == '__main__':
    main()
    