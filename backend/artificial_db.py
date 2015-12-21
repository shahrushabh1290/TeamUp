from pymongo import MongoClient
import random
import os
import time


MONGO_DB_URL = os.environ.get('MONGO_DB_URL')
col_users = MongoClient(MONGO_DB_URL).test.users
col_events = MongoClient(MONGO_DB_URL).test.events

events = True
users = True


# Creation of a dummy events database
if events:
    tags = ['basket', 'rugby', 'soccer', 'cricket', 'tennis']
    titles = ['Soccer at central park', 'Baseball game !!', 'tennis downtown', 'rugby in the bronx', 'Cricket at wankhede', 'Jog in Central Park', 'waterpolo in paris', 'hiking in the Alps', 'squash at Columbia', 'Rugby game at Dartmouth']
    description = ["Let's play soccer in the north of central park, I have ball. Everybody is welcome to join !",
                    "Anyone down for playing Baseball this afternoon ? My friends and I are beginners and would love to play with experienced players",
                    "Need a player for a tennis game down asap. Level: intermediate",
                    "It is a beautiful afternoon, let's play rugby !",
                    "",
                    "Need a teammate for my marathon preparation",
                    "The pool is booked, we only need more players !",
                    "For every people passionated by the mountain !",
                    "Need a teammate asap for a squash game !",
                    "A warm up is organised with the Dartmouth rugby team, everybody is welcome to join !"]
    locationNames = ['Central Park', 'Central Park, North', 'Soho', 'Lansdowne Rugby Club', 
                    "Wankhede, Mumbai", "Reservoir, Central Park", "Paris, XVIe", "Chamonix", "Columbia University", "Dartmouth, Hanover"]

    for k in range(10):
        index_tag = random.randint(0, len(tags)-1)
        print index_tag
        col_events.insert(
            {
            'title': titles[k],
            'tag': tags[index_tag],
            'enrolment': [],
            'locRaw' : locationNames[k],
            'privacy': random.randint(0,1),
            'notifications': [],
            'start_time': int(time.time()),
            'end_time': int(time.time()),
            'creator': random.randint(0,50),
            'capacity': 10,
            'description': description[k],
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
            'user_fb_id': str(k),
            'interests': [tags[index_tag]],
            # 'events_subscribed': [],
            'fb_friends': [fb_friends[index_tag]]
            })



