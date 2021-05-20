#  Copyright (c) 2021.
#  COMP90024 Cluster and Cloud Computing Proj2
#  Group27

import argparse
import time
import re
import requests
import urllib3
from bs4 import BeautifulSoup

import sys


if __name__ == '__main__':

    # print(sys.path)

    parser = argparse.ArgumentParser()
    parser.add_argument('-path', type=str, default='files/')
    args = parser.parse_args()
    path = args.path

    url = 'https://discover.data.vic.gov.au/dataset/victorian-coronavirus-data'

    r = requests.get(url)
    soup = BeautifulSoup(r.text, 'html.parser')
    lis = soup.find(name='ul', attrs={"class": "resource-list"}).find_all('li', attrs={"class": "resource-item"})
    li = lis[1]

    file_url = li.find(name='a', attrs={"class": "resource-url-analytics"}).get('href')
    file_url = re.sub(r'&amp;', r'&', file_url)

    urllib3.disable_warnings()
    # file_url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vTwXSqlP56q78lZKxc092o6UuIyi7VqOIQj6RM4QmlVPgtJZfbgzv0a3X7wQQkhNu8MFolhVwMy4VnF/pub?gid=0&single=true&output=csv"
    r = requests.get(file_url, verify=False)

    filename = path + 'covid_cases_' + time.strftime("%Y%m%d") + '.csv'
    with open(filename, 'wb') as f:
        f.write(r.content)

    print(filename)