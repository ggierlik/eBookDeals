from google.appengine.ext import db

class Update(db.Model):
    publisher = db.StringProperty()
    updated = db.BooleanProperty(default=True)
    date = db.DateTimeProperty(auto_now_add=True)

def update(publisher, updated):
    update = Update()
    update.publisher = publisher
    update.updated = updated

    update.put()

