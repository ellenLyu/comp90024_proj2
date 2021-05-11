import tweepy
import tweepy
from tweepy import Stream
from tweepy.streaming import StreamListener 
from tweepy import OAuthHandler
import json
import pandas as pd

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
tweet_count = 200000


for page in tweepy.Cursor(api.search, q=keyword,include_rts=False,geocode=geo).pages():
     while tweet_count > 0:
          for tweet in page:
               #print(tweet._json)
               print(tweet_count)
               tweet_json = {}
              
               tweet_count -= 1
               with open('tweet.json2', 'a') as outfile:
                    json.dump(tweet._json, outfile, indent=2)                     
               try:
                    coord = tweet['coordinates']['coordinates']
               except TypeError:
                    coord = 'no coordinates'
                                #print(coord)
                    '''
                    new_entry = []

                    #storing all JSON data from twitter API
                    tweet = tweet._json    

                    #Append the JSON parsed data to the string list:

                    new_entry += [tweet['id'], tweet['created_at'], tweet['lang'], tweet['text'], 
                                  tweet['user']['name']]

                    #check if place name is available, in case not the entry is named 'no place'
                    try:
                        place = tweet['place']['name']
                    except TypeError:
                        place = 'no place'
                    new_entry.append(place)

                    try:
                        place_type = tweet['place']['place_type']
                    except TypeError:
                        place_type = 'na'
                    new_entry.append(place_type)

                    try:
                        bbx = tweet['place']['bounding_box']['coordinates']
                    except TypeError:
                        bbx = 'na'
                    new_entry.append(bbx)

                    #check if coordinates is available, in case not the entry is named 'no coordinates'
                    try:
                        coord = tweet['coordinates']['coordinates']
                    except TypeError:
                        coord = 'no coordinates'
                    new_entry.append(coord)

                    # wrap up all the data into a data frame
                    single_tweet_df = pd.DataFrame([new_entry], columns=COLS)
                    df = df.append(single_tweet_df, ignore_index=True)

                    #get rid of tweets without a place
                    df_cleaned = df[df.place != 'no place']

print("tweets with place:")
print(len(df[df.place != 'no place']))

print("tweets with coordinates:")
print(len(df[df.coordinates != 'no coordinates']))

df_cleaned.to_csv('tweets_'+geo+'.csv', columns=COLS,index=False)'''


        
