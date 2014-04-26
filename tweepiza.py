#!/usr/bin/env python
import os, sys, re, time
import tweepy
#from nltk.chat.eliza import eliza_chatbot as bot

# Set these in your .bashrc or something.
# Find them on the twitter dev web site for the '@rhlize' user, weak password 't*'
at =os.getenv('RHLIZA_ACCESS_TOKEN')
ats=os.getenv('RHLIZA_ACCESS_TOKEN_SECRET')
ck =os.getenv('RHLIZA_CONSUMER_KEY')
cs =os.getenv('RHLIZA_CONSUMER_SECRET')

print at
print ats
print ck
print cs
auth = tweepy.OAuthHandler(ck, cs)
#redirect_url = auth.get_authorization_url()
#print "Must verify access at: ", redirect_url

auth.set_access_token(at, ats)

api = tweepy.API(auth)

print api.me().name

replies = api.mentions_timeline()

for reply in reversed(replies):
    print reply.text


