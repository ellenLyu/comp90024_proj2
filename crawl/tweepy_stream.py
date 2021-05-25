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
melb_bbox = [112.28, -44.36, 155.23, -10.37]
DB_AUTH = {"ADDRESS": "172.26.131.194", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}
url = 'http://{0}:{1}@{2}:{3}/'.format(DB_AUTH["COUCHDB_USER"], DB_AUTH["COUCHDB_PASSWORD"],
                                        DB_AUTH["ADDRESS"], DB_AUTH["PORT"])
db = database.Connection(url=url, database_name=dbname)


class CustomStreamListener(tweepy.StreamListener):
    def on_data(self, raw_data):
        data = json.loads(raw_data)
        print(data["text"])
        db.insert_tweets([data])

    def on_error(self, status_code):
        #print(status)
        print(status_code)
        print(sys.stderr, 'Encountered error with status code:', status_code)
        return True # Don't kill the stream

    def on_timeout(self):
        print(sys.stderr, 'Timeout...')
        return True # Don't kill the stream


if __name__ == '__main__':
    streamingAPI = tweepy.streaming.Stream(auth, CustomStreamListener())
    while True:
        try:
            streamingAPI.filter(languages=["en"], locations=melb_bbox)
        except Exception as e:
            print(e)
            continue
