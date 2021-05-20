#  Copyright (c) 2021.
#  COMP90024 Cluster and Cloud Computing Proj2
#  Group27

import datetime
import time

import couchdb
from couchdb import design


def getBetweenDay(begin_date, end_date):
    date_list = []
    begin_date = datetime.datetime.strptime(begin_date, "%Y-%m-%d")
    end_date = datetime.datetime.strptime(end_date, "%Y-%m-%d")
    # end_date = datetime.datetime.strptime(time.strftime('%Y-%m-%d', time.localtime(time.time())), "%Y-%m-%d")

    while begin_date <= end_date:
        date_str = begin_date.strftime("%Y-%m-%d")
        date_list.append(date_str)
        begin_date += datetime.timedelta(days=1)
    return date_list


DB_AUTH = {"ADDRESS": "172.26.128.60", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}

if __name__ == '__main__':

    data_list = getBetweenDay('2020-01-25', '2021-05-17')

    url = 'http://{0}:{1}@{2}:{3}/'.format(DB_AUTH["COUCHDB_USER"], DB_AUTH["COUCHDB_PASSWORD"],
                                           DB_AUTH["ADDRESS"], DB_AUTH["PORT"])
    couch = couchdb.Server(url=url)
    couch_db_connector = couch['covid_before']

    missing_date = []

    date_idx = 0

    for item in couch_db_connector.view('example/get_daily_new', group=True):
        while data_list[date_idx] < str(item.key):
            print(data_list[date_idx] + "  " + str(item.key))
            missing_date.append(data_list[date_idx])
            date_idx += 1
        date_idx += 1

    bulk = []
    for missing in missing_date:
        bulk_doc = {
            "_id": "idx_0_" + missing,
            "diagnosis_date": missing,
            "Postcode": "0",
            "acquired": "Travel overseas",
            "Localgovernmentarea": ""
        }
        bulk.append(bulk_doc)

    res = couch_db_connector.update(bulk)
    print(res)
    #
    #
    # view = design.
