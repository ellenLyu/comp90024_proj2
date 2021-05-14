import couchdb
import re
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer


class Connection():
    BATCH_SIZE = 100

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

        # Vader Sentiment Analyzer
        analyzer = SentimentIntensityAnalyzer()

        # Batch list for insertion
        tmp_batch_list = []

        for tweet in tweets_list:

            # Parse the tweet into dict
            doc = self.parse_tweet(tweet, analyzer)

            if doc is not None:
                tmp_batch_list.append(doc)

            # If reach the batch size, insert
            if len(tmp_batch_list) == self.BATCH_SIZE:
                res = self.couch_db_connector.update(tmp_batch_list)
                print(res)

        # Insert the remaining
        if len(tmp_batch_list) != 0:
            res = self.couch_db_connector.update(tmp_batch_list)
            print(res)

    def insert_dataset(self, file, keys: list):
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
                print(fields)
                continue
            else:
                tmp_batch_list.append(self.parse_csv(fields, row, keys=keys))

                if len(tmp_batch_list) == self.BATCH_SIZE:
                    res = self.couch_db_connector.update(tmp_batch_list)
                    print(res)

        if len(tmp_batch_list) != 0:
            res = self.couch_db_connector.update(tmp_batch_list)
            print(res)

    ###########################
    # Helper function
    ###########################

    def parse_csv(self, fields:list, row, keys):
        """
        Parse the row in csv file into dict
        :param fields: cols list
        :param row: row data
        :return: dict with expected fields
        """
        try:
            doc = None

            # Join the key for row
            sid = '_'.join([row[fields.index(key)] for key in keys])

            # If the row does not exist in database, create the doc
            if sid not in self.couch_db_connector:
                doc = dict(zip(fields, row))
                doc['_id'] = sid

        except Exception as e:
            print("Failed to parse csv row: ", str(e))
            return None

        return doc

    def parse_tweet(self, tweet, analyzer):
        """
        Parse the tweets into dict
        :param tweet: tweet data
        :return: dict with expected fields
        """
        try:
            doc = None
            tweet_id = tweet['doc']['id_str']

            if tweet_id not in self.couch_db_connector:

                # Get the sentiment
                text = self.tweet_preprocessing(tweet['doc']['text'])
                compound = analyzer.polarity_scores(text)['compound']
                sentiment = 'negative' if compound <= -0.05 else 'positive' if compound >= 0.05 else 'neutral'

                doc = {'_id': tweet_id, 'text': tweet['doc']['text'], 'sentiment': sentiment}
                print(doc)
                # if "_rev" in tweet['doc']:
                # print(tweet['doc'].pop("_rev"))
                # if tweet['doc']['_rev'] != '':
                #     doc['_rev'] = tweet['doc']['_rev']

                # If the tweet has geo info
                if tweet['doc']['coordinates']['coordinates'] != '':
                    doc['coordinates'] = tweet['doc']['coordinates']['coordinates']

            return doc

        except Exception as e:
            print("Failed to parse tweet: ", str(e))
            return None

    def tweet_preprocessing(self, text):
        """
        Preprocessing the tweet text
        :param text: tweet text
        :return: after preprocessing
        """
        text = re.sub(r'https?:\/\/\S*|http?:\/\/\S*', '', text)
        text = re.sub(r'#(\w+)', '', text)
        text = re.sub(r'@[A-Za-z0-9]+', '', text)

        return text
