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
col_events.create_index([("location.coordinates", GEO2D)])



#Show login page
@application.route('/')
def hello():
    return render_template('hello.html')


@application.route('/display_events', methods=['POST', 'GET'])
def display_events():
    user_id = str(request.args.get('user_id'))
    latitude = request.args.get('lat')
    longitude = request.args.get('long')
    radius = int(request.args.get('radius'))
    tag = request.args.get('search_query')

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
    try:
        if request.method == 'POST':
            # user_id = request.form['user_id']
            
            tag = request.form['tag']
            print "ok"
            title = request.form['title']

            start_time = request.form['start_time']

            end_time = request.form['end_time']
            creator = request.form['creator']
            capacity = request.form['capacity']
            description = request.form['description']
            location_event = request.form['location']
            enrolment = [user_id]


            #Editing the event
            # event_id = request.form['event_id']
            event = dict({
                'tag': tag,
                'title': title,
                'start_time': start_time,
                'end_time': end_time,
                'creator': creator,
                'capacity': capacity,
                'description': description,
                'location': location_event,
                'enrolment': enrolment,   
                })
            col_events.update({ "_id": ObjectId(event_id)}, event)
            return 'Event updated !'

    except KeyError, e:
        raise

    return 'fuck get request'



if __name__ == "__main__":
    application.run(debug=True)
