import argparse
import time
import tweepy
import json
import database
from tweepy import Stream, OAuthHandler, TweepError
from tweepy.streaming import StreamListener 


# consumer_key = 'rZFruNsPGW0dztpugftVGj837'  
# consumer_secret = 'L6AzbTcwZZuJYWkBxt5mL7Ern7sKUxHuGyUqKE7WSql9vkRZNG'  
# access_token = '1381264077268283393-otuXk0e8EVuw5BnrYbKpEuVDButxQI'  
# access_token_secret = '9HMXSW6FzC24nlA1sccTaF8Wwypdh2KHbpxz2GT6jSB94'

# consumer_key = 'T08RKyC74lJTtOeozaBMSjbs9'  
# consumer_secret = 'nBbdYRK7BmsoD5h23l9OT4ICQ2CABnmcCJAJunZtFtv556bYzL'  
# access_token = '1388437713536229380-rUyKUTYcwz887Hw0AdsLbhZtfapxeJ'  
# access_token_secret = 'bmZSM7APPq9MFbNZqiBsMkE9nk3rpZVU07qDzNfePg6T2'
# env_label = "dev"


consumer_key = 'tOZAOMxCWzvKXZDDTOsYweYr1'  
consumer_secret = '8xOqAwE8JvH0LtfjVL7dSh1uh3KIAVIvU7KgsijF55LDofTyAA'  
access_token = '1388443056366522373-10dWmvKxRHWUCNxvkkIpbU6ee0Wb9c'  
access_token_secret = '0hvk2gG9KdkblJkmU88Y2SmOcECU3SXQHaC9A6ginJrL3'
env_label = "test"

keywords = [
    "covid",  
]
keyword = f'({" OR ".join(keywords)})'
DB_AUTH = {"ADDRESS": "172.26.130.120", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}
BATCH_SIZE = 150


class Crawler:
    def __init__(self, api, db, suburbs, tweet_count: int = 1000):
        self.db = db
        self.api = api
        self.suburbs = suburbs
        self.tweet_count = tweet_count
        self.tweets = []

    def crawl(self, query, suburb):
        for tweet in tweepy.Cursor(self.api.search_30_day, query=query, environment_name=env_label).items(self.tweet_count):
            temp = tweet._json
            if temp is not None:
                temp['suburb'] = suburb
                self.tweets.append(temp)
            if len(self.tweets) >= BATCH_SIZE:
                self.db.insert_tweets(self.tweets)
                self.tweets.clear()

    def run(self):
        for suburb, coordinates in self.suburbs.items():
            lat, lon = coordinates
            geocode = f'[{lon} {lat} 2km]'
            print(suburb, geocode)
            query = f'{keyword} point_radius:{geocode}'
            try:
                self.crawl(query, suburb)
            except TweepError as e:
                print(e)
                if e.response.status_code == 429:
                    print("Waiting on limit for 1 min")
                    time.sleep(60)
                    try:
                        self.crawl(query, suburb)
                    except Exception as e:
                        print("retry failed")
                        continue
                else:
                    time.sleep(10)
                continue
        if len(self.tweets) > 0:
            self.db.insert_tweets(self.tweets)
        print("Finished")


def get_api():
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)  
    auth.set_access_token(access_token, access_token_secret)  
    api = tweepy.API(auth,wait_on_rate_limit=False)
    return api


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-count', type=int, default=1000)
    parser.add_argument('-dbname', type=str, default='demo')
    args = parser.parse_args()


    tweet_count = args.count
    dbname = args.dbname


    with open("melb_coordinates.json", "r") as f:
        suburbs = json.load(f)

    
    url = 'http://{0}:{1}@{2}:{3}/'.format(DB_AUTH["COUCHDB_USER"], DB_AUTH["COUCHDB_PASSWORD"],
                                            DB_AUTH["ADDRESS"], DB_AUTH["PORT"])
    db = database.Connection(url=url, database_name=dbname)

    api = get_api()

    crawler = Crawler(api, db, suburbs, tweet_count)
    crawler.run()
