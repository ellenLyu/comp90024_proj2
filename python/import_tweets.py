import argparse
import database
import json
import mmap
from mpi4py import MPI


DB_AUTH = {"ADDRESS": "172.26.128.60", "PORT": "5984", "COUCHDB_USER": "admin", "COUCHDB_PASSWORD": "group27"}
# DB_NAME = 'tweets'


def parse_tweet(line: str):
    row = None
    if line.startswith('{"id"'):
        if line.endswith('}},'):
            row = json.loads(line[:-1])
        elif line.endswith('}}'):
            row = json.loads(line)
        elif line.endswith('}}]}'):
            row = json.loads(line[:-2])
    return row


def parse_tweets(lines: list):
    return [parse_tweet(row) for row in lines]


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

    # Initialise MPI necessary variable
    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()
    size = comm.Get_size()

    send_buffer = []
    next_target = 1
    batch_size = 30

    if rank == 0:
        # master process
        # load the twitter data file line by line
        with open(filename, "r", encoding="utf8") as f:

            mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)

            while True:
                line = mm.readline().strip().decode("utf8")
                if line == "":
                    break
                send_buffer.append(line)
                if len(send_buffer) >= batch_size:
                    comm.send(send_buffer, dest=next_target)
                    next_target += 1
                    if next_target == size:
                        next_target = 1
                    send_buffer = []
            mm.close()

            if len(send_buffer) > 0:
                comm.send(send_buffer, dest=next_target)
            for i in range(1, size):
                comm.send(None, dest=i)
    else:
        # slave process
        # receive twitter data string list from master process then process twitter data
        while True:
            data = comm.recv(source=0)
            if data is None:
                break
            rows = parse_tweets(data)
            db.insert_tweets(rows)
