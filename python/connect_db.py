import argparse
import csv
import database
import json




DB_AUTH = {"ADDRESS": "172.26.128.60", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}
# DB_NAME = 'tweets'
FILE_PATH = 'files/'

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-mode', type=str, default='csv')
    parser.add_argument('-filename', type=str, default='dataset1.csv')
    parser.add_argument('-dbname', type=str, default='covid')

    args = parser.parse_args()

    mode = args.mode
    filename = args.filename
    dbname = args.dbname

    # 'https://username:password@host:port/'
    url = 'http://{0}:{1}@{2}:{3}/'.format(DB_AUTH["COUCHDB_USER"], DB_AUTH["COUCHDB_PASSWORD"],
                                           DB_AUTH["ADDRESS"], DB_AUTH["PORT"])
    db = database.Connection(url=url, database_name=dbname)

    if mode == 'twitter':
        with open(FILE_PATH + filename, "r") as f:
            twitter_file = json.load(f)
        db.insert_tweets(twitter_file['rows'])
    elif mode == 'csv':
        with open(FILE_PATH + filename, "r") as f:
            csv_file = csv.reader(f)
            db.insert_dataset(csv_file)