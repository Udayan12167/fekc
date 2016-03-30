#!flask/bin/python
from flask import Flask, request
from flask_restful import Resource, Api, reqparse
from pymongo import MongoClient
from bson.objectid import ObjectId
from bson.json_util import dumps

app = Flask(__name__)
api = Api(app)


def connect():
    connection = MongoClient("localhost", 27017)
    handle = connection["test"]
    return handle

user_parser = reqparse.RequestParser()
user_parser.add_argument('name')
user_parser.add_argument('fbtoken')
user_parser.add_argument('gcmtoken')

handle = connect()


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
            updated = handle.tasks.update_one({'_id': ObjectId(user_id)},
                                              {'$set': args})
        return {"mod_count": updated.modified_count}, 201


class UserList(Resource):
    def post(self):
        args = user_parser.args()
        user = {'name': args["name"], "fbtoken": args["fbtoken"]}
        user_id = handle.tasks.insert_one(user)
        return dumps(user)

api.add_resource(UserList, '/users')
api.add_resource(User, '/user/<user_id>')

if __name__ == '__main__':
    app.run(debug=True)
