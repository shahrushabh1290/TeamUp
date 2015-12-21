from flask import Flask, render_template, request, url_for
import time
from pymongo import MongoClient, GEO2D
import os
from bson import json_util
import json
import uuid
from bson.objectid import ObjectId
import urllib2
import ast

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
    user_fb_id = str(request.args.get('user_fb_id'))
    latitude = request.args.get('lat')
    longitude = request.args.get('long')
    print user_fb_id, latitude, longitude
    radius = int(request.args.get('radius'))
    print radius
    tag = request.args.get('search_query')
    tag = tag.lower()

    location = [float(latitude), float(longitude)]
    print location
    print "Login with: \n (user_fb_id, location, radius): (", user_fb_id, ', ', location, ', ', radius,')'

    cursor = col_users.find({'user_fb_id': user_fb_id})
    events_list = []
    res = dict()

    if tag == '':
        #If there is no search query we retrieve the users interests
        if cursor.count() == 0:
            print "user doesn't exist !"
            interests=[]
        else:
            for user in cursor:
                interests = user['interests']
        if len(interests) == 0:
            for doc in col_events.find({"location.coordinates": {"$within": {"$center": [location, radius]}}}).sort("start_time"):
                events_list.append(doc)
            res['events'] = events_list
            print "Without search query and interests: ", str(events_list)
        else:
            for doc in col_events.find({"tag": {"$in": interests}, "location.coordinates": {"$within": {"$center": [location, radius]}}}).sort("start_time"):
                events_list.append(doc)
            res['events'] = events_list
            print "Without search query but with interests: ", str(events_list)           
    else:
        tag = '/' + tag + '/' 
        print "Searching with tag: ", tag
        for doc in col_events.find({"tag": {"$regex": tag}, "location.coordinates": {"$within": {"$center": [location, radius]}}}).sort("start_time"):
            events_list.append(doc)
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


@application.route('/subscribe', methods=['POST'])
def subscribe_event():
    event_id = request.form["event_id"]
    user_id = request.form["user_id"]

    event_id = ast.literal_eval(event_id)
    event_id = event_id['$oid']
    
    col_events.update_one(
    {"_id": ObjectId(event_id)},
    {"$push": {"enrolment": user_id}}
    )
    return "Subscription done !"


@application.route('/unsubscribe', methods=['POST'])
def unsubscribe_event():
    event_id = request.form["event_id"]
    user_id = request.form["user_id"]
    event_id = ast.literal_eval(event_id)
    event_id = event_id['$oid']

    col_events.update_one(
    {"_id": ObjectId(event_id)},
    {"$pull": {"enrolment": user_id}}
    )
    return "Unsubscription done !"



@application.route('/create_event', methods=['GET', 'POST'])     # Difference between edit and create event in the front end ?
def create_event():
    """ method that creates OR update an existing event if the event_id field is not empty in the post request """
    try:
        if request.method == 'POST':
            # If we want to create a new event
            if request.form.get('event_id') == None:
                tag = request.form['tag'].lower()
                title = request.form['title'].lower()
                start_time = request.form['startTime']
                end_time = request.form['endTime']
                user_id = request.form['user_id'] 
                capacity = request.form['capacity']
                description = request.form['description']
                lat = request.form['lat']
                longi = request.form['long']
                enrolment = [user_id]
                loc_raw = request.form['locationRaw']

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
                    'loc_raw': loc_raw   
                    })

                return 'Event created !'
            else:
                # We update an existing event
                event_id = request.form['event_id']
                tag = request.form['tag'].lower()
                title = request.form['title'].lower()
                start_time = request.form['startTime']
                end_time = request.form['endTime']
                creator = request.form['creator'] 
                capacity = request.form['capacity']
                description = request.form['description']
                lat = request.form['lat']
                longi = request.form['long']
                enrolment = [creator]
                loc_raw = request.form['locationRaw']

                #Editing the event
                location_event = {'type': 'Point', 'coordinates': [float(lat), float(longi)] }

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
                    'loc_raw': loc_raw   
                    })

                col_events.update({ "_id": ObjectId(event_id)}, {"$set": event})
                return 'Event successfully updated'
    except KeyError, e:
        raise
    return 'This is a get request'



@application.route('/insert_user')
def insert_user():
    user_fb_id = request.args.get('user_fb_id')
    cursor = col_users.find({'user_fb_id': user_fb_id}) 
    var = "User already known"
    if cursor.count() != 0:
        var = "New user inserted !"
        user = dict({
            'user_fb_id': user_fb_id,
            # 'events_subscribed': [],
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
        interests += " "+str(k)
    res['interests'] = interests
    print res
    return json.dumps(res, default=json_util.default)
     

@application.route('/add_interest', methods=['GET', 'POST'])
def add_interest():
    """ add interests to a given user when they are seperated by comas """
    interest_added = request.form['new_interest'].encode('ascii','ignore')
    interest_added=interest_added.replace('"',"")
    interest_added=interest_added[1:len(interest_added)-1].split(",")
    user_fb_id = request.form['user_fb_id']
    col_users.update(
    {"user_fb_id": user_fb_id},
    { "$addToSet": {"interests": {"$each": interest_added }}}, 
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


@application.route('/facebook_friends', methods=['GET', 'POST'])
def get_facebook_friends():
    """ from a facebook short lived token returns a long lived token to the client (we need to catch the http errors..."""

    short_lived_token = request.form('short_lived_token')
    app_id = request.form['app_id']
    app_secret = request.form['app_secret']
    url = 'https://graph.facebook.com/oauth/access_token'+ '?grant_type=fb_exchange_token&client_id='+ app_id+ '&client_secret='+ app_secret+ '&fb_exchange_token='+ short_lived_token
    
    long_lived_token = urllib2.urlopen(url).read()
    if 'access_token=' in long_lived_token:
        long_lived_token = long_lived_token.replace('access_token', '')

    return long_lived_token



if __name__ == "__main__":
    application.run(debug=True)
