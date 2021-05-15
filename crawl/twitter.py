import argparse
import tweepy
import json
import database
from tweepy import Stream, OAuthHandler, TweepError
from tweepy.streaming import StreamListener 


consumer_key = 'rZFruNsPGW0dztpugftVGj837'  
consumer_secret = 'L6AzbTcwZZuJYWkBxt5mL7Ern7sKUxHuGyUqKE7WSql9vkRZNG'  
access_token = '1381264077268283393-otuXk0e8EVuw5BnrYbKpEuVDButxQI'  
access_token_secret = '9HMXSW6FzC24nlA1sccTaF8Wwypdh2KHbpxz2GT6jSB94'

# consumer_key = 'thXKO36EQXVLHLyCI9OmtDYxz'  
# consumer_secret = 'sejP7Ejxgwj7CeJ7v6Cq0Xgkjr6wlX7nqAGN7JI4EFrBT9aBYMsM'  
# access_token = '1388443056366522373-hF6fQq4h6v8Y6NYb8N0mg819881M5w'  
# access_token_secret = '55VHShxJhL8r8ZxVRcOsyUvcqjDc3mDSZAPqgerpRcTjZ'  


keywords = [
    "covid",  "virus", "corona", "outbreak", "covid-19", "quarantine", "morrison", "scott morrison",
]
DB_AUTH = {"ADDRESS": "localhost", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}
BATCH_SIZE = 300



def get_api():
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)  
    auth.set_access_token(access_token, access_token_secret)  
    api = tweepy.API(auth,wait_on_rate_limit=False)
    return api


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-count', type=int, default='300')
    parser.add_argument('-dbname', type=str, default='demo')
    args = parser.parse_args()


    tweet_count = args.count
    dbname = args.dbname
    keyword = " OR ".join(keywords) + " -filter:retweets"


    with open("melb_coordinates.json", "r") as f:
        suburbs = json.load(f)

    
    url = 'http://{0}:{1}@{2}:{3}/'.format(DB_AUTH["COUCHDB_USER"], DB_AUTH["COUCHDB_PASSWORD"],
                                            DB_AUTH["ADDRESS"], DB_AUTH["PORT"])
    db = database.Connection(url=url, database_name=dbname)


    api = get_api()

    tweets = []
    for suburb, coordinates in suburbs.items():
        lat, lon = coordinates
        geocode = f'{lat},{lon},10km'
        try:
            for tweet in tweepy.Cursor(api.search, q=keyword, include_rts=False, geocode=geocode).items(tweet_count):
                tweet._json['suburb'] = suburb
                tweets.append(tweet._json)
                if len(tweets) >= BATCH_SIZE:
                    db.insert_tweets(tweets)
                    tweets.clear()
        except TweepError as e:
            print(e)
            if e.response.status_code == 429:
                print("Waiting on limit for 15 mins")
                time.sleep(15 * 60 + 1)
            else:
                time.sleep(10)
            continue
    if len(tweets) > 0:
        db.insert_tweets(tweets)
    print("Finished")
