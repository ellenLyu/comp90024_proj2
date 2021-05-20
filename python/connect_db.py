#  Copyright (c) 2021.
#  COMP90024 Cluster and Cloud Computing Proj2
#  Group27

import argparse
import csv
import sys

import database
import json

# DB_AUTH = {"ADDRESS": "localhost", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}
DB_AUTH = {"ADDRESS": "172.26.128.60", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}

# DB_NAME = 'tweets'
FILE_PATH = 'files/'

if __name__ == '__main__':
    print(sys.argv)
    parser = argparse.ArgumentParser()
    parser.add_argument('-mode', type=str, required=True, default='twitter')
    parser.add_argument('-filename', type=str, required=True, default='smallTwitter.json')
    parser.add_argument('-dbname', type=str, required=True, default='tweets')
    parser.add_argument('-keys', nargs='+', required='csv' in sys.argv)

    args = parser.parse_args()

    mode = args.mode
    filename = args.filename
    dbname = args.dbname
    keys = args.keys

    print(keys)

    # 'https://username:password@host:port/'
    url = 'http://{0}:{1}@{2}:{3}/'.format(DB_AUTH["COUCHDB_USER"], DB_AUTH["COUCHDB_PASSWORD"],
                                           DB_AUTH["ADDRESS"], DB_AUTH["PORT"])
    db = database.Connection(url=url, database_name=dbname)

    if mode == 'twitter':
        with open(FILE_PATH + filename, "r") as f:
            twitter_file = json.load(f)
        db.insert_tweets(tweets_list=twitter_file['rows'])
    elif mode == 'csv':
        with open(FILE_PATH + filename, "r") as f:
            csv_file = csv.reader(f)
            db.insert_dataset(file=csv_file, keys=keys)
