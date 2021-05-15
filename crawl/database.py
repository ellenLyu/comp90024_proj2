import couchdb


class Connection():

    BATCH_SIZE = 500

    def __init__(self, database_name, url):
        print(url)
        self.couch = couchdb.Server(url=url)

        try:
            self.couch_db_connector = self.couch[database_name]
        except:
            self.couch_db_connector = self.couch.create(database_name)

    def insert_tweets(self, tweets_list):
        """
        Insert twitter file to database
        :param tweets_list: list of tweets in json format
        :param batch_size: the batch size
        :return:
        """

        tmp_batch_list = []

        for tweet in tweets_list:
            doc = self.parse_tweet(tweet)
            if doc is not None:
                tmp_batch_list.append(self.parse_tweet(tweet))

            if len(tmp_batch_list) == self.BATCH_SIZE:
                res = self.couch_db_connector.update(tmp_batch_list)
                print(res)

        if len(tmp_batch_list) != 0:
            res = self.couch_db_connector.update(tmp_batch_list)
            print(res)

    def insert_dataset(self, file):
        """
        Insert csv file to database
        :param file: list of csv file content
        :return:
        """

        fields = None
        tmp_batch_list = []

        for row in file:
            if fields is None:
                fields = row
                continue
            else:
                tmp_batch_list.append(self.parse_csv(fields, row))

                if len(tmp_batch_list) == self.BATCH_SIZE:
                    res = self.couch_db_connector.update(tmp_batch_list)
                    print(res)

        if len(tmp_batch_list) != 0:
            res = self.couch_db_connector.update(tmp_batch_list)
            print(res)






    ###########################
    # Helper function
    ###########################

    def parse_csv(self, fields, row):
        doc = {}

        for idx in range(0, len(fields)):
            doc[fields[idx]] = row[idx]

        return doc

    def parse_tweet(self, tweet):
        try:
            tweet = tweet
            doc = {}
            tweet_id = tweet['id_str']

            if tweet_id not in self.couch_db_connector:
                doc['_id'] = tweet_id
                doc['text'] = tweet['text']
                doc['created_at'] = tweet['created_at']
                doc['retweet_count'] = tweet['retweet_count']

                # if "_rev" in tweet['doc']:
                # print(tweet['doc'].pop("_rev"))
                # if tweet['doc']['_rev'] != '':
                #     doc['_rev'] = tweet['doc']['_rev']

                if tweet['coordinates'] != '':
                    doc['coordinates'] = tweet['coordinates']
                if tweet['entities']['hashtags']:
                    doc['hashtags'] = tweet['entities']['hashtags']
                if 'suburb' in tweet:
                    doc['suburb'] = tweet['suburb']

                    
            return doc

        except Exception as e:
            print("Failed to parse tweet: ", str(e))
            return None
