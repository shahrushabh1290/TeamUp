from pymongo import MongoClient
import random
import os
import time


MONGO_DB_URL = os.environ.get('MONGO_DB_URL')
col_users = MongoClient(MONGO_DB_URL).test.users
col_events = MongoClient(MONGO_DB_URL).test.events

events = False
users = True


# Creation of a dummy events database
if events:
    tags = ['basket', 'rugby', 'soccer', 'cricket', 'tennis']

    for k in range(10):
        index_tag = random.randint(0, len(tags)-1)
        print index_tag
        col_events.insert(
            {
            'title': 'aa',
            'tag': tags[index_tag],
            'enrolment': [],
            'privacy': random.randint(0,1),
            'notifications': [],
            'start_time': int(time.time()),
            'end_time': int(time.time()),
            'creator': random.randint(0,50),
            'capacity': 10,
            'description': 'brgd',
            'location': {'type': 'Point', 'coordinates': [random.uniform(-80., 80.), random.uniform(-80., 80.)] },
            })




# Creation of a dummy users database
if users:
    tags = ['basket', 'rugby', 'soccer', 'cricket', 'tennis']
    events = ['event1', 'event2', 'event3', 'event4', 'event5']
    fb_friends = range(5)

    for k in range(10):
        index_tag = random.randint(0, len(tags)-1)
        print index_tag
        col_users.insert(
            {
            'user_fb_id': '',
            'interests': [tags[index_tag]],
            'events_subscribed': [events[index_tag]],
            'fb_friends': [fb_friends[index_tag]]
            })



