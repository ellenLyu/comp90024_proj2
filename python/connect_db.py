import json
from python import database

DB_AUTH = {"ADDRESS": "localhost", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}
DB_NAME = 'tweets'
BATCH_SIZE = 100
FILE_PATH = 'files/'

if __name__ == '__main__':
    # 'https://username:password@host:port/'
    url = 'http://{0}:{1}@{2}:{3}/'.format(DB_AUTH["COUCHDB_USER"], DB_AUTH["COUCHDB_PASSWORD"],
                                           DB_AUTH["ADDRESS"], DB_AUTH["PORT"])
    db = database.Connection(url=url, database_name=DB_NAME)

    with open(FILE_PATH + 'tinyTwitter.json', "r") as f:
        twitter_file = json.load(f)
    db.insert_tweets(twitter_file['rows'], BATCH_SIZE)