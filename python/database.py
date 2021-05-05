import couchdb


class Connection():

    def __init__(self, database_name, url):
        print(url)
        self.couch = couchdb.Server(url=url)
        self.couch_db_connector = self.couch[database_name]

    def insert_tweets(self, tweets_list, batch_size):

        tmp_batch_list = []

        for tweet in tweets_list:
            doc = self.parse_tweet(tweet)
            if doc is not None:
                tmp_batch_list.append(self.parse_tweet(tweet))

            if len(tmp_batch_list) == batch_size:
                res = self.couch_db_connector.update(tmp_batch_list)
                print(res)

        if len(tmp_batch_list) != 0:
            res = self.couch_db_connector.update(tmp_batch_list)
            print(res)

    def parse_tweet(self, tweet):

        try:
            tweet = tweet
            doc = {}
            tweet_id = tweet['doc']['id_str']

            if tweet_id not in self.couch_db_connector:
                doc['_id'] = tweet_id
                doc['text'] = tweet['doc']['text']

                # if "_rev" in tweet['doc']:
                    # print(tweet['doc'].pop("_rev"))
                # if tweet['doc']['_rev'] != '':
                #     doc['_rev'] = tweet['doc']['_rev']

                # emtpy geo
                if tweet['doc']['coordinates']['coordinates'] != '':
                    doc['coordinates'] = tweet['doc']['coordinates']['coordinates']

            return doc

        except Exception as e:
            print("Failed to parse tweet: ", str(e))
            return None
