import sys
import tweepy
import database
import json


consumer_key = 'rAmNZM8q4JiRwk1wUKYhJ1Q0e'
consumer_secret = 'BYlCr4JKmPwRH8NDEUZYntK2VTN838rNS1BfNZzazdecO5g86x'
access_token = '1388437713536229380-CVBqqfkN7vES6jxmdRCR4zLja3qBTl'
access_token_secret = '0qbEMgpIHInUDcmlfthhKpxAkmyjzaciDSq4h2eZaAEKc'


#use variables to access twitter
auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)
api = tweepy.API(auth, wait_on_rate_limit=True)


dbname = "tweets"
melb_bbox = [144.33363404800002, -38.50298801599996, 145.8784120140001, -37.17509899299995]
DB_AUTH = {"ADDRESS": "localhost", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}
url = 'http://{0}:{1}@{2}:{3}/'.format(DB_AUTH["COUCHDB_USER"], DB_AUTH["COUCHDB_PASSWORD"],
                                        DB_AUTH["ADDRESS"], DB_AUTH["PORT"])
db = database.Connection(url=url, database_name=dbname)


class CustomStreamListener(tweepy.StreamListener):
    def on_data(self, raw_data):
        print(raw_data["text"])
        db.insert_tweets([raw_data])

    def on_error(self, status_code):
        #print(status)
        print(status_code)
        print(sys.stderr, 'Encountered error with status code:', status_code)
        return True # Don't kill the stream

    def on_timeout(self):
        print ( sys.stderr, 'Timeout...')
        return True # Don't kill the stream


if __name__ == '__main__':
    streamingAPI = tweepy.streaming.Stream(auth, CustomStreamListener())
    streamingAPI.filter(languages=["en"], locations=melb_bbox)
