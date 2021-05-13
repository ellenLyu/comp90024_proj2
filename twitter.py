import tweepy
import tweepy
from tweepy import Stream
from tweepy.streaming import StreamListener 
from tweepy import OAuthHandler
import json
import pandas as pd
import sys

print(len(sys.argv))
print(str(sys.argv))
input = str(sys.argv)
if input[1] ==  

sys.exit()

consumer_key = 'rZFruNsPGW0dztpugftVGj837'  
consumer_secret = 'L6AzbTcwZZuJYWkBxt5mL7Ern7sKUxHuGyUqKE7WSql9vkRZNG'  
access_token = '1381264077268283393-otuXk0e8EVuw5BnrYbKpEuVDButxQI'  
access_token_secret = '9HMXSW6FzC24nlA1sccTaF8Wwypdh2KHbpxz2GT6jSB94'  

'''
consumer_key = 'thXKO36EQXVLHLyCI9OmtDYxz'  
consumer_secret = 'sejP7Ejxgwj7CeJ7v6Cq0Xgkjr6wlX7nqAGN7JI4EFrBT9aBYMsM'  
access_token = '1388443056366522373-hF6fQq4h6v8Y6NYb8N0mg819881M5w'  
access_token_secret = '55VHShxJhL8r8ZxVRcOsyUvcqjDc3mDSZAPqgerpRcTjZ'  
'''

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)  
auth.set_access_token(access_token, access_token_secret)  
api = tweepy.API(auth,wait_on_rate_limit=True)
places = api.geo_search(query="melbourne", granularity="city")
#print(places)
#sys.exit()

melb = api.geo_id("01864a8a64df9dc4")
print(melb)

'''
tweets = api.user_timeline(id = 'Bitcoin',count = 10)
for tweet in tweets:
    print(tweet.text)
    print(tweet.coordinates,tweet.place)'''

COLS = ['id','created_at','lang','original text','user_name', 'place', 'place type', 'bbx', 'coordinates']
keyword='covid'


geo='-37.972566514250005,145.1525293990965,50km'
df = pd.DataFrame(columns=COLS)
tweet_count = 10

print("  ")

for page in tweepy.Cursor(api.search, q=keyword,include_rts=False,geocode=geo).pages():     
    while tweet_count > 0:
        for tweet in page:
               print(str(tweet._json))
               print(tweet_count)
               if tweet_count == 0:
                   break
               tweet_count -= 1
               with open('tweet2.json', 'a') as outfile:
                    #print(tweet._json)
                    #outfile.write(str(tweet._json).replace(u'\xa0', u''))
                    json_str = json.dumps(tweet._json)
                    outfile.write(json_str)
                    outfile.write("\n")
                    
                    #json.dump(tweet._json, outfile)                     

    break                  



        
