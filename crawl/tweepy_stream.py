import csv
import sys

import tweepy

# Valid Key my
# consumer_key = 'rZFruNsPGW0dztpugftVGj837'
# consumer_secret = 'L6AzbTcwZZuJYWkBxt5mL7Ern7sKUxHuGyUqKE7WSql9vkRZNG'
# access_token = '1381264077268283393-otuXk0e8EVuw5BnrYbKpEuVDButxQI'
# access_token_secret = '9HMXSW6FzC24nlA1sccTaF8Wwypdh2KHbpxz2GT6jSB94'


# override stream listener tweepy.StreamListener to add logic to on_status


consumer_key = 'rAmNZM8q4JiRwk1wUKYhJ1Q0e'
consumer_secret = 'BYlCr4JKmPwRH8NDEUZYntK2VTN838rNS1BfNZzazdecO5g86x'
access_token = '1388437713536229380-CVBqqfkN7vES6jxmdRCR4zLja3qBTl'
access_token_secret = '0qbEMgpIHInUDcmlfthhKpxAkmyjzaciDSq4h2eZaAEKc'


#use variables to access twitter
auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)
api = tweepy.API(auth, wait_on_rate_limit=True)

#create an object called 'customStreamListener'

class CustomStreamListener(tweepy.StreamListener):

    def on_status(self, status):
        print (status.author.screen_name, status.created_at, status.text)
        # Writing status data
        # with open('OutputStreaming.txt', 'a') as f:
        #     writer = csv.writer(f)
        #     writer.writerow([status.author.screen_name, status.created_at, status.text])


    def on_error(self, status_code):
        #print(status)
        print(status_code)
        print(sys.stderr, 'Encountered error with status code:', status_code)
        return True # Don't kill the stream

    def on_timeout(self):
        print ( sys.stderr, 'Timeout...')
        return True # Don't kill the stream

# Writing csv titles
with open('OutputStreaming.txt', 'w') as f:
    writer = csv.writer(f)
    writer.writerow(['Author', 'Date', 'Text'])

streamingAPI = tweepy.streaming.Stream(auth, CustomStreamListener())
streamingAPI.filter(languages=["en"], locations=[112.28, -44.36, 155.23, -10.37])