from pymongo import MongoClient
import random
import os
import time


MONGO_DB_URL = os.environ.get('MONGO_DB_URL')
col_users = MongoClient(MONGO_DB_URL).test.users
col_events = MongoClient(MONGO_DB_URL).test.events

tags = ['basket', 'rugby', 'soccer', 'cricket', 'tennis']

for k in range(10):
    index_tag = random.randint(0, len(tags)-1)
    print index_tag
    col_events.insert(
        {
        'title': 'aa',
        'tag': tags[index_tag],
        'enrolment': random.randint(0, 10),
        'privacy': random.randint(0,1),
        'notifications': [],
        'start_time': int(time.time()),
        'end_time': int(time.time()),
        'creator': random.randint(0,50),
        'capacity': 10,
        'description': 'brgd',
        'location': {'type': 'Point', 'coordinates': [random.uniform(-80., 80.), random.uniform(-80., 80.)] },
        })