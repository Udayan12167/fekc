#!flask/bin/python
from flask import Flask, request
from flask_restful import Resource, Api, reqparse
from pymongo import MongoClient
from bson.objectid import ObjectId
from bson.json_util import dumps
from pushjack import GCMClient
from ast import literal_eval
import json

app = Flask(__name__)
api = Api(app)

gcm_client = GCMClient(api_key='AIzaSyC2KtWM8Ce1lWrtIN-Ql2J0c5bPZc0iLQc')

def connect():
    connection = MongoClient("localhost", 27017)
    handle = connection["test"]
    return handle

handle = connect()

errors = {
    'AuthenticationError': {
        'message': "Token was incorrect",
        'status': 200,
    },
    'ResourceDoesNotExist': {
        'message': "A resource with that ID no longer exists.",
        'status': 410,
        'extra': "Any extra information you want.",
    },
}

user_parser = reqparse.RequestParser()
user_parser.add_argument('fbtoken')
user_parser.add_argument('fbid')
user_parser.add_argument('gcmtoken')

class User(Resource):
    def get(self, user_id):
        user = handle.users.find_one({'_id': ObjectId(user_id)})
        return dumps(user)

    def delete(self, user_id):
        deleted = handle.users.delete_one({'_id': ObjectId(user_id)})
        return {"delete_count": deleted.deleted_count}, 204

    def put(self, user_id):
        args = user_parser.parse_args()
        user = handle.users.find_one({'_id': ObjectId(user_id)})
        if user["fbtoken"] == args["fbtoken"]:
            updated = handle.users.update_one({'_id': ObjectId(user_id)},
                                              {'$set': args})
            return {"mod_count": updated.modified_count}, 201
        return errors["AuthenticationError"]


class UserList(Resource):
    def post(self):
        args = user_parser.parse_args()
        print args
        user = {'fbtoken': args["fbtoken"],
                'fbid': args["fbid"]}
        user_id = handle.users.insert_one(user)
        return {'oid': str(user["_id"])}


task_track_parser = reqparse.RequestParser()
task_track_parser.add_argument('fbtoken')


class TrackedTaskList(Resource):
    def get(self, user_id):
        args = task_track_parser.parse_args()
        user = handle.users.find_one({'_id': ObjectId(user_id)})
        if user["fbtoken"] == args["fbtoken"]:
            task_mappings = handle.tracked_tasks.find({'user_id': user["fbid"]})
            tasks = []
            for t in task_mappings:
                print t
                task_id = t["tracked_task"]
                print task_id
                task = handle.tasks.find_one({'_id': ObjectId(task_id)})
                if task:
                    tasks.append(str(task["task"]))
                    print(task.__dict__)
            dictionary = {}
            dictionary['tasks'] = tasks
            return str(dictionary)

task_parser = reqparse.RequestParser()
task_parser.add_argument('task')
task_parser.add_argument('user_id')


class Tasks(Resource):
    def post(self):
        args = task_parser.parse_args()
        print args["task"]
        task = {'user_id': args["user_id"],
                'task': args["task"]}
        tracked_friends = literal_eval(args["task"])["friends"]
        task_id = handle.tasks.insert_one(task)
        for friend in tracked_friends:
            task_entry = {'user_id': friend, 'tracked_task': str(task["_id"])}
            handle.tracked_tasks.insert_one(task_entry)
            f = handle.users.find_one({'fbid': friend})
            if f:
                gcm_client.send(f["gcmtoken"], literal_eval(args["task"]))
        return {'tid': str(task["_id"])}

api.add_resource(UserList, '/users')
api.add_resource(User, '/user/<user_id>')
api.add_resource(Tasks, '/tasks')
api.add_resource(TrackedTaskList, "/tracked_tasks/<user_id>")

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
