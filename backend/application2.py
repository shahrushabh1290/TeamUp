from flask import Flask, render_template, request, url_for
import time
from pymongo import MongoClient, GEO2D
import os
from bson import json_util
import json
import uuid
from bson.objectid import ObjectId

application = Flask(__name__)

MONGO_DB_URL = os.environ.get('MONGO_DB_URL')
col_users = MongoClient(MONGO_DB_URL).test.users

col_events = MongoClient(MONGO_DB_URL).test.events
#col_events.create_index([("location.coordinates", GEO2D)])



#Show login page
@application.route('/')
def hello():
    return render_template('hello.html')


@application.route('/display_events', methods=['POST', 'GET'])
def display_events():
    user_id = str(request.args.get('user_id'))
    latitude = request.args.get('lat')
    longitude = request.args.get('long')
    print user_id, latitude, longitude
    radius = int(request.args.get('radius'))
    print radius
    tag = request.args.get('search_query')
    tag = tag.lower()

    location = [float(latitude), float(longitude)]
    print location
    print "Login with: \n (user_id, location, radius): (", user_id, ', ', location, ', ', radius,')'

    cursor = col_users.find({'user_id': user_id})
    if cursor.count() == 0:
        print "user doesn't exist !"

    # Implement the function get events (user_id, location, timestamp, tag_query=None)
    events_list = []
    res = dict()
    if tag == '':
        #retrieve user interest ...
        for doc in col_events.find({"location.coordinates": {"$within": {"$center": [location, radius]}}}).sort("start_time"):
            events_list.append(doc)
        res['events'] = events_list
        print "Without search query: ", str(events_list)
    else:
        print "Searching with tag: ", tag
        for doc in col_events.find({"tag": tag, "location.coordinates": {"$within": {"$center": [location, radius]}}}).sort("start_time"):
            events_list.append(doc)
        # print "With search query: "
        # for k in events_list:
        #     print k
        #     print k['tag'], k['start_time']
        #     print '\n'
        print events_list
        res['events'] = events_list
    return json.dumps(res, default=json_util.default)


@application.route('/search', methods=['POST', 'GET'])
def display_search_events(user):
    if request.method == 'POST':
        tag_query = request.form['tag_query']
    redirect(url_for('display_events', tag_query=tag_query))


@application.route('/event_page')
def get_event():
    event_id = request.args.get('event_id')
    doc = col_events.find({'_id': ObjectId(event_id)})
    for i in doc:
        loc = dict()
        location = i['location']
        loc['lat'] = location['coordinates'][0]
        loc['long'] =location['coordinates'][1]
        i['location'] = loc
        event = json.dumps(i, default=json_util.default)
    return event

@application.route('/subscribe')
def subscribe_event():
    event_id = request.args.get("event_id")
    user_id = request.args.get("user_id")
    col_events.update_one(
    {"_id": ObjectId(event_id)},
    {"$push": {"enrolment": user_id}}
    )
    return "Subscription done !"


@application.route('/create_event', methods=['GET', 'POST'])     # Difference between edit and create event in the front end ?
def create_event():
    print request.method
    try:
        if request.method == 'POST':
            print request.form
            print request.data
            user_id = request.form['user_id']
            loc_raw = request.form['locationRaw']
            tag = request.form['tag'].lower()
            title = request.form['title'].lower()
            start_time = request.form['startTime']
            print tag, title, start_time

            end_time = request.form['endTime']
            #creator = request.form['creator'] 
            capacity = request.form['capacity']
            description = request.form['description']
            print 'ok'

            lat = request.form['lat']
            longi = request.form['long']
            print lat, longi

            enrolment = [user_id]


            #Editing the event
            location_event = {'type': 'Point', 'coordinates': [float(lat), float(longi)] }

            event = dict({
                'tag': tag,
                'title': title,
                'start_time': start_time,
                'end_time': end_time,
                'creator': user_id,
                'capacity': capacity,
                'description': description,
                'location': location_event,
                'enrolment': enrolment,
                'locRaw' : loc_raw   
                })
            # col_events.update({ "_id": ObjectId(event_id)}, event)
            col_events.insert(event)
            return 'Event created !'

    except KeyError, e:
        raise

    return 'fuck get request'



@application.route('/insert_user')
def insert_user():
    user_fb_id = request.args.get('user_fb_id')
    cursor = col_users.find({'user_fb_id': user_fb_id}) 
    var = "User already known"
    if cursor.count() != 0:
        var = "New user inserted !"
        user = dict({
            'user_fb_id': user_fb_id,
            'events_subscribed': [],
            'interests': [],
            'fb_friends': []
            })
        col_users.insert(user)
    return var


@application.route('/get_interest')
def get_interest():
    res={}
    user_id = request.args.get('user_id')
    cursor = col_users.find_one({'user_fb_id': user_id})
    interests = ""
    for k in cursor['interests']:
        interests += str(k)
    res['interests'] = interests
    print res
    return json.dumps(res, default=json_util.default)
     

@application.route('/add_interest', methods=['GET', 'POST'])
def add_interest():
    interest_added = request.form('new_interest')
    print interest_added
    user_fb_id = request.args.get('user_fb_id')
    col_users.update(
    {"user_fb_id": user_fb_id},
    { "$addToSet":{"interests": interest_added} }, 
    upsert=True)
    return "Interest added !"


    
@application.route('/remove_interest', methods=['GET', 'POST'])
def remove_interest():
    user_fb_id = request.args.get('user_fb_id')
    interests_to_remove = request.args.get('interests_to_remove')
    interests_to_remove = interests_to_remove.split(',')
    col_users.update(
    {"user_fb_id": user_fb_id},
    { "$pull": { "interests": {"$in":  interests_to_remove} }}, 
    multi=True)
    return "Interest deleted !"  



if __name__ == "__main__":
    application.run(debug=True)
