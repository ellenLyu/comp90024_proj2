import couchdb
import re
from couchdb import design
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer


class Connection:
    BATCH_SIZE = 150

    def __init__(self, database_name, url):
        print(url)
        self.couch = couchdb.Server(url=url)

        try:
            self.couch_db_connector = self.couch[database_name]
        except:
            self.couch_db_connector = self.couch.create(database_name)
            self.create_default_views()

    def insert_tweets(self, tweets_list, has_doc: bool = False):
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
            doc = self.parse_tweet(tweet, analyzer, has_doc)

            if doc is not None:
                tmp_batch_list.append(doc)

            # If reach the batch size, insert
            if len(tmp_batch_list) == self.BATCH_SIZE:
                res = self.couch_db_connector.update(tmp_batch_list)
                tmp_batch_list = []
                print(res)

        # Insert the remaining
        if len(tmp_batch_list) != 0:
            res = self.couch_db_connector.update(tmp_batch_list)
            print(res)

    def insert_dataset(self, file, keys: list):
        """
        Insert csv file to database
        :param keys: List of fields to concat the id
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
                doc = self.parse_csv(fields, row, keys=keys)

                if doc is not None:
                    tmp_batch_list.append(doc)

                if len(tmp_batch_list) == self.BATCH_SIZE:
                    res = self.couch_db_connector.update(tmp_batch_list)
                    tmp_batch_list = []
                    print(res)

        if len(tmp_batch_list) != 0:
            res = self.couch_db_connector.update(tmp_batch_list)
            print(res)

    def create_default_views(self):
        """
        Create default views to get all docs with all fields.
        :return:
        """

        # view 1: views to get all docs with all fields
        view1_map = "function (doc) { emit(doc._id, doc) }"
        view1 = design.ViewDefinition(design="example", name="get_all", map_fun=view1_map)

        view1.sync(self.couch_db_connector)

        # view 2: views to get count of all docs
        view2_map = "function (doc) { emit(doc._id, 1) }"
        view2_reduce = "function(keys, values) { return sum(values) }"
        view2 = design.ViewDefinition(design="example", name="get_all_count",
                                      map_fun=view2_map, reduce_fun=view2_reduce)

        view2.sync(self.couch_db_connector)

    ###########################
    # Helper function
    ###########################

    def parse_csv(self, fields: list, row, keys):
        """
        Parse the row in csv file into dict
        :param fields: cols list
        :param row: row data
        :return: dict with expected fields
        """
        try:
            doc = dict(zip(fields, row))

            # Join the key for row
            sid = '_'.join([row[fields.index(key)] for key in keys])
            doc['_id'] = sid

            # If the row does not exist in database, create the doc
            if sid not in self.couch_db_connector:
                return doc
            else:
                # Get the doc in database
                ori_doc = dict(self.couch_db_connector[sid])

                return self._compare_two_docs(doc=doc, ori_doc=ori_doc)

        except Exception as e:
            print("Failed to parse csv row: ", str(e))
            return None

    def parse_tweet(self, tweet, analyzer, has_doc: bool = False):
        """
        Parse the tweets into dict
        :param tweet: tweet data
        :return: dict with expected fields
        """
        try:
            doc = {}
            if has_doc:
                tweet_id = tweet['doc']['id_str']
                source_text = tweet['doc']['text']
                created_at = tweet['doc']['created_at']
                retweet_count = tweet['doc']['retweet_count']
                favorite_count = tweet['doc']['favorite_count']
                if tweet['doc']['coordinates']['coordinates'] != '':
                    doc['coordinates'] = tweet['doc']['coordinates']['coordinates']
                if tweet['doc']['entities']['hashtags']:
                    doc['hashtags'] = tweet['doc']['entities']['hashtags']
            else:
                tweet_id = tweet['id_str']
                source_text = tweet['text']
                created_at = tweet['created_at']
                retweet_count = tweet['retweet_count']
                favorite_count = tweet['favorite_count']
                if tweet['coordinates']:
                    doc['coordinates'] = tweet['coordinates']
                if tweet['entities']['hashtags']:
                    doc['hashtags'] = tweet['entities']['hashtags']


            # Get the sentiment
            text = self.tweet_preprocessing(source_text)
            compound = analyzer.polarity_scores(text)['compound']
            sentiment = 'negative' if compound <= -0.05 else 'positive' if compound >= 0.05 else 'neutral'


            if 'suburb' in tweet:
                doc['suburb'] = tweet['suburb']
            doc.update({
                '_id': tweet_id, 'text': source_text, 'sentiment': sentiment,
                'created_at': created_at, 'retweet_count': retweet_count, 'favorite_count': favorite_count,
            })
            # print(doc)

            if tweet_id not in self.couch_db_connector:
                return doc
            else:
                # Get the doc in database
                ori_doc = dict(self.couch_db_connector[tweet_id])

                return self._compare_two_docs(doc=doc, ori_doc=ori_doc)

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


    def _compare_two_docs(self, doc: dict, ori_doc: dict):
        """
        Compare the existing doc and new doc
        :param doc: new doc
        :param ori_doc: existing doc
        :return: update doc if different, otherwise None
        """
        # Extract the existing _rev
        _rev = ori_doc.pop('_rev')

        if ori_doc != doc:
            # If they are different, require update
            ori_doc.update(doc)
            ori_doc['_rev'] = _rev
            return ori_doc
        else:
            # Otherwise, do not perform update
            return None
